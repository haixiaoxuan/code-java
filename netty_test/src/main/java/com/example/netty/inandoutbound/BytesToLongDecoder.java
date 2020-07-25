package com.example.netty.inandoutbound;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * description
 * <p>
 * Created by xiexiaoxuan on 2020/3/26.
 */
public class BytesToLongDecoder extends ByteToMessageDecoder {

    /**
     *  decode 方法会被调用多次，直到byteBuf中没有 可读字节
     * @param ctx
     * @param in 入站的byteBuf
     * @param out list集合将解码后的数据传给下一个handler
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if(in.readableBytes() >= 8 ){
            out.add(in.readLong());
        }
    }
}
