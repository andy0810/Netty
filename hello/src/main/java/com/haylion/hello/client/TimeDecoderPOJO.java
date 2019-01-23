package com.haylion.hello.client;

import com.haylion.hello.server.Time;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 客户端数据解码类
 */
public class TimeDecoderPOJO extends ByteToMessageDecoder {
    /**
     * 有新数据接收时调用
     * 为防止分包现象，先将数据存入内部缓存，到达满足条件之后再进行解码
     * @param ctx
     * @param in
     * @param out
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 4 ){
            return;
        }
        //out添加对象则表示解码成功
        System.out.println("TimeDecoderPOJO----------decode......");
        out.add(new Time(in.readUnsignedInt()));
    }
}
