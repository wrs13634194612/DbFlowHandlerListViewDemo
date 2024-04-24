package com.example.chat.repository;


import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Repository
public class RoomIdUserIdRepository {
    private final MultiValueMap<String, String> roomIdUserIdMap = new LinkedMultiValueMap<>();

    public MultiValueMap<String, String> getRoomIdUserIdMap(){
        return roomIdUserIdMap;
    }

}
