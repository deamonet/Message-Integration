from typing import Union
from pydantic import BaseModel


class Action(BaseModel):
    type: str
    params: dict

class Result(BaseModel):
    code: int
    message: str
    data: dict

class ResutlCode(BaseModel):
    code: int
    message: str
    data: dict