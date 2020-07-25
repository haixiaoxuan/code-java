package com.example.netty.rpc.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * description
 * <p>
 * Created by xiexiaoxuan on 2020/3/28.
 */
public class NettyClient {


    static ExecutorService executor = Executors.newFixedThreadPool(3);
    static NettyClientHandler clientHandler = new NettyClientHandler();


    public void init() throws Exception{

        EventLoopGroup eventExecutors = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventExecutors)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new StringEncoder());
                            pipeline.addLast(clientHandler);
                        }
                    });

            bootstrap.connect("127.0.0.1", 6666).sync();
//            channelFuture.channel().closeFuture().sync();
        }finally {
//            eventExecutors.shutdownGracefully();
        }
    }


    /**
     * 使用代理模式获取一个代理对象
     * @param serviceClass
     * @param providerName
     * @return
     */
    public Object getBean(final Class<?> serviceClass, final String providerName){
        return Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                new Class<?>[]{serviceClass},
                (proxy, method, args) -> {
                    if(clientHandler != null){
                        init();
                    }

                    clientHandler.setParam(providerName + args[0]);
                    return executor.submit(clientHandler).get();
                });
    }
}
