package org.sq.gameDemo.svr.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.log4j.Logger;

public class SvrMessageHandler3 extends SimpleChannelInboundHandler<Object> {

    private static final Logger log = Logger.getLogger(SvrMessageHandler3.class);


    /**
     * 做请求转发
     * @param ctx
     * @param request 指令请求
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object request) throws Exception {
        System.out.println("接受客户端发送1协议消息");
        //在这里分发Handler


    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
