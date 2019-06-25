package org.sq.gameDemo.cli;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;

public class CliHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) {
        channelHandlerContext.channel().attr(AttributeKey.valueOf("Attribute_key")).set(msg);
        channelHandlerContext.close();
    }
}
