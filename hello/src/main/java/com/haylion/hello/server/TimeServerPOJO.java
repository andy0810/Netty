package com.haylion.hello.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.nio.charset.Charset;

/**
 * 服务器端类
 */
public class TimeServerPOJO {
    private int port;

    public TimeServerPOJO(int port){
        this.port = port;
    }

    public void run() throws Exception{
        EventLoopGroup bossGroup = new NioEventLoopGroup();     // 用来接收进来的连接
        EventLoopGroup workerGroup = new NioEventLoopGroup();   // 用来处理已经被接收的连接
        System.out.println("start........"+port);

        try {
            ServerBootstrap s = new ServerBootstrap();          // 启动NIO服务的辅助启动类
            s.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //自定义处理类
                            //注意添加顺序
                            ch.pipeline().addLast(new TimeEncoderPOJO(),new TimeServerHandlerPOJO());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG,128)
                    .childOption(ChannelOption.SO_KEEPALIVE,true);

            //绑定端口，开始接收进来的连接
            System.out.println("TimeServerPOJO--------bind......1");
            ChannelFuture f = s.bind(port).sync();
            System.out.println("TimeServerPOJO--------bind......2");
            //等待服务器socket关闭
            f.channel().closeFuture().sync();
            System.out.println("TimeServerPOJO--------close......1");
        }catch (Exception e){
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
    public static void main(String[] args)throws Exception{
        int port = 8080;
        System.out.println("main.......");

        Charset utf8 = Charset.forName("UTF-8");
        ByteBuf buf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);

        ByteBuf sliced = buf.slice(0, 14);  // 创建从0开始到14的新slice
        System.out.println(sliced.toString(utf8));  //打印 Netty in Action

        buf.setByte(0, (byte) 'J');                 //更新索引为0的字节
        // 断言成功，说明slice之后两段数据共享
        assert buf.getByte(0) == sliced.getByte(0);
        System.out.println(buf.getByte(0));
        System.out.println(sliced.getByte(0));

        Charset utf81 = Charset.forName("UTF-8");
        ByteBuf buf1 = Unpooled.copiedBuffer("Netty in Action rocks!", utf81);

        ByteBuf copy = buf1.copy(0, 14);     // 注意这里使用了copy
        System.out.println(copy.toString(utf8));

        buf1.setByte(0, (byte) 'J');
    // 断言成功，说明原数据修改对copy不影响
        assert buf1.getByte(0) != copy.getByte(0);
        System.out.println(buf1.getByte(0));
        System.out.println(copy.getByte(0));

        new TimeServerPOJO(port).run();
        System.out.println("stop.........");
    }
}
