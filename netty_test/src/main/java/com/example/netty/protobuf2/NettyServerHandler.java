package com.example.netty.protobuf2;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;


/**
 * description
 * <p>
 * Created by xiexiaoxuan on 2020/3/22.
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        MyDataInfo.MyMessage m = (MyDataInfo.MyMessage) msg;

        if(m.getDataType() == MyDataInfo.MyMessage.DataType.StudentType){
            System.out.println("客户端发送数据：" + m.getStudent().getName());
        }


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
