package com.example.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

/**
 * description
 * <p>
 * Created by xiexiaoxuan on 2020/3/23.
 */
public class MyHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {


    // 当有读取事件发生时会触发
    // HttpObject 是客户端与服务端通信的数据类型
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpObject httpObject) throws Exception {

        if(httpObject instanceof HttpRequest){

            System.out.println("客户端地址： " + channelHandlerContext.channel().remoteAddress());


            // 过滤特定的请求
            HttpRequest request = (HttpRequest) httpObject;
            URI uri = new URI(request.uri());
            if("/favicon.ico".equals(uri.getPath())){
                System.out.println("不做处理....");
                return;
            }


            // 回复信息给浏览器
            ByteBuf buffer = Unpooled.copiedBuffer("hello client", CharsetUtil.UTF_8);
            DefaultFullHttpResponse response =
                    new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buffer);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, buffer.readableBytes());

            channelHandlerContext.writeAndFlush(response);
        }
    }
}
