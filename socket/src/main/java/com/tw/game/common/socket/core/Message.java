package com.tw.game.common.socket.core;

import com.tw.game.common.utils.lang.ByteUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.Getter;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 通信信息对象
 * 
 * <pre>
 * 包：包头[包标识+包长度]+通信信息
 * 包头：是定长的，长度为:{@link ResponseConstants#PACKAGE_LENGTH}
 * 
 * 通信信息：[信息头][信息体][附加内容]
 * 信息头:[长度][格式][状态][序号][会话][模块号][指令]
 * 信息体:[长度][内容]
 * 附加内容:[内容]
 * </pre>
 */
@Getter
public class Message {

	private static final Logger logger = LoggerFactory.getLogger(Message.class);

	/** 信息头 */
	private Header header;
	/** 信息体 */
	private byte[] body = new byte[0];

	/** 从原始包数据构建信息对象实例 */
	public static Message valueOf(byte[] array) {
		try {
			Message result = new Message();
			// 头长度
			int offset = ByteUtils.intFromByte(array);
			result.header = Header.valueOf(array);
			int length = ByteUtils.intFromByte(array, offset);
			result.body = ArrayUtils.subarray(array, offset += 4, offset += length - 4);
			return result;
		} catch (Exception e) {
			String message = "创建通信信息对象异常";
			logger.error(message, e);
			throw new RuntimeException(message, e);
		}
	}

	public static Message valueOf(Header header, byte[] body) {
		Message result = new Message();
		result.header = header;
		if (body != null) {
			result.body = body;
		}
		return result;
	}

	public byte[] toBytes() {
		int msgLen = Header.DEFAULT_HEADER_LENGTH + 4 + (body == null ? 0 : body.length);
		ByteBuf buffer = Unpooled.buffer(msgLen);
		write(buffer);
		byte[] result = new byte[buffer.readableBytes()];
		buffer.readBytes(result);
		return result;
	}

	/**
	 * 将对象写入输出流
	 */
	public void write(ByteBuf out) {
		byte[] header = this.header.toBytes();
		out.writeBytes(header);
		out.writeInt((body == null ? 0 : body.length) + 4);
		if (body != null) {
			out.writeBytes(body);
		}
	}

	public Message clearBody() {
		this.body = new byte[0];
		return this;
	}

	public Message changeToNormalResponse() {
		header.setState(ResponseConstants.STATE_RESPONSE);
		return this;
	}
	public Message changeToErrorResponse(int state) {
		header.setState(ResponseConstants.STATE_ERROR + ResponseConstants.STATE_RESPONSE + state);
		return this;
	}

	public boolean hasState(int checked) {
		return header.hasState(checked);
	}

	public void addState(int added) {
		header.addState(added);
	}

	public void removeState(int removed) {
		header.removeState(removed);
	}

	@Override
	public String toString() {
		return "H:=" + header + " B:" + (body == null ? 0 : body.length) + " A:";
	}

}
