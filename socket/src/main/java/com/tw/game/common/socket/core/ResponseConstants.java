package com.tw.game.common.socket.core;

/**
 * 回应状态
 * @author frank
 */
public interface ResponseConstants {
	// 包定义部分

	/** 包标识 */
	int PACKAGE_INDETIFIER = 0xFFFFFFFF;
	/** 包长度 */
	int PACKAGE_LENGTH = 8;

	// 信息状态部分

	/** 状态:正常(请求状态) */
	int STATE_NORMAL = 0;

	/** 状态:回应(不是回应就是请求) */
	int STATE_RESPONSE = 1;

	/** 压缩标记位(没有该状态代表未经压缩) */
	int STATE_COMPRESS = 1 << 1;

	/** 转发标记位 */
	int STATE_FORWARD = 1 << 2;

	/** 附加信息标记位(存在就标识除<code>Body</code>还有附加信息<code>Attachment</code>) */
	int STATE_ATTACHMENT = 1 << 3;

	/** 原生信息标记位(有该状态代表信息体为原生类型，即不进行编解码) */
	int STATE_RAW = 1 << 4;

	/** 错误标记位(没有该状态代表正常) */
	int STATE_ERROR = 1 << 16;

	/** 用于推送的消息序号 */
	long DEFAULT_SN = -1;

	/** 请求指令不存在 */
	int COMMAND_NOT_FOUND = 1 << 17;

	/** 解码异常 */
	int DECODE_EXCEPTION = 1 << 18;

	/** 编码异常 */
	int ENCODE_EXCEPTION = 1 << 19;

	/** 参数异常 */
	int PARAMETER_EXCEPTION = 1 << 20;

	/** 处理异常 */
	int PROCESSING_EXCEPTION = 1 << 21;

	/** 会话身份异常 */
	int IDENTITY_EXCEPTION = 1 << 22;

	/** 消息SN异常 */
	int MSG_SN_EXCEPTION = 1 << 23;

	/** 消息业务异常 - 返回错误码 */
	int MSG_LOGIC_EXCEPTION = 1 << 24;

	/** 未知异常 */
	int UNKNOWN_EXCEPTION = 1 << 25;

}
