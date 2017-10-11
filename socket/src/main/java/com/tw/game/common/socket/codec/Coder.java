package com.tw.game.common.socket.codec;

public interface Coder {

	byte[] encode(Object obj, Object type);

	Object decode(byte[] bytes, Object type);
}
