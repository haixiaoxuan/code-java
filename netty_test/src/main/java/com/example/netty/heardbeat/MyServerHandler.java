package com.example.netty.heardbeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * description
 * <p>
 * Created by xiexiaoxuan on 2020/3/24.
 */
public class MyServerHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        if(evt instanceof IdleStateEvent){

            IdleStateEvent event = (IdleStateEvent) evt;

            String state = null;
            switch (event.state()){
                case READER_IDLE:
                    state = "read idle";
                    break;
                case WRITER_IDLE:
                    state = "write idle";
                    break;
                case ALL_IDLE:
                    state = "all idle";
                    break;
            }

            System.out.println(ctx.channel().remoteAddress() + " " + state);
        }
    }
}
