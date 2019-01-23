package com.haylion.hello.client;

import com.haylion.hello.server.Time;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 客户端数据处理类
 */
public class TimeClientHandlerPOJO extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 直接将信息转换成Time类型输出即可
        Time time = (Time)msg;
        System.out.println("TimeClientHandlerPOJO----time:::::"+time);
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        System.out.println("TimeClientHandlerPOJO-----exceptionCaught");
        ctx.close();
    }
}
