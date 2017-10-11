package com.tw.game.common.socket.config.core;

import io.netty.channel.ChannelOption;

import java.util.HashMap;
import java.util.Map;

public class SessionConfig {

    private Integer readBuffSize;
    private Integer writeBuffSize;
    private Integer timeout;
    private Integer backlog;

    public static SessionConfig of(int readBufferSize, int writeBuffSize, int timeout, int backlog) {
        SessionConfig result = new SessionConfig();
        result.readBuffSize = readBufferSize;
        result.writeBuffSize = writeBuffSize;
        result.timeout = timeout;
        result.backlog = backlog;
        return result;
    }

    public static SessionConfig of(int readBufferSize, int writeBuffSize, int timeout) {
        return of(readBufferSize, writeBuffSize, timeout, -1);
    }

    public Map<ChannelOption<Object>, Object> buildClient() {
        Map values = new HashMap();
        values.put(ChannelOption.SO_RCVBUF, readBuffSize);
        values.put(ChannelOption.SO_SNDBUF, writeBuffSize);
        if (timeout > 0) {
            values.put(ChannelOption.SO_TIMEOUT, timeout);
        }

        boolean TCP_NODELAY = true;
        values.put(ChannelOption.TCP_NODELAY, TCP_NODELAY);
        boolean ALLOW_HALF_CLOSURE = false;
        values.put(ChannelOption.ALLOW_HALF_CLOSURE, ALLOW_HALF_CLOSURE);
        return values;
    }

    public Map<ChannelOption<Object>, Object> buildServer() {
        Map values = buildClient();
        values.put(ChannelOption.SO_BACKLOG, backlog);

        boolean REUSE_ADDR = true;
        values.put(ChannelOption.SO_REUSEADDR, REUSE_ADDR);
        return values;
    }
}
