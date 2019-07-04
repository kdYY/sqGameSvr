package org.sq.gameDemo.svr.common;

import com.google.protobuf.MessageLite;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.sq.gameDemo.common.JsonUtil;
import org.sq.gameDemo.common.ProtobufNum;
import org.sq.gameDemo.svr.game.entity.model.MessageProto;
import org.sq.gameDemo.svr.game.entity.model.MessageProto2;

import java.util.List;

public class ProtobufDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        while (in.readableBytes() > 4) { // 如果可读长度小于包头长度，退出。
            in.markReaderIndex();

            // 获取包头中的body长度
            byte low = in.readByte();
            byte high = in.readByte();
            short s0 = (short) (low & 0xff);
            short s1 = (short) (high & 0xff);
            s1 <<= 8;
            short length = (short) (s0 | s1);

            // 获取包头中的protobuf类型
            in.readByte();
            byte dataType = in.readByte();

            // 如果可读长度小于body长度，恢复读指针，退出。
            if (in.readableBytes() < length) {
                in.resetReaderIndex();
                return;
            }

            // 读取body
            ByteBuf bodyByteBuf = in.readBytes(length);

            byte[] array;
            int offset;

            int readableLen= bodyByteBuf.readableBytes();
            if (bodyByteBuf.hasArray()) {
                array = bodyByteBuf.array();
                offset = bodyByteBuf.arrayOffset() + bodyByteBuf.readerIndex();
            } else {
                array = new byte[readableLen];
                bodyByteBuf.getBytes(bodyByteBuf.readerIndex(), array, 0, readableLen);
                offset = 0;
            }

            //反序列化
            MessageLite result = decodeBody(dataType, array, offset, readableLen);
            out.add(result);
        }
    }

    public MessageLite decodeBody(byte dataType, byte[] array, int offset, int length) throws Exception {
        MessageLite messageLite = (MessageLite)ProtobufNum.getMessageLiteByNum((int) dataType);
        return messageLite.
                   getParserForType().parseFrom(array, offset, length);

//        if (dataType == 0x00) {
//            return MessageProto.Msg.getDefaultInstance().
//                    getParserForType().parseFrom(array, offset, length);
//        }
//        if (dataType == 0x01) {
//            return MessageProto2.Msg.getDefaultInstance().
//                    getParserForType().parseFrom(array, offset, length);
//        }

        //return null; // or throw exception
    }
}
