package com.tw.game.common.socket.exception;

/**
 * 消息序号参数异常
 * @author Ramon
 */
public class MessageSnException extends SocketException {

	private static final long serialVersionUID = -2608772970436403289L;

	public MessageSnException() {
		super();
	}

	public MessageSnException(String message, Throwable cause) {
		super(message, cause);
	}

	public MessageSnException(String message) {
		super(message);
	}

	public MessageSnException(Throwable cause) {
		super(cause);
	}
}
