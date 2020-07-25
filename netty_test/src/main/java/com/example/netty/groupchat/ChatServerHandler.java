package com.example.netty.groupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;

/**
 * description
 * <p>
 * Created by xiexiaoxuan on 2020/3/24.
 */
public class ChatServerHandler extends SimpleChannelInboundHandler<String> {


    private static ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    /**
     * 一旦连接建立，此方法首先被执行
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();

        // 该方法会遍历所有channel
        group.writeAndFlush("[客户端] " + channel.remoteAddress() + " 上线了... ");
        group.add(channel);
    }


    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        group.writeAndFlush("[客户端] " + channel.remoteAddress() + " 下线... ");
    }


    // 表示channel处于活动状态
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " 加入聊天 ");
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " 退出聊天 ");
    }


    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        final Channel channel = ctx.channel();
        group.forEach(ele -> {
            if(channel != ele){
                ele.writeAndFlush("[客户端]" + channel.remoteAddress() + ": " + msg);
            }else{
                ele.writeAndFlush("[本人]: " +  msg);
            }
        });
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
