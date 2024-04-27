# -*- coding:utf-8 -*-
# create: 2022/10/20 10:23
# author: ly1102

import asyncio
import io
import json
import sys

import comtypes
from comtypes import client

from utils import redis_client, keys
from wxrobot.wxRobot import WeChatRobot

sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8')

TCP_HOOK_PORT = 10808  # hook的微信消息接收端口，保证不被占用了就行


class MessageTCPServer:
    """
    同时启动TCP server监听微信的消息hook推送
    接收到微信消息：WeChat ---> TCP server ---> Redis
    """

    def __init__(self):
        self.ping_interval = 5  # 多久发送一次ping，保持连接。单位秒
        self.r = redis_client

    async def handle(self, reader: asyncio.StreamReader, writer: asyncio.StreamWriter):
        """
        TCP Server的主函数，微信收到消息后通过Hook推送到该函数，本系统使用的短链接，即微信收到
        :param reader: tcp reader
        :param writer:  tcp writer
        :return: None
        """
        comtypes.CoInitialize()
        while True:
            ptr_data = b""
            try:
                while True:
                    # Python源码中并未找到设置eof的地方，所以无奈之下采用直接修改的方式，
                    # 使得没有接收到数据就一直保持await状态，否则一await就会自动返回空字节
                    reader._eof = False  # 这一行非常非常重要，如果不添加这个语句，会导致reader一直返回空字节
                    data = await reader.read(1024)
                    # print(reader.at_eof(), reader._eof)
                    ptr_data += data
                    if len(data) == 0 or data[-1] == 0xA:
                        break
                print(f'receive raw data:\n{ptr_data}\n')
                try:
                    if ptr_data:
                        msg = json.loads(ptr_data.decode('utf-8'))
                        print(f'receive decode message:\n{msg}\n')
                        await self.msg_callback(msg)
                except json.JSONDecodeError as e:
                    print(f'JSON解码失败：{e}')
            except OSError as e0:
                print(f'OS Error: {e0}')
                break
            except Exception as e:
                print(f'TCP server未知异常：{e}')
                break

            writer.write("200 OK".encode())
            await writer.drain()
            # 每条消息都有一条新的连接，接收到空字节的时候即为接收完成，此时应跳出循环关闭链接，
            # 否则报错Task was destroyed but it is pending!
            if ptr_data == b'':
                break

        writer.close()
        comtypes.CoUninitialize()
        return

    async def msg_callback(self, msg_origin):
        """
        回调函数，收到TCP Server的消息后，通过该函数对内容进行处理和转发到Java储存到Redis
        :param msg_origin: ReceiveMsgStruct
        :return: None
        """
        # 主线程中已经注入，此处禁止调用StartService和StopService
        # msg = {'pid': msg_origin.pid,
        #       'time': msg_origin.time,
        #       'type': msg_origin.type,
        #       'isSendMsg': msg_origin.isSendMsg,
        #       'wxid': msg_origin.wxid,
        #       'sendto' if msg_origin.isSendMsg else 'from': msg_origin.sender,
        #       'message': msg_origin.message}
        msg = msg_origin
        robot = client.CreateObject("WeChatRobot.CWeChatRobot")
        event = client.CreateObject("WeChatRobot.RobotEvent")
        wx = WeChatRobot(msg_origin['pid'], robot, event)
        userinfo = wx.GetWxUserInfo(msg_origin['wxid'])
        if 'wxRemark' in userinfo and userinfo['wxRemark']:  # 有备注就显示备注名
            msg['alias'] = userinfo['wxRemark']
        else:
            msg['alias'] = userinfo['wxNumber']  # 无备注就显示微信号

        msg['chatroom_name'] = ""
        # isSendMsg 表示是否是本机发出的消息
        # 1 表示本机发出，0 表示其他人发出
        if msg_origin["isSendMsg"] == 0:
            # 如果是群聊
            if '@chatroom' in msg_origin['sender']:
                chatroom_info = wx.GetWxUserInfo(msg_origin['sender'])
                # {'wxBackground': 'null'             , 'wxBigAvatar': 'null', 'wxCity'     : 'null', 'wxId'      :
                # '18493567127@chatroom', 'wxNation': 'null', 'wxNickName' : '重生之老废物乐园', 'wxNumber'  : 'null',
                # 'wxProvince': 'null', 'wxRemark'   : 'null', 'wxSignature': 'null', 'wxSmallAvatar':
                # 'http://wx.qlogo.cn/mmcrhead
                # /PiajxSqBRaELUuUWdJfZ3lNSe1L1a2dy7RupMZicicLiaYQqKKuwcNyzV1ufOAgj336cOFDaAkEa44uWC2pspUPgu9CUnhwk7ZTr/0', 'wxV3': 'v3_020b3826fd030100000000008555349ee6b83c000000501ea9a3dba12f95f6b60a0536a1adb69ebdff5bd1c65ee89df1e18d11a71d540713fa9c9eefcf3db7e37b5953f6f545ae3f585ed6f16075f017dfb17d7e117a3e943aab814e5ed47cf85832@stranger' }
                msg['chatroom_name'] = chatroom_info['wxNickName']
                msg['nickname'] = wx.GetChatRoomMemberNickname(msg_origin['sender'], msg_origin['wxid'])
            else:
                msg['nickname'] = userinfo['wxNickName']

        print(f"final message: \n{msg}\n")
        try:
            self.r.lpush(f"msgkey", msg['msgid'])
            for key in keys:
                self.r.lpush(f"msg{msg['msgid']}", msg[key])
        except Exception as e:
            print(f'储存消息到Redis失败：{e}')
        robot.Release()
        event.Release()

    async def start_tcp_server(self):
        """
        启动监听微信tcp发送过来的消息推送服务器
        :return:
        """
        server = await asyncio.start_server(self.handle, '127.0.0.1', TCP_HOOK_PORT)  # 开始监听微信消息
        addr = server.sockets[0].getsockname()
        print(f'TCP Serving on {addr}')
        async with server:
            await server.serve_forever()

    def run(self):
        """
        启动程序，开启TCP Server的监听
        :return: None
        """
        server_loop = asyncio.new_event_loop()
        server_loop.create_task(self.start_tcp_server())
        server_loop.run_forever()


if __name__ == '__main__':
    messageServer = MessageTCPServer()
    messageServer.run()