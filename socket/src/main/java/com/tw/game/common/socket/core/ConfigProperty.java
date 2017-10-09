package com.tw.game.common.socket.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConfigProperty {

    private String bindAddress="127.0.0.1";

    private int port = 11111;

    private int readBuffer = 2048;

    private int writeBuffer = 2048;

    private int readTimeout = 2000;

    private int backlog = 128;

    private int connectThread = 1;

    private int workThread = Runtime.getRuntime().availableProcessors()/2;

    private int poolMin = 1;

    private int poolMax = Runtime.getRuntime().availableProcessors();

    private int poolIdle = Runtime.getRuntime().availableProcessors()/2;


}
