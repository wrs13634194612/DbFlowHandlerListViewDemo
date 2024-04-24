package com.example.chat;

import com.example.chat.domain.UserInfo;
import com.example.chat.netty.NettyChatServer;
import com.example.chat.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TCPServer {
    private final NettyChatServer nettyChatServer;
    private final UserRepository userRepository;

    @PostConstruct
    public void init() throws InterruptedException {

        // 테스트용 사용자 계정 입력
        UserInfo user = new UserInfo();
        user.setUserName("도하");
        user.setUserId("doha123");
        user.setPassword("1234");

        userRepository.save(user);

        user = new UserInfo();
        user.setUserName("성만");
        user.setUserId("sungmen123");
        user.setPassword("1234");

        userRepository.save(user);

        nettyChatServer.run();
    }

}
