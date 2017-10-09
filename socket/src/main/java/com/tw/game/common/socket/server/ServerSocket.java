package com.tw.game.common.socket.server;

import com.tw.game.common.socket.core.ConfigProperty;
import com.tw.game.common.utils.thread.NamedThreadFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

public class ServerSocket {

    private ConfigProperty config;

    public ServerSocket(ConfigProperty config) {
        this.config = config;
    }

    public ServerSocket initial() {
        EventLoopGroup bossGroup = eventLoopGroup(config.getConnectThread(), "socker-boss");
        EventLoopGroup workGroup = eventLoopGroup(config.getWorkThread(), "socket-work");

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        return this;
    }

    protected EventLoopGroup eventLoopGroup(int thread, String name) {
        EventLoopGroup loopGroup = new NioEventLoopGroup(thread, new NamedThreadFactory(name));
        return loopGroup;
    }
}
