package com.example.chat.repository;

import io.netty.channel.ChannelId;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ChannelIdUserIdRepository {
    private final Map<ChannelId, String> channelIdUserIdMap = new ConcurrentHashMap<>();

    public Map<ChannelId, String> getChannelIdUserIdMap(){
        return channelIdUserIdMap;
    }

}
