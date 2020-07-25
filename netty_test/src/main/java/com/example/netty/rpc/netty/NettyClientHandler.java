package com.example.netty.rpc.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

/**
 * description
 * <p>
 * Created by xiexiaoxuan on 2020/3/28.
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable {


    private ChannelHandlerContext ctx;
    private String result;
    private String param;


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
    }

    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        result = msg.toString();
        System.out.println("服务端返回消息："+ result);

        notify(); // 唤醒等待线程
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    @Override
    public synchronized Object call() throws Exception {
        /**
         *  该方法被代理对象调用，发送数据给服务器 -> wait -> 等待被唤醒 -> 继续执行
         */
        ctx.writeAndFlush(param);
        wait(); // 等待被唤醒
        return result;
    }


    void setParam(String param){
        this.param = param;
    }
}
