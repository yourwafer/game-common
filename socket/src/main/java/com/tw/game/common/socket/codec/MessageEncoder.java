package com.tw.game.common.socket.codec;

import com.tw.game.common.socket.core.Message;
import com.tw.game.common.socket.core.ResponseConstants;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.tw.game.common.utils.codec.HashUtils.BPHash;

public class MessageEncoder extends MessageToByteEncoder<Message> {

	private static final Logger logger = LoggerFactory.getLogger(MessageEncoder.class);

	@Override
	protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
		encode(msg, out);
	}
	private void encode(Message msg, ByteBuf out) {
		byte[] bytes = msg.toBytes();
		if (logger.isDebugEnabled()) {
			logger.debug("编码:{}", ((Message) msg).toString());
		}
		out.writeInt(ResponseConstants.PACKAGE_INDETIFIER);
		out.writeInt((bytes == null ? 0 : bytes.length) + 4);
		int hashcode;
		if (bytes != null) {
			out.writeBytes(bytes);
			hashcode = (int) BPHash(bytes, bytes.length);
		} else {
			hashcode = 0;
		}
		out.writeInt(hashcode);
	}

}
