package com.haylion.chat.client;

import com.haylion.chat.server.SimpleChatServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * 客户端配置初始化
 * 与服务器端类似
 */
public class SimpleChatClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //添加处理类
        //使用'\r' '\n' 分隔符
        pipeline.addLast("framer",new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        //解码器
        pipeline.addLast("decoder",new StringDecoder());
        //编码器
        pipeline.addLast("encoder",new StringEncoder());
        //处理器
        pipeline.addLast("handler",new SimpleChatClientHandler());
    }
}
