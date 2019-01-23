package com.haylion.hello.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class TimeClientPOJO{
    public static void main(String[] args) throws Exception{
        String host = "127.0.0.1"; //ip
        int port = 8080;           //端口
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();  //与serverBootstrap类似
            b.group(workerGroup); //客户端不需要boss worker
            b.channel(NioSocketChannel.class);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    //POJO
                    System.out.println("TimeClientPOJO-----------initChannel...1");
                    ch.pipeline().addLast(new TimeDecoderPOJO(),new TimeClientHandlerPOJO());
                    System.out.println("TimeClientPOJO-----------initChannel...2");
                }
            });
            b.option(ChannelOption.SO_KEEPALIVE,true);  //客户端的socketchannel没有父亲

            System.out.println("TimeClientPOJO-----------connect....1");
            //启动客户端 客户端用connect连接
            ChannelFuture f = b.connect(host,port).sync();
            System.out.println("TimeClientPOJO-----------connect....2");
            //等待连接关闭
            f.channel().closeFuture().sync();
            System.out.println("TimeClientPOJO-----------close....");
        }catch (Exception e){
            workerGroup.shutdownGracefully();
        }
    }
}
