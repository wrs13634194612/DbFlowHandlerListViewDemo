package com.example.chat.netty.handler;

import com.example.chat.gateway.TaskGateway;
import com.example.chat.service.LoginService;
import com.example.chat.service.MessageSendService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@ChannelHandler.Sharable
public class NettyChatServerHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private LoginService loginService;
    @Autowired
    private MessageSendService messageSendService;
    @Autowired
    private TaskGateway taskGateway;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("Netty Chat Server Connected");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("Client say :" + msg.toString());

        Map<String, Object> requestData;
        Map<String, Object> responseData = new HashMap<>();
        try {
            requestData = objectMapper.readValue((String) msg, new TypeReference<>() {});
            log.info("REQUEST DATA : {}", requestData);

        } catch (JsonParseException | JsonMappingException e) {
            e.printStackTrace();

            messageSendService.sendMessage(ctx.channel(), responseData, e, "1001");
            return;
        }

        taskGateway.execute(ctx.channel(), requestData, responseData);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        loginService.logout(ctx.channel());
        ctx.close();

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
