package com.example.chat.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.Channel;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class MessageSendService {
    private final ObjectMapper objectMapper;

    public void sendMessage(Channel channel, Map<String, Object> responseData, Throwable throwable, String resultCode) throws Exception{
        responseData.put("resultCode", resultCode);
        responseData.put("msg", ExceptionUtils.getStackTrace(throwable));

        channel.writeAndFlush(sendMessage(responseData));
    }

    void sendMessage(Channel channel, Map<String, Object> responseData, String task) {

        responseData.put("resultCode", "0000");
        responseData.put("task", task);

        try {

            channel.writeAndFlush(sendMessage(responseData));

        } catch (Exception e) {

            e.printStackTrace();

        }


    }

    Object sendMessage(Map<String, Object> responseData) throws Exception {
        return objectMapper.writeValueAsString(responseData) + System.lineSeparator();
    }

    public Object sendMessage(String message) {
        return message + System.lineSeparator();
    }

}
