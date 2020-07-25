package com.example.netty.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * description
 * <p>
 * Created by xiexiaoxuan on 2020/3/27.
 */
public class MyMessageDecoder extends ReplayingDecoder<MyMessage> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        System.out.println("server decoder ... ");
        int length = in.readInt();
        byte[] content = new byte[length];
        in.readBytes(content);
        out.add(new MyMessage(length, content));
    }
}
