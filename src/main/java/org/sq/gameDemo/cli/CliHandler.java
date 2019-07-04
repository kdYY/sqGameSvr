package org.sq.gameDemo.cli;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.sq.gameDemo.svr.game.entity.model.MessageProto;

public class CliHandler extends SimpleChannelInboundHandler<MessageProto.Msg> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageProto.Msg msg) {
        MessageProto.Msg response = (MessageProto.Msg) msg;
        System.out.println("服务端返回1协议:" + response.getContent() + "\n");
    }


}
