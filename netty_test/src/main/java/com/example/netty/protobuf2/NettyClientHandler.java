package com.example.netty.protobuf2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * description
 * <p>
 * Created by xiexiaoxuan on 2020/3/22.
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {


    // 当通道就绪就会触发该方法
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        /**
         * 构造worker对象同理
         */
        MyDataInfo.MyMessage message = MyDataInfo.MyMessage.newBuilder()
                .setDataType(MyDataInfo.MyMessage.DataType.StudentType)
                .setStudent(MyDataInfo.Student.newBuilder().setId(100).setName("效玄").build())
                .build();
        ctx.writeAndFlush(message);
    }


    // 当通道有读取事件时就会触发
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("msg : " + buf.toString(CharsetUtil.UTF_8));
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
