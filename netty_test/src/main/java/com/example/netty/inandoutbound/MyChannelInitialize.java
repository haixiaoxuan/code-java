package com.example.netty.inandoutbound;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;


/**
 * description
 * <p>
 * Created by xiexiaoxuan on 2020/3/26.
 */
public class MyChannelInitialize extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        /**
         * 经过测试，管道处理是有序的
         */
        pipeline.addLast(new BytesToLongDecoder());
        pipeline.addLast(new LongToBytesEncoder());
        pipeline.addLast(new MyServerHandler());
    }
}
