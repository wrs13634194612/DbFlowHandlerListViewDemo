package com.example.chat.repository;

import io.netty.channel.Channel;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class UserIdChannelRepository {

    private final Map<String, Channel> userIdChannelMap = new ConcurrentHashMap<>();

    public Map<String, Channel> getUserIdChannelMap(){
        return userIdChannelMap;
    }

}
