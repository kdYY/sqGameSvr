package org.sq.gameDemo.svr.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Logger;
import org.sq.gameDemo.common.entity.MsgEntity;
import org.sq.gameDemo.common.proto.MessageProto;
import org.sq.gameDemo.svr.common.dispatch.DispatchRequest;
@Slf4j
public class SvrHandler extends SimpleChannelInboundHandler<MsgEntity> {
   // private static final Logger log = Logger.getLogger(SvrMessageHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel active");
    }

    /**
     * 做请求转发
     * @param ctx
     * @param msgEntity 协议请求实体
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MsgEntity msgEntity) throws Exception {
        System.out.println("接受客户端发送1协议消息");
        DispatchRequest.dispatchRequest(ctx, msgEntity);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //玩家掉线，玩家数据 回写 到数据库进行保存，同时设置玩家数据在内存中保留的时间，避免内存泄漏

    }
}
