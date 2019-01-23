package com.haylion.hello.server;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 自定义服务器的业务逻辑类
 * 服务器解码器
 * 连接建立时发送当前时间
 */
public class TimeServerHandlerPOJO extends ChannelInboundHandlerAdapter {
    /**
     * 连接建立的时候并且准备进行通信时被调用
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 发送当前时间信息
        ChannelFuture f = ctx.writeAndFlush(new Time());

        System.out.println("TimeServerHandlerPOJO....channelActive----"+f);
        // 发送完毕之后关闭 Channel
        f.addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
      //  super.exceptionCaught(ctx, cause);
        System.out.println("TimeServerHandlerPOJO....exceptionCaught----");
        ctx.close();
    }
}
