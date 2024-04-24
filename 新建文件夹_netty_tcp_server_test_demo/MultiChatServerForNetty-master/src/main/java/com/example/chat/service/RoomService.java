package com.example.chat.service;

import com.example.chat.domain.UserInfo;
import com.example.chat.repository.*;
import io.netty.channel.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomService {

    private final UserIdChannelRepository userIdChannelRepository;
    private final ChannelIdUserIdRepository channelIdUserIdRepository;
    private final UserIdRoomIdRepository userIdRoomIdRepository;
    private final RoomIdUserIdRepository roomIdUserIdRepository;
    private final UserRepository userRepository;
    private final LoginService loginService;
    private final MessageSendService messageSendService;

    public void createRoom(Channel channel, String task, Map<String, Object> responseData) throws Exception {
        String userId = channelIdUserIdRepository.getChannelIdUserIdMap().get(channel.id());
        responseData.put("task", task);

        if(userIdRoomIdRepository.getUserIdRoomIdMap().containsKey(userId)) {

            responseData.put("error", "Already entered user");
            messageSendService.sendMessage(channel, responseData, new Exception("룸에 입장해있는 사용자입니다."), "1006");
            return;
        }

        String roomId = UUID.randomUUID().toString();

        roomIdUserIdRepository.getRoomIdUserIdMap().add(roomId, userId);
        userIdRoomIdRepository.getUserIdRoomIdMap().put(userId, roomId);

        responseData.put("roomId", roomId);

        messageSendService.sendMessage(channel, responseData, task);
    }

    public void enterRoom(Channel channel, String task, Map<String, Object> requestData, Map<String, Object> responseData) throws Exception {
        String userId = channelIdUserIdRepository.getChannelIdUserIdMap().get(channel.id());

        if(userIdRoomIdRepository.getUserIdRoomIdMap().containsKey(userId)) {
            messageSendService.sendMessage(channel, responseData, new Exception("룸에 입장해있는 사용자입니다."), "1006");
            return;
        }

        String roomId = (String) requestData.get("roomId");

        if(!roomIdUserIdRepository.getRoomIdUserIdMap().containsKey(roomId)){
            messageSendService.sendMessage(channel, responseData, new Exception("존재하지 않는 룸 아이디입니다.."), "1007");
            return;
        }

        roomIdUserIdRepository.getRoomIdUserIdMap().add(roomId, userId);
        userIdRoomIdRepository.getUserIdRoomIdMap().put(userId, roomId);

        responseData.put("task", task);
        responseData.put("roomId", roomId);

        messageSendService.sendMessage(channel, responseData, task);
    }

    public void exitRoom(Channel channel, String task, Map<String, Object> responseData) throws Exception {

        String userId = channelIdUserIdRepository.getChannelIdUserIdMap().get(channel.id());

        if (!userIdRoomIdRepository.getUserIdRoomIdMap().containsKey(userId)) {
            messageSendService.sendMessage(channel, responseData, new Exception("룸에 존재하지 않습니다."), "1008");
            return;
        }

        String roomId = userIdRoomIdRepository.getUserIdRoomIdMap().get(userId);

        roomIdUserIdRepository.getRoomIdUserIdMap().remove(roomId, userId);
        userIdRoomIdRepository.getUserIdRoomIdMap().remove(userId);

        responseData.put("task", task);

        messageSendService.sendMessage(channel, responseData, task);

    }

    public void sendRoom(Channel channel, String task, Map<String, Object> requestData, Map<String, Object> responseData) throws Exception {

        String userId = channelIdUserIdRepository.getChannelIdUserIdMap().get(channel.id());

        if (!userIdRoomIdRepository.getUserIdRoomIdMap().containsKey(userId)) {
            messageSendService.sendMessage(channel, responseData, new Exception("룸에 존재하지 않습니다."), "1008");
            return;
        }

        UserInfo user = userRepository.findByUserId(userId);
        if (user == null) {
            messageSendService.sendMessage(channel, responseData, new Exception("사용자 정보를 조회할 수 없습니다.."), "1009");
            return;
        }

        String userName = user.getUserName();

        // 전달할 메세지 내용 생성
        responseData.put("task", task);
        responseData.put("userId", userId);
        responseData.put("userName", userName);
        responseData.put("msg", requestData.get("msg"));

        String roomId = userIdRoomIdRepository.getUserIdRoomIdMap().get(userId);

        // 룸에 메세지 전송
        roomIdUserIdRepository.getRoomIdUserIdMap().get(roomId).parallelStream().forEach(otherUserId -> {

            Channel otherChannel = userIdChannelRepository.getUserIdChannelMap().get(otherUserId);

            // 채널이 활성화 상태가 아니라면 사용자를 제거
            if (!otherChannel.isActive()) {

                loginService.logout(otherChannel);
                return;

            }

            messageSendService.sendMessage(otherChannel, responseData, task);
        });
    }
}
