package org.sq.gameDemo.cli;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.sq.gameDemo.svr.game.entity.model.MessageProto;
import org.sq.gameDemo.svr.game.entity.model.MessageProto2;

public class CliHandler2 extends SimpleChannelInboundHandler<MessageProto2.Msg> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageProto2.Msg msg) {
        MessageProto2.Msg response = (MessageProto2.Msg) msg;
        System.out.println("服务端返回2协议:" + response.getContent() + "\n");
    }


}