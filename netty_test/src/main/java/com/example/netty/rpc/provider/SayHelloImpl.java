package com.example.netty.rpc.provider;

import com.example.netty.rpc.publicinterface.SayHello;

/**
 * description
 * <p>
 * Created by xiexiaoxuan on 2020/3/28.
 */
public class SayHelloImpl implements SayHello {
    @Override
    public String say(String string) {

        System.out.println("msg: " + string);
        return string;
    }
}
