package com.tw.game.common.socket.codec;

import com.tw.game.common.socket.core.Message;
import com.tw.game.common.socket.core.ResponseConstants;
import com.tw.game.common.socket.exception.DecodeException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

import java.net.SocketAddress;

import static com.tw.game.common.socket.core.ResponseConstants.PACKAGE_INDETIFIER;
import static com.tw.game.common.utils.codec.HashUtils.BPHash;

public class MessageDecoder extends LengthFieldBasedFrameDecoder {

	private static final Logger logger = LoggerFactory.getLogger(MessageDecoder.class);

	public MessageDecoder(int maxLen) {
		super(maxLen, 4, 4, 0, 0);
	}

	@Override
	protected ByteBuf extractFrame(ChannelHandlerContext ctx, ByteBuf buffer, int index, int length) {
		if (logger.isDebugEnabled()) {
			Channel channel = ctx.channel();
			SocketAddress remoteAddress = channel.remoteAddress();
			logger.debug("连接[{}]已解码切片BUFF[{}:{}]", remoteAddress, index, length);
		}
		return buffer.slice(index, length);
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		if (in.readableBytes() == 0) {
			// BUFF为空
			return null;
		}
		Channel channel = ctx.channel();
		SocketAddress remoteAddress = channel.remoteAddress();
		if (logger.isDebugEnabled()) {
			logger.debug("连接[{}]准备解码BUFF[{}:{}]", remoteAddress, in.readerIndex(), in.readableBytes());
		}
		ByteBuf frame = (ByteBuf) super.decode(ctx, in);
		if (frame == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("忽略连接[{}]的无效数据包 - 数据包不完整", remoteAddress);
			}
			return null;
		}
		try {
			Message message = decode(frame);
			if (logger.isDebugEnabled()) {
				if (message.hasState(ResponseConstants.STATE_COMPRESS)) {
					logger.debug("解码数据,会话:[{}] 头信息:[{}] 信息体:BYTES[{}]", remoteAddress, message.toString(),
						message.getBody().length);
				} else {
					if (message.getBody().length > 100) {
						logger.debug("解码数据,会话:[{}] 头信息:[{}] 信息体:BYTES[{}]", remoteAddress, message.toString(),
							message.getBody().length);
					} else {
						logger.debug("解码数据,会话:[{}] 头信息:[{}] 信息体:[{}]", remoteAddress, message.toString(),
							new String(message.getBody()));
					}
				}
			}
			return message;
		} catch (Exception ex) {
			if (logger.isWarnEnabled()) {
				logger.warn("连接[{}]解码错误", remoteAddress, ex);
			}
			// 解码错误, 断开连接
			ctx.close();
			logger.error("断开连接[{}], 解码错误{} - {}", remoteAddress, ex.getClass(), ex.getMessage());
			// 抛出解码错误
			throw new DecodeException(ex);
		}
	}

	private Message decode(ByteBuf in) {
		int header = in.readInt();
		if (header != PACKAGE_INDETIFIER) {
			// 包头不匹配
			String message = MessageFormatter.format("非法数据包 - 标识[{}]不匹配", header).getMessage();
			throw new DecodeException(message);
		}
		int length = in.readInt();
		// 真正包数据长度减去校验码4Byte
		length -= 4;
		// 提前当前包的数据
		byte[] data = new byte[length];
		in.readBytes(data);
		// 校验码比较
		int checksum = in.readInt();
		int hashcode = (int) BPHash(data, data.length);
		if (checksum != hashcode) {
			FormattingTuple msg = MessageFormatter.format("校验码[{}]错误需要[{}]", checksum, hashcode);
			if (logger.isInfoEnabled()) {
				logger.info(msg.getMessage());
			}
			throw new DecodeException(msg.getMessage());
		}
		// 将数据转为消息对象
		return Message.valueOf(data);

	}

}
