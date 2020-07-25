package com.example.netty.inandoutbound;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * description
 * <p>
 * Created by xiexiaoxuan on 2020/3/26.
 */
public class MyServerHandler extends SimpleChannelInboundHandler<Long> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
        System.out.println("long: " + msg);

        ctx.writeAndFlush(msg);
    }
}
