package com.example.chat;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.Map;

public class NettyTest {
    final static Logger log = LoggerFactory.getLogger(NettyTest.class);
    public static void main(String[] args) throws Exception {

        EmbeddedChannel channel = new EmbeddedChannel(); //카페 차림

        channel.pipeline().addLast(new LoggingHandler(LogLevel.INFO)); //들고 나가는 고객 로그 기록
        //channel.pipeline().addLast(new LineBasedFrameDecoder(80)); // 엔터(\n)키로 단락 분리하고 80byte가 최대 임
        channel.pipeline().addLast(new StringDecoder(CharsetUtil.UTF_8)); // ByteBuf -> 문자열로 변경
        channel.pipeline().addLast(new EchoServerHandler()); // channelRead에 전달

        channel.pipeline().addLast(new StringEncoder(CharsetUtil.UTF_8)); // channelRead에 받은 String을 ByteBuf 로 변경 후 LoggingHandler로 전달

        ByteBuf buf = Unpooled.buffer();
        //buf.writeBytes("안녕 반가워\n".getBytes());
        buf.writeBytes("{\"userId\":\"doha123\", \"password\":\"1234\",\"task\":\"login\"}\n".getBytes());

        channel.writeInbound(buf);  // 손님 들어간다

        ByteBuf readBuff = channel.readOutbound(); // 손님 나온다
        log.info("readBuff={}",((ByteBuf)readBuff).toString(CharsetUtil.UTF_8));

        channel.finish(); // 카페 닫는다
    }
}

class EchoServerHandler extends ChannelInboundHandlerAdapter {
    ObjectMapper objectMapper = new ObjectMapper();
    final static Logger log = LoggerFactory.getLogger(NettyTest.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("channelActive..");
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("Read Data : {}", msg);

        Map<String, Object> data;
        try {
            data = objectMapper.readValue((String) msg, new TypeReference<>() {});
            log.info("json : " + data);
        } catch (JsonParseException | JsonMappingException e) {
            log.error("JSON PARSE ERROR : {}", e);

            return;
        }

        ctx.channel().writeAndFlush(msg);
    }
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("channelInactive..");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    }
}
