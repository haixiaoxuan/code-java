package com.example.netty.heardbeat;

import com.example.netty.groupchat.ChatServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * description
 * <p>
 * Created by xiexiaoxuan on 2020/3/24.
 */
public class MyServer {

    public static void main(String[] args) throws Exception{

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(8);

        try{

            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new LoggingHandler(LogLevel.INFO)) // 给bossGroup设置日志级别
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();

                            /**
                             * idleStateHandler是netty提供的处理空闲状态的handler
                             * readerIdleTime 表示多长时间没有读数据就发送心跳检测包是否连接
                             * writerIdleTime
                             * allIdleTime 读写空闲
                             * 当 idleStateHandler 触发之后就会传递给管道的下一个handler，调用userEventTrigger方法
                             */
                            pipeline.addLast(new IdleStateHandler(3,
                                    5, 7, TimeUnit.SECONDS));

                            pipeline.addLast(new MyServerHandler());
                        }
                    });

            System.out.println("netty 服务器启动... ");
            ChannelFuture channelFuture = serverBootstrap.bind(55555).sync();
            channelFuture.channel().closeFuture().sync();


        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
