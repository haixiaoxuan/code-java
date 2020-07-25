package com.example.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * description
 * <p>
 * Created by xiexiaoxuan on 2020/3/23.
 */
public class ChannelInitialHandler extends ChannelInitializer<SocketChannel>{


    protected void initChannel(SocketChannel socketChannel) throws Exception {
        /**
         * HttpServerCodec 是netty提供的http编解码器
         */
        socketChannel.pipeline().addLast("MyHttpCodec", new HttpServerCodec());
        socketChannel.pipeline().addLast("MyHandler", new MyHttpServerHandler());
    }
}
