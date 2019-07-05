package org.sq.gameDemo.common.entity;


import io.netty.channel.Channel;

import java.util.Arrays;

public class MsgEntity {
    private short cmdCode;
    private byte[] data;
    private Channel channel;

    public short getCmdCode() {
        return cmdCode;
    }

    public void setCmdCode(short cmdCode) {
        this.cmdCode = cmdCode;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }


    @Override
    public String toString() {
        return "MsgEntity{" +
                "cmdCode=" + cmdCode +
                ", data=" + Arrays.toString(data) +
                '}';
    }
}
