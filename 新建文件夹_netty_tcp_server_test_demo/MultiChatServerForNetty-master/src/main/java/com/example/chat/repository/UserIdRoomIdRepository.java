package com.example.chat.repository;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class UserIdRoomIdRepository {

    private final Map<String, String> userIdRoomIdMap = new ConcurrentHashMap<>();

    public Map<String, String> getUserIdRoomIdMap(){
        return userIdRoomIdMap;
    }

}
