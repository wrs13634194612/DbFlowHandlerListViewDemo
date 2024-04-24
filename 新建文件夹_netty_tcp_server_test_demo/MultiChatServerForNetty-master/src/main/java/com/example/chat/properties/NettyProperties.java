package com.example.chat.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "netty.server")
@Data
public class NettyProperties {
    private int port;
    private Count count = new Count();

    @Data
    public static class Count{
        private int boss;
        private int worker;
    }

}
