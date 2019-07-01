package org.sq.gameDemo.svr.common;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.sq.gameDemo.common.NettyConstant;
import org.sq.gameDemo.common.ObjectCodec;

@Component
public class SvrChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    private NettyConstant nettyConstant;


    @Override
    protected void initChannel(SocketChannel ch) throws Exception {


        ch.pipeline()
//                .addLast(new LengthFieldBasedFrameDecoder(nettyConstant.getMaxFrameLength()
//                , 0, 2, 0, 2))
//                .addLast(new LengthFieldPrepender(2))
//                .addLast(new ObjectCodec())
//                .addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()))
                .addLast(new StringDecoder(CharsetUtil.UTF_8))
                .addLast(new StringEncoder(CharsetUtil.UTF_8))
                .addLast(new SvrMessageHandler());
    }
}
