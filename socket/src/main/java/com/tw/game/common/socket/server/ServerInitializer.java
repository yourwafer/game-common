package com.tw.game.common.socket.server;

import com.eyu.common.socket.codec.MessageCodecFactory;
import com.eyu.common.socket.filter.session.NettySessionManager;
import com.eyu.common.socket.filter.session.SessionManagerFilter;
import com.eyu.common.socket.handler.NettyHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.concurrent.EventExecutorGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Map.Entry;

/**
 * CHANNEL 初始化
 * @author Ramon
 */
public class ServerInitializer extends ChannelInitializer<SocketChannel> {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private ChannelHandler handler;
	private Map<String, ChannelHandler> filters;
	private EventExecutorGroup executor;

	private MessageCodecFactory codecFactory = new MessageCodecFactory();
	private SessionManagerFilter defaultSessionManager = new SessionManagerFilter();

	ServerInitializer(NettyHandler handler, EventExecutorGroup executor, NettySessionManager sessionManager,
                    Map<String, ChannelHandler> filters) {
		this.handler = handler;
		this.executor = executor;
		this.defaultSessionManager.setSessionManager(sessionManager);
		this.filters = filters;
	}

	@Override
	public void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();

		SessionManagerFilter sessionManagerFilter = null;
		// 过滤器
		if (filters != null) {
			for (Entry<String, ChannelHandler> e : filters.entrySet()) {
				String key = e.getKey();
				ChannelHandler value = e.getValue();
				try {
					if (pipeline.get(key) != null) {
						continue;
					}
					if (value instanceof SessionManagerFilter) {
						sessionManagerFilter = (SessionManagerFilter) value;
					} else {
						pipeline.addLast(key, value);
					}
					if (logger.isDebugEnabled()) {
						logger.debug("连接[{}]添加过滤器[{}]", ch.remoteAddress(), key);
					}
				} catch (Exception ex) {
					logger.error("连接[{}]添加过滤器异常!", ch.remoteAddress(), ex);
					throw ex;
				}

			}
		}

		// 编码解码器
		pipeline.addLast("DECODER", codecFactory.getDecoder(Short.MAX_VALUE));
		pipeline.addLast("ENCODER", codecFactory.getEncoder());
		// SESSION MANAGER
		if (sessionManagerFilter == null) {
			pipeline.addLast("session", defaultSessionManager);
		} else {
			pipeline.addLast("session", sessionManagerFilter);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("连接[{}]添加过滤器[{}]", ch.remoteAddress(), "session");
		}

		// 处理器
		pipeline.addLast(executor, "HANDLER", handler);
	}
}
