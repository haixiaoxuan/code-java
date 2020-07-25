package com.example.netty.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.time.LocalTime;

/**
 * description
 * <p>
 * Created by xiexiaoxuan on 2020/3/24.
 */
public class MyTextWebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {

        System.out.println(msg.text());
        ctx.channel().writeAndFlush(new TextWebSocketFrame("服务器时间 " + LocalTime.now() + " " + msg.text()));
    }


    // 当客户端连接时触发
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        // asLongText 是唯一， asShortText 非唯一
        System.out.println("handler add " + ctx.channel().id().asLongText());
    }


    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handler remove " + ctx.channel().id().asLongText());
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
