package com.example.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;


/**
 * description 改造http，实现webSocket长连接
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

                            // 因为基于http，所以要用到http编解码
                            pipeline.addLast(new HttpServerCodec());
                            // 是以块方式写
                            pipeline.addLast(new ChunkedWriteHandler());

                            /**
                             * http在数据传输过程中是分段的，此handler就是为了聚合
                             * 这就是为什么浏览器在传输大量数据的时候需要发送多次请求
                             */
                            pipeline.addLast(new HttpObjectAggregator(8192));

                            /**
                             * 对应webSocket，他的数据以 帧的形式传递
                             * WebSocketFrame 有六个子类
                             * 浏览器请求时，ws://localhost:55555/hello
                             * WebSocketServerProtocolHandler 核心功能将http协议升级为 ws协议，保持长连接
                             * 是通过状态码 101
                             */
                            pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));


                            // 业务逻辑
                            pipeline.addLast(new MyTextWebSocketHandler());

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
