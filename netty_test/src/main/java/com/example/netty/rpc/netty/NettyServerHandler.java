package com.example.netty.rpc.netty;

import com.example.netty.rpc.provider.SayHelloImpl;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * description
 * <p>
 * Created by xiexiaoxuan on 2020/3/28.
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        /**
         * 定义协议，client 发送 SayHello#say#msg时 发起rpc调用
         */
        String content = (String) msg;
        System.out.println("客户端发送消息：" + content);
        if(content.startsWith("SayHello#say")){
            String result = new SayHelloImpl().say(content.substring(content.lastIndexOf("#") + 1));
            ctx.writeAndFlush(result);
        }

    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }






}
