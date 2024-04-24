## Netty Toy Project MultiChatServer


#### 시스템 구성도

![image](https://github.com/LeeYuHwan/MultiChatServerForNetty/assets/66478929/dfb3a2fa-2898-44de-85da-a0f4b0a29c0b)

--------------------

#### Socket 통신 명세
--------------------
JSON Data를 이용하여 통신
--------------------
## 로그인
# Request Data
{
"userId": userId,
"password": password,
"task":"login"
}

# Response Data
{
"result":"로그인 완료",
"task":"login",
"resultCode":"0000"
}

--------------------
## 방 생성
# Request Data
{
"task":"createRoom"
}

# Response Data
{
"task":"createRoom",
"resultCode":"0000",
"roomId": roomId
}

--------------------
## 방 입장
# Request Data
{
"roomId": roomId,
"task":"enterRoom"
}

# Response Data
{"task":"enterRoom",
"resultCode":"0000",
"roomId": roomId
}

--------------------
## 방 퇴장
# Request Data
{
"roomId": roomId,
"task":"exitRoom"
}

# Response Data
{"task":"exitRoom",
"resultCode":"0000"
}

--------------------
## 메시지 전송
# Request Data
{
"msg": message,
"task":"sendRoom"
}

# Response Data
{
"msg": message,
"task":"sendRoom",
"resultCode":"0000",
"userName": userName,
"userId": userId
}
