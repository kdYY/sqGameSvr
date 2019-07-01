package org.sq.gameDemo.svr.common;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.sq.gameDemo.common.DispatchRequest;


public class SvrMessageHandler extends SimpleChannelInboundHandler<Object> {

    private static final Logger log = Logger.getLogger(SvrMessageHandler.class);

    @Autowired
    private DispatchRequest dispatchRequest;


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel active");
        ctx.channel().writeAndFlush("welcome to game! type something to play game\r\n");
    }



//    @Override
//    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        ctx.channel().flush();
//    }

    /**
     *
     * @param ctx
     * @param request 指令请求
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object request) throws Exception {
        //做请求转码转发等
        // Generate and write a response.
        String response = "默认";
        boolean close = false;
//        String req = String.valueOf(request);
//        if (req.isEmpty()) {
//            response = "Please type something.\r\n";
//        } else if ("exit".equals(req.toLowerCase())) {
//            response = "Have a good day!\r\n";
//            close = true;
//        } else {
//            DispatchRequest.getInstance().getHandleBean(req);
//            System.out.println("收到请求:" + req + "\r\n");
//        }
//

        response = dispatchRequest.dispatch(ctx, String.valueOf(request));
        ChannelFuture future =  ctx.channel().writeAndFlush(response);
        if (close) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
