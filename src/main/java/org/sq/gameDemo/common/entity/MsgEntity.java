package org.sq.gameDemo.common.entity;


import com.google.common.primitives.Bytes;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.Channel;
import lombok.Data;
import org.apache.coyote.http2.ByteUtil;

import java.util.Arrays;

@Data
public class MsgEntity {
    private Integer head;
    private Short type;
    private Short length;
    private Byte[] datas;



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

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    @Override
    public String toString() {
        return "MsgEntity{" +
                "cmdCode=" + cmdCode +
                ", data=" + Arrays.toString(data) +
                '}';
    }


    public static void main(String[] args) {


    }
}
