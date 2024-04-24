package com.example.chat.service;

import com.example.chat.domain.UserInfo;
import com.example.chat.repository.*;
import io.netty.channel.Channel;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class LoginService {
    private final UserRepository userRepository;
    private final ChannelIdUserIdRepository channelIdUserIdRepository;
    private final UserIdChannelRepository userIdChannelRepository;
    private final UserIdRoomIdRepository userIdRoomIdRepository;
    private final RoomIdUserIdRepository roomIdUserIdRepository;
    private final MessageSendService messageSendService;

    public void login(Channel channel, String task, Map<String, Object> requestData, Map<String, Object> responseData) throws Exception {
        String userId = (String) requestData.get("userId");
        String password = (String) requestData.get("password");

        if (userId == null || password == null) {
            messageSendService.sendMessage(channel, responseData, new Exception("아이디 또는 비밀번호를 입력하세요."), "1002");
            return;
        }

        UserInfo user = userRepository.findByUserId(userId);

        if (user == null) {

            messageSendService.sendMessage(channel, responseData, new Exception("사용자 아이디가 존재하지 않습니다."), "1003");
            return;

        } else if (!StringUtils.equals(password, user.getPassword())) {

            messageSendService.sendMessage(channel, responseData, new Exception("비밀번호가 일치하지 않습니다."), "1004");
            return;

        }

        channelIdUserIdRepository.getChannelIdUserIdMap().put(channel.id(), userId);
        userIdChannelRepository.getUserIdChannelMap().put(userId, channel);

        responseData.put("result" , "로그인 완료");

        messageSendService.sendMessage(channel, responseData, task);
    }

    public void logout(Channel channel) {

        String userId = channelIdUserIdRepository.getChannelIdUserIdMap().get(channel.id());

        if(!StringUtils.isEmpty(userId)) {
            userIdChannelRepository.getUserIdChannelMap().remove(userId);
            String roomId = userIdRoomIdRepository.getUserIdRoomIdMap().get(userId);

            if(!StringUtils.isEmpty(roomId)){
                roomIdUserIdRepository.getRoomIdUserIdMap().remove(roomId, userId);
                userIdRoomIdRepository.getUserIdRoomIdMap().remove(userId);
            }

            channelIdUserIdRepository.getChannelIdUserIdMap().remove(channel.id());
        }

    }

}
