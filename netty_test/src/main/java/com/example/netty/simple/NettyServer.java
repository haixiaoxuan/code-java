package com.example.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * description
 * <p>
 * Created by xiexiaoxuan on 2020/3/22.
 */
public class NettyServer {

    public static void main(String[] args) throws InterruptedException {


        /**
         * boss 只处理连接请求，业务处理交给worker
         * 这两个都是无限循环
         * bossGroup workerGroup 含有的子线程(NioEventLoop)数 默认为 cpu_core * 2
         */
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();


        try {
            // 服务端的启动对象
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)  // 设置服务器通道实现
                    .option(ChannelOption.SO_BACKLOG, 128)  // 设置线程队列得到连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true)  // 设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() { // 创建一个通道测试对象
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new NettyServerHandler());
                        }
                    }); // 给workerGroup的eventLoop设置对应的处理器
            /*
             * 与 childHandler 相对应的有一个handler方法，前者给workerGroup添加handler,后者给bossGroup
             */


            // 绑定一个端口并设置同步
            ChannelFuture channelFuture = serverBootstrap.bind(6666).sync();

            // 对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();

        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
