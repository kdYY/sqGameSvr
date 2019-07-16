package org.sq.gameDemo.svr.net;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import org.sq.gameDemo.svr.handler.AcceptorIdleStateTrigger;

import java.net.InetSocketAddress;

@Slf4j
@Service
public class GameSvr {

    @Autowired
    private SvrChannelInitializer svrChannelInitializer;




    private final EventLoopGroup bossGroup = new NioEventLoopGroup();
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();
    private Channel channel;

    public ChannelFuture run(InetSocketAddress address) {
        ChannelFuture future = null;
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(svrChannelInitializer)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            future = b.bind(address).sync();
            System.out.println(GameSvr.class.getName() + " started and listen on " + future.channel().localAddress());
            channel = future.channel();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (future != null && future.isSuccess()) {
                log.info("Netty server listening on port " + address.getPort() + " and ready for connections...");
            } else {
                log.error("Netty server start up Error!");
            }
        }
        return  future;
    }
    public void destroy() {
        log.info("Shutdown Netty Server...");
        if(channel != null) {
            channel.close();
        }
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
        log.info("Shutdown Netty Server Success!");
    }
}
