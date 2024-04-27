import redis
from snowflake import SnowflakeGenerator

REDIS_HOST = "127.0.0.1"
REDIS_PORT = 6379
REDIS_PASSWORD = "abc"

redis_client = redis.StrictRedis(host=REDIS_HOST, port=REDIS_PORT, decode_responses=True, password=REDIS_PASSWORD)

gen = SnowflakeGenerator(42)

keys = ["extrainfo", "filepath", "isSendByPhone", "isSendMsg", "message", "self", "sender", "sign",
        "thumb_path", "time", "timestamp", "type", "wxid", "chatroom_name", "alias"]

types = ['text', 'file', 'image']