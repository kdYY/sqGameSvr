package org.sq.gameDemo.svr.common;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.sq.gameDemo.common.NettyConstant;
import org.sq.gameDemo.svr.game.entity.model.MessageProto;

@Component
public class SvrChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    private NettyConstant nettyConstant;


    @Override
    protected void initChannel(SocketChannel ch) throws Exception {



//                .addLast(new LengthFieldBasedFrameDecoder(nettyConstant.getMaxFrameLength()
//                , 0, 2, 0, 2))
//                .addLast(new LengthFieldPrepender(2))
//                .addLast(new ObjectCodec())
//                .addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()))
//                .addLast(new StringDecoder(CharsetUtil.UTF_8))
//                .addLast(new StringEncoder(CharsetUtil.UTF_8))

        // 添加ProtobufVarint32FrameDecoder，主要用于Protobuf的半包处理
        ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
        // 添加ProtobufDecoder×××，它的参数是com.google.protobuf.MessageLite
        // 实际上就是要告诉ProtobufDecoder需要解码的目标类是什么，否则仅仅从字节数组中是
        // 无法判断出要解码的目标类型信息的（服务端需要解析的是客户端请求，所以是Req）
        ch.pipeline().addLast(new ProtobufDecoder(MessageProto.Msg.getDefaultInstance()));
        /**
         * 来自源码的代码注释，用于Protobuf的半包处理
         * * An encoder that prepends the the Google Protocol Buffers
         * <a href="https://developers.google.com/protocol-buffers/docs/encoding?csw=1#varints">Base
         * 128 Varints</a> integer length field. For example:
         * <pre>
         * BEFORE ENCODE (300 bytes)       AFTER ENCODE (302 bytes)
         * +---------------+               +--------+---------------+
         * | Protobuf Data |-------------->| Length | Protobuf Data |
         * |  (300 bytes)  |               | 0xAC02 |  (300 bytes)  |
         * +---------------+               +--------+---------------+
         * </pre> *
         */
        ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
        // 添加ProtobufEncoder编码器，这样就不需要对SubscribeResp进行手工编码
        ch.pipeline().addLast(new ProtobufEncoder());
        ch.pipeline().addLast(new SvrMessageHandler());
    }
}
