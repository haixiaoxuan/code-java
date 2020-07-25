package com.example.netty.rpc.provider;


import com.example.netty.rpc.netty.NettyServer;

/**
 * description
 * <p>
 * Created by xiexiaoxuan on 2020/3/28.
 */
public class ServerBootStrap {

    public static void main(String[] args){

        NettyServer.startServer("127.0.0.1", 6666);
    }
}
