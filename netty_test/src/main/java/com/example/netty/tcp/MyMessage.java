package com.example.netty.tcp;

import java.util.Arrays;

/**
 * description
 * <p>
 * Created by xiexiaoxuan on 2020/3/27.
 */
public class MyMessage {

    private int length;
    private byte[] content;

    public MyMessage(int length, byte[] content) {
        this.length = length;
        this.content = content;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "MyMessage{" +
                "length=" + length +
                ", content=" + new String(content) +
                '}';
    }
}
