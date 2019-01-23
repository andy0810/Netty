package com.haylion.hello.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 自定义服务器数据编码类
 */
public class TimeEncoderPOJO extends MessageToByteEncoder<Time> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Time msg, ByteBuf out) throws Exception {
        out.writeInt((int) msg.value());
        System.out.println("TimeEncoderPOJO------encode");
    }
}
