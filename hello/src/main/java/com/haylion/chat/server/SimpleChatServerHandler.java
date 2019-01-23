package com.haylion.chat.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * 服务端处理器
 */
public class SimpleChatServerHandler extends SimpleChannelInboundHandler<String> {
    public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    /**
     * 接收到新的客户端连接时调用
     * 将客户端channel存入列表，并广播消息
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        //广播加入消息
        channels.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + " 加入\n");
        channels.add(incoming);
    }

    /**
     * 客户端连接断开时调用
     * 广播消息
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        //广播离开消息
        channels.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + " 离开\n");
        //channel会自动从ChannelGroup中删除
    }

    /**
     * 收到消息时调用
     * 将消息转发给其他客户端
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel incoming = ctx.channel();
        for (Channel channel : channels){   //遍历所有连接的客户端
            if (channel != incoming){       //其他客户端
                channel.writeAndFlush("["+incoming.remoteAddress()+"] " + msg + "\n");
            }else {                         // 自己
                channel.writeAndFlush("[you] "+ msg + "\n");
            }
        }
    }

    /**
     * 监听到客户端活动时调用
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        System.out.println("SimpleChatClient: " + incoming.remoteAddress() + " 在线");
    }

    /**
     * 监听到客户端不活动时调用
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        System.out.println("SimpleChatClient: " + incoming.remoteAddress() + " 掉线");
    }

    /**
     * 当Netty由于IO错误或者处理器在处理事件抛出异常时调用
     * 关闭连接
     * @param ctx
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel incoming = ctx.channel();
        System.out.println("SimpleChatClient: " + incoming.remoteAddress() + " 异常");
    }
}
