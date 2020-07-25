package com.example.netty.tcp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * description
 * <p>
 * Created by xiexiaoxuan on 2020/3/27.
 */
public class MyClientHandler extends SimpleChannelInboundHandler<MyMessage> {


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for(int i = 0; i < 10; i++)
        {
            String content = "锄禾日当午" + i;

            System.out.println("client send message... ");
            ctx.writeAndFlush(new MyMessage(content.getBytes().length, content.getBytes()));

        }

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MyMessage msg) throws Exception {

    }
}
