package org.sq.gameDemo.svr.common;

import io.netty.channel.*;
import io.netty.util.ReferenceCountUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.sq.gameDemo.common.DispatchRequest;
import org.sq.gameDemo.common.SpringUtil;
import org.sq.gameDemo.svr.game.entity.model.MessageProto;

public class SvrMessageHandler extends SimpleChannelInboundHandler<MessageProto.Msg> {

    private static final Logger log = Logger.getLogger(SvrMessageHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel active");
        ctx.channel().writeAndFlush(subReq("welcome to game! type something to play game"));
    }


    private static MessageProto.Msg subReq(Object send) {
        MessageProto.Msg.Builder builder = MessageProto.Msg.newBuilder();
        builder.setMsgId(2L);
        builder.setContent(String.valueOf(send));
        return builder.build();
    }
    /**
     * 做请求转发
     * @param ctx
     * @param request 指令请求
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProto.Msg request) throws Exception {
        System.out.println("接受客户端发送1协议消息");
        MessageProto.Msg msg = (MessageProto.Msg) request;
        DispatchRequest.dispatchRequest(ctx, msg.getOrder());
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
