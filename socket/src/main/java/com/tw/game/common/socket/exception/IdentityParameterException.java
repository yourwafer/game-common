package com.tw.game.common.socket.exception;

/**
 * 会话身份参数异常
 * @author frank
 */
public class IdentityParameterException extends ParameterException {

	private static final long serialVersionUID = -556367660544784073L;

	public IdentityParameterException() {
		super();
	}

	public IdentityParameterException(String message, Throwable cause) {
		super(message, cause);
	}

	public IdentityParameterException(String message) {
		super(message);
	}

	public IdentityParameterException(Throwable cause) {
		super(cause);
	}
}
