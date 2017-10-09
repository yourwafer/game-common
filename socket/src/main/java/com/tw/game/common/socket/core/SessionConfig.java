package com.tw.game.common.socket.core;

import io.netty.channel.ChannelOption;

import java.util.HashMap;
import java.util.Map;

public class SessionConfig {

    public static final boolean TCP_NODELAY = true;
    public static final boolean REUSE_ADDR = true;
    public static final boolean ALLOW_HALF_CLOSURE = false;

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

    public Map<ChannelOption<?>, ? extends Object> buildClient() {
        Map<ChannelOption<?>, Object> values = new HashMap<>();
        values.put(ChannelOption.SO_RCVBUF, readBuffSize);
        values.put(ChannelOption.SO_SNDBUF, writeBuffSize);
        if (timeout > 0) {
            values.put(ChannelOption.SO_TIMEOUT, timeout);
        }

        values.put(ChannelOption.TCP_NODELAY, TCP_NODELAY);
        values.put(ChannelOption.ALLOW_HALF_CLOSURE, ALLOW_HALF_CLOSURE);
        return values;
    }

    public Map<ChannelOption<?>, ?> buildServer() {
        Map<ChannelOption<?>, Object> values = (Map<ChannelOption<?>, Object>) buildClient();
        values.put(ChannelOption.SO_BACKLOG, backlog);

        values.put(ChannelOption.SO_REUSEADDR, REUSE_ADDR);
        return values;
    }
}
