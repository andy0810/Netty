package com.haylion.chat.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * 客户端
 * 开启客户端，接收控制台输入并发送给服务器
 */
public class SimpleChatClient {
    private final String host;
    private final int port;

    public SimpleChatClient(String host,int port){
        this.host = host;
        this.port = port;
    }

    //配置并运行客户端
    public void run(){
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();  //客户端辅助启动类
            b.group(workerGroup)                //客户端只需要一个用来接收并处理连接
                    .channel(NioSocketChannel.class)  //设置如何接受连接
                    .handler(new SimpleChatClientInitializer()); //配置channel

            //连接服务器
            Channel channel = b.connect(host,port).sync().channel();
            //读取控制台输入字符
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            while (true){
                // 每行成一帧输出，以"\r\n"结尾
                channel.writeAndFlush(in.readLine() + "\r\n");
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            workerGroup.shutdownGracefully();
        }
    }
    public static void main(String[] args) throws Exception {
        new SimpleChatClient("localhost",8000).run();
    }
}
