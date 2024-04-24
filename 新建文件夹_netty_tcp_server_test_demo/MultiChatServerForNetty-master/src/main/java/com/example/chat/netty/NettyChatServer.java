package com.example.chat.netty;

import com.example.chat.netty.handler.NettyChatServerHandler;
import com.example.chat.properties.NettyProperties;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class NettyChatServer {
    @Autowired
    private NettyProperties nettyProperties;
    @Autowired
    private NettyChatServerHandler nettyChatServerHandler;

    public void run() {
        try {
            ServerBootstrap bs = new ServerBootstrap();
            bs.group(getBossGroup(), getWorkerGroup())
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    //.handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new LoggingHandler(LogLevel.INFO));
                            p.addLast(new StringDecoder(CharsetUtil.UTF_8));
                            p.addLast(new StringEncoder(CharsetUtil.UTF_8));
                            p.addLast(nettyChatServerHandler);
                        }
                    });
            ChannelFuture f = bs.bind(nettyProperties.getPort()).sync();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Bean(destroyMethod = "shutdownGracefully")
    private NioEventLoopGroup getBossGroup(){
        return new NioEventLoopGroup(nettyProperties.getCount().getBoss());
    }

    @Bean(destroyMethod = "shutdownGracefully")
    private NioEventLoopGroup getWorkerGroup(){
        return new NioEventLoopGroup();
    }
}
