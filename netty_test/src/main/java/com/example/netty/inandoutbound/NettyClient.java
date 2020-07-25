package com.example.netty.inandoutbound;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * description
 * <p>
 * Created by xiexiaoxuan on 2020/3/22.
 */
public class NettyClient {

    public static void main(String[] args) throws InterruptedException {

        EventLoopGroup eventExecutors = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventExecutors)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new LongToBytesEncoder());


                            pipeline.addLast(new BytesToLongDecoder());
                            pipeline.addLast(new MyClientHandler());
                        }
                    });

            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 6666);
            channelFuture.channel().closeFuture().sync();
        }finally {
            eventExecutors.shutdownGracefully();
        }


    }
}
