package org.sq.gameDemo.cli;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.log4j.Logger;
import org.sq.gameDemo.common.proto.MessageProto;
import org.sq.gameDemo.common.proto.MessageProto2;

import java.util.Scanner;

public class GameCli {

    private static final Logger log = Logger.getLogger(GameCli.class);

    private static Bootstrap b;
    private static ChannelFuture f;
    private static final EventLoopGroup workerGroup = new NioEventLoopGroup();

    private static void init () {
        try {
            b = new Bootstrap();
            b.group(workerGroup).remoteAddress("127.0.0.1", 8085);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new CliChannelInitializer());
            f = b.connect().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws InterruptedException {
        init();
        Scanner scanner = new Scanner(System.in);
        String line = "";
        while (scanner.hasNext()) {
            try {
                line = scanner.nextLine();
                GameCli.sendMsg(line);
                line = "";
            } catch (Exception e) {
                log.error(e.getMessage());
                e.printStackTrace();
                //关闭连接
                f.channel().close().sync();
            }
        }

    }


    public static void sendMsg(Object send) throws InterruptedException {
        // 传数据给服务端
        f.channel().writeAndFlush(subReq(String.valueOf(send)));
    }

    private static MessageProto.Msg subReq(String send) {

        //register name=kevins password=123456
        String[] input = send.split(" ");

        MessageProto.Msg.Builder builder = MessageProto.Msg.newBuilder();
        builder.setOrder(String.valueOf(send));
        return builder.build();

    }

}
