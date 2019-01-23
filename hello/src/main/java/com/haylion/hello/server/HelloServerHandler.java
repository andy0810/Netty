package com.haylion.hello.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

public class HelloServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 收到数据时使用
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //super.channelRead(ctx, msg);
        try {
            ByteBuf in = (ByteBuf) msg;
            System.out.print(in.toString(CharsetUtil.UTF_8));
        }finally {
            //抛弃收到的数据
            ReferenceCountUtil.release(msg);
        }
    }

    /**
     * 当netty由于io错误或者处理器在处理事件时抛出异常时调用
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        // 当出现异常就关闭连接
        ctx.close();
    }
}
