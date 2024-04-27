import inspect
import sys

import comtypes.client
import nacos
from fastapi import FastAPI

from dto.models import Action
from utils.constant import redis_client, keys, types
from utils.functions import get_wechat_pid_list
from wxrobot.wxRobot import WeChatRobot

# Both HTTP/HTTPS protocols are supported, if not set protocol prefix default is HTTP, and HTTPS with no ssl check(verify=False)
# "192.168.3.4:8848" or "https://192.168.3.4:443" or "http://192.168.3.4:8848,192.168.3.5:8848" or "https://192.168.3.4:443,https://192.168.3.5:443"
SERVER_ADDRESSES = "server addresses split by comma"
NAMESPACE = "namespace id"

# no auth mode
client = nacos.NacosClient(SERVER_ADDRESSES, namespace=NAMESPACE)
# auth mode
# client = nacos.NacosClient(SERVER_ADDRESSES, namespace=NAMESPACE, ak="{ak}", sk="{sk}")
client.add_naming_instance("wexin-robot", ip, port, cluster_name, weight, metadata, enable, healthy)

# get config
data_id = "config.nacos"
group = "group"
print(client.get_config(data_id, group))



app = FastAPI()


# 直接启动即可
pid_list = get_wechat_pid_list()
if not pid_list:
    print('没有发现微信进程！')
    sys.exit(-1)

print(f'微信进程：{pid_list}')

wxRobot = WeChatRobot(pid_list[0])
wxRobot.StartService()
msgs = {}


@app.get("/api/message")
async def get_message():
    msgids = redis_client.keys("msg*")
    for msgid in msgids:
        msg = {}
        for key, item in zip(keys, redis_client.lrange(msgid, 0, len(keys) - 1).reverse()):
            msg[key] = item

        msgs[msgid] = msg

    return msgs


@app.put("/api/message/{msgid}")
async def read_message(msgid):
    redis_client.expire(msgid, 30)


@app.get("/api/sender/{wxid}/messages")
async def get_contact_message(wxid):
    temp_msgs = {}
    for msgid, msg in msgs.items():
        if msg['sender'] == wxid:
            temp_msgs[msgid] = msg

    return temp_msgs


@app.post("/api/message")
async def send_message(action: Action):
    """
    调用robot的发送消息、获取消息api
    :param
    post json body actions {'type': 'file', params: {'receiver': 'filehelper', 'filepath': 'C:\\1.jpg'}}
    :return:{
        'code': 200(成功)/500(报错),
        'data': [None, dict(), list(), object](根据函数返回值确定),
        'message': '操作成功/失败理由'
    }
    """
    comtypes.CoInitialize()
    robot = comtypes.client.CreateObject("WeChatRobot.CWeChatRobot")
    event = comtypes.client.CreateObject("WeChatRobot.RobotEvent")
    robot = WeChatRobot(pid_list[0], robot, event)
    send_type = action['type']
    if send_type not in types:
        return {'code': 500, 'data': None, 'message': '缺少指定的type名称，请查看示例代码'}

    params = action['params']
    method_name = "Send{}".format(send_type[0].upper() + send_type[1:])
    func = getattr(robot, method_name)
    kwargs = {}
    func_info = inspect.getfullargspec(func)
    default_length = 0
    if func_info.defaults:
        defaults = func_info.defaults
        default_length = len(defaults)
    else:
        defaults = tuple()
    func_args = func_info.args
    args_length = len(func_args)
    for index, arg in enumerate(func_args):
        if arg == 'self':  # 方法本身的self参数不用传递
            continue
        if arg in params:
            kwargs[arg] = params[arg]
        else:
            if index < args_length - default_length:
                print(f'缺少必要参数：{arg}')
                return {'code': 500, 'data': None,
                        'message': f'{method_name}方法，缺少必要参数：{arg}。{{"params": {{"{arg}": "xxx"}}'}
            # print('a', index - (args_length - default_length))
            kwargs[arg] = defaults[index - (args_length - default_length)]

    comtypes.CoInitialize()
    try:
        result = func(**kwargs)  # 调用对应api
        return {'code': 200, 'data': result, 'message': f'操作成功'}
    except Exception as e:
        return {'code': 500, 'data': None, 'message': f'执行{method_name}方法出错：{e}'}
