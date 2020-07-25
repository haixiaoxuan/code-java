package com.example.netty.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * description
 * <p>
 * Created by xiexiaoxuan on 2020/3/27.
 */
public class MyMessageEncoder extends MessageToByteEncoder<MyMessage> {
    @Override
    protected void encode(ChannelHandlerContext ctx, MyMessage msg, ByteBuf out) throws Exception {
//        ctx.writeAndFlush(msg.getLength());
//        ctx.writeAndFlush(msg.getContent());

        System.out.println("client encoder ... ");
        out.writeInt(msg.getLength());
        out.writeBytes(msg.getContent());
    }
}
