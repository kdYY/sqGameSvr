package org.sq.gameDemo.common;

import com.google.protobuf.MessageLite;
import org.sq.gameDemo.common.proto.MessageProto;
import org.sq.gameDemo.common.proto.MessageProto2;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public enum ProtobufNum {
    MessageProtoc1(1, MessageProto.Msg.class),
    MessageProtoc2(2, MessageProto2.Msg.class);


    private  Integer protoNum;
    private Class messageLiteClazz;

    private  ProtobufNum(Integer protoNum, Class messageLite) {
        this.protoNum = protoNum;
        this.messageLiteClazz = messageLite;
    }

    public Integer getProtoNum() {
        return protoNum;
    }

    public Class getMessageLiteClazz() {
        return messageLiteClazz;
    }

    public static Object getMessageLiteByNum(Integer protoNum) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        for (ProtobufNum protobufNum : ProtobufNum.values()) {
            if(protobufNum.getProtoNum() == protoNum) {
                Constructor constructor = protobufNum.getMessageLiteClazz().getDeclaredConstructor();
                constructor.setAccessible(true);
                return constructor.newInstance();
            }
        }
        return null;
    }

//    if (msg instanceof MessageProto.Msg) {
//        messageType = 0x00;
//    }
    public static Byte getNumByMessageLite(MessageLite msg) {
    for (ProtobufNum protobufNum : ProtobufNum.values()) {

        if(msg.getClass() == protobufNum.getMessageLiteClazz()) {
            // TODO 控制1个字节
            return Byte.parseByte(String.valueOf(protobufNum.getProtoNum()));

        }
    }
    return null;
}

}
