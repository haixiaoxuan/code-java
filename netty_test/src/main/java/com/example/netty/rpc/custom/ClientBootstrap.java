package com.example.netty.rpc.custom;

import com.example.netty.rpc.netty.NettyClient;
import com.example.netty.rpc.publicinterface.SayHello;

/**
 * description
 * <p>
 * Created by xiexiaoxuan on 2020/3/28.
 */
public class ClientBootstrap {

    public static void main(String[] args){

        NettyClient client = new NettyClient();

        // 创建代理对象
        SayHello bean = (SayHello) client.getBean(SayHello.class, "SayHello#say#");

        bean.say("xiaoxuan");

    }
}
