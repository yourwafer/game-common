package com.tw.game.common.socket.config.core;

import com.tw.game.common.utils.thread.NamedThreadFactory;
import io.netty.channel.ChannelOption;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

import java.util.Map;

public class ConfigUtils {
    public static Map<ChannelOption<Object>, Object> buildServerSessionConfig(ConfigProperty configProperty){
        SessionConfig sessionConfig = SessionConfig.of(configProperty.getReadBuffer(),
                configProperty.getWriteBuffer(),
                configProperty.getReadTimeout(),
                configProperty.getBacklog());
        return sessionConfig.buildServer();
    }

    public static EventExecutorGroup buildExecutorGroup(int thread, String groupName, String threadName){
        ThreadGroup group = new ThreadGroup(groupName);
        NamedThreadFactory threadFactory = new NamedThreadFactory(group, threadName);
        EventExecutorGroup executor = new DefaultEventExecutorGroup(thread, threadFactory);
        return executor;
    }
}
