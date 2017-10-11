package com.tw.game.common.socket.server;

import com.tw.game.common.socket.config.core.ConfigProperty;
import com.tw.game.common.socket.config.core.ConfigUtils;
import com.tw.game.common.utils.thread.NamedThreadFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.EventExecutorGroup;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
public class ServerSocket {

    private ConfigProperty config;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workGroup;
    private List<ChannelHandler> childHandlers;
    private EventExecutorGroup eventExecutors;

    public ServerSocket(ConfigProperty config) {
        this.config = config;
    }

    public ServerSocket initial() {
        initialEventLoop();
        initialServerBootstrap();
        return this;
    }

    private void initialServerBootstrap() {
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        serverBootstrap.group(bossGroup, workGroup);

        Map<ChannelOption<Object>, Object> channelOptionMap = ConfigUtils.buildServerSessionConfig(config);
        channelOptionMap.forEach((type, value) -> {
            serverBootstrap.option(type, value);
        });
        serverBootstrap.channel(NioServerSocketChannel.class);

        eventExecutors = ConfigUtils.buildExecutorGroup(config.getExecutorThread(), "服务器", "执行线程");
    }

    private void initialEventLoop() {
        int bossConnectThread = config.getConnectThread();
        if (bossConnectThread <= 0) {
            log.warn("BOSS线程数量设置异常[{}]", bossConnectThread);
        }
        bossGroup = eventLoopGroup(bossConnectThread, "BOSS网络线程");
        int workThread = config.getWorkThread();
        workGroup = eventLoopGroup(workThread, "socket-work");
        log.debug("Boss EventLoop数量[{}],Work EventLoop数量[{}]", bossConnectThread, workThread);
    }

    protected EventLoopGroup eventLoopGroup(int thread, String name) {
        EventLoopGroup loopGroup = new NioEventLoopGroup(thread, new NamedThreadFactory(name));
        return loopGroup;
    }
}
