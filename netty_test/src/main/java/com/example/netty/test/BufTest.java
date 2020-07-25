package com.example.netty.test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * description
 * <p>
 * Created by xiexiaoxuan on 2020/3/23.
 */
public class BufTest {

    public static void main(String[] args){


        ByteBuf buf = Unpooled.buffer(10);

        for(int i = 0 ; i < 10; i++){
            buf.writeByte(i);
        }

        // 与NIO不同，不需要 flip
        for(int i = 0; i < buf.capacity(); i++){
//            System.out.println(buf.readByte());
            System.out.println(buf.getByte(i));
        }

        // 获取可读取的字节数
        System.out.println(buf.readableBytes());

    }
}
