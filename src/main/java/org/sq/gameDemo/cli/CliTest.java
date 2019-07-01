package org.sq.gameDemo.cli;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
import org.apache.log4j.Logger;

import java.net.InetSocketAddress;
import java.util.Scanner;

public class CliTest {

    private static final Logger log = Logger.getLogger(CliTest.class);

    private static Bootstrap b;
    private static ChannelFuture f;
    private static final EventLoopGroup workerGroup = new NioEventLoopGroup();

    private static void init () {
        try {
            log.info("init...");
            b = new Bootstrap();
            b.group(workerGroup)
                    .remoteAddress("127.0.0.1", 8085);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) {
                    // 解码编码
                    socketChannel.pipeline().addLast(new StringDecoder(CharsetUtil.UTF_8));
                    socketChannel.pipeline().addLast(new StringEncoder(CharsetUtil.UTF_8));
                    socketChannel.pipeline().addLast(new CliHandler());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendMsg(Object send) throws InterruptedException {
        // 传数据给服务端
        f.channel().writeAndFlush(send);
    }

    public static void main(String[] args) throws InterruptedException {
        init();
        f = b.connect().sync();
        Scanner scanner = new Scanner(System.in);
        String line = "";
        while (scanner.hasNext()) {
            try {
                line = scanner.nextLine();
                CliTest.sendMsg( line);
                line = "";
            } catch (Exception e) {
                log.error(e.getMessage());
                e.printStackTrace();
                //关闭连接
                f.channel().close().sync();
            } finally {

            }
        }

    }
}
