package org.sq.gameDemo.svr.common;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import org.springframework.stereotype.Component;
import org.sq.gameDemo.common.DispatchRequest;

@Component
public class SvrMessageHandler extends SimpleChannelInboundHandler<Object> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel active");
        ctx.channel().writeAndFlush("welcome to game! type something to play game\r\n");
    }



//    @Override
//    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        ctx.channel().flush();
//    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object request) throws Exception {
        //做请求转码转发等
        // Generate and write a response.
        String response = "默认";
        boolean close = false;
        String req = String.valueOf(request);
        if (req.isEmpty()) {
            response = "Please type something.\r\n";
        } else if ("exit".equals(req.toLowerCase())) {
            response = "Have a good day!\r\n";
            close = true;
        } else {
            DispatchRequest.getInstance().getHandleBean(req);
            System.out.println("收到请求:" + req + "\r\n");
        }

        ChannelFuture future =  ctx.channel().writeAndFlush(response);
        // Close the connection after sending 'Have a good day!'
        // if the client has sent 'bye'.
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
