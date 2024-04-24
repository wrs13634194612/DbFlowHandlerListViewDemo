package com.example.chat.gateway;

import com.example.chat.service.LoginService;
import com.example.chat.service.MessageSendService;
import com.example.chat.service.RoomService;
import io.netty.channel.Channel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@RequiredArgsConstructor
@Component
public class TaskGateway {
    private final LoginService loginService;
    private final RoomService roomService;
    private final MessageSendService messageSendService;

    public void execute(Channel channel, Map<String, Object> requestData, Map<String, Object> responseData) throws Exception {

        String task = (String) requestData.getOrDefault("task", "");

        switch (task) {

            case "login":
                // 사용자 인증 처리
                loginService.login(channel, task, requestData, responseData);
                break;

            case "createRoom":
                // 룸 생성
                roomService.createRoom(channel, task, responseData);
                break;

            case "enterRoom":
                // 룸 입장
                roomService.enterRoom(channel, task, requestData, responseData);
                break;

            case "exitRoom":
                // 룸 퇴장
                roomService.exitRoom(channel, task, responseData);
                break;

            case "sendRoom":

                // 룸에 메세지 전송
                roomService.sendRoom(channel, task, requestData, responseData);
                break;

            default:
                messageSendService.sendMessage(channel, responseData, new Exception("메세지 구분이 정확하지 않습니다."), "1005");
        }

    }

}
