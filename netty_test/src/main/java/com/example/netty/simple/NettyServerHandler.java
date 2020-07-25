package com.example.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * description
 * <p>
 * Created by xiexiaoxuan on 2020/3/22.
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {


    /**
     * 读取客户端发送过来的消息
     * 1. ctx 上下文对象，含有管道、通道、客户端地址
     * 2. msg 客户端发送过来的消息
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        /**
         * 如果此处的任务比较耗时，可以将任务提交到task_queue中
         *  1. 用户自定义普通任务，如果有多个任务会串行执行，task_queue由一个线程执行
         */
        ctx.channel().eventLoop().execute(new Runnable() {
            public void run() {
                System.out.println("耗时操作... ");
            }
        });

        // 2. 用户自定义定时任务，将任务提交到 schedule_task_queue
        ctx.channel().eventLoop().schedule(new Runnable() {
            public void run() {
                System.out.println(" ..... ");
            }
        }, 5, TimeUnit.SECONDS);




        System.out.println("server ctx " + ctx);
        // 此处是netty提供的Buf，非NIO
        ByteBuf buf = (ByteBuf) msg;

        System.out.println("msg: " + buf.toString(CharsetUtil.UTF_8));
        System.out.println("client addr: " + ctx.channel().remoteAddress());
    }


    // 数据读取完毕
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

        // 将数据写入缓存并刷新
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello 客户端", CharsetUtil.UTF_8));
    }


    // 发生异常之后的处理
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
