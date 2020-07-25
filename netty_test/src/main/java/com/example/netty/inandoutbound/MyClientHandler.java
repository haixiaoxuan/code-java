package com.example.netty.inandoutbound;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * description
 * <p>
 * Created by xiexiaoxuan on 2020/3/26.
 */
public class MyClientHandler extends SimpleChannelInboundHandler<Long> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
        System.out.println(msg);
    }


    /**
     * 发送消息给服务端
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(1000L);

        /**
         * 如果发送的类型不是encoder指定的类型，则不会经过编码
         */
//        ctx.writeAndFlush(Unpooled.copiedBuffer("abcdabcd", CharsetUtil.UTF_8));
    }
}
