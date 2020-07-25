package com.example.netty.tcp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * description
 * <p>
 * Created by xiexiaoxuan on 2020/3/27.
 */
public class MyServerHandler extends SimpleChannelInboundHandler<MyMessage> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MyMessage msg) throws Exception {
        System.out.println("server receive message ... ");
        System.out.println(msg);
    }
}
