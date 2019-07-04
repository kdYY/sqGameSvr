package org.sq.gameDemo.cli;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import org.apache.log4j.Logger;
import org.sq.gameDemo.cli.handler.CliHandler;
import org.sq.gameDemo.cli.handler.CliHandler2;
import org.sq.gameDemo.common.ProtobufDecoder;
import org.sq.gameDemo.common.ProtobufEncoder;
import org.sq.gameDemo.common.proto.MessageProto;
import org.sq.gameDemo.common.proto.MessageProto2;

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
                protected void initChannel(SocketChannel ch) {
                    ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
                    ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
                    ch.pipeline().addLast(new ProtobufDecoder());
                    ch.pipeline().addLast(new ProtobufEncoder());
                    ch.pipeline().addLast(new CliHandler());
                    ch.pipeline().addLast(new CliHandler2());
                }
            });
            f = b.connect().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendMsg(Object send) throws InterruptedException {
        // 传数据给服务端
        f.channel().writeAndFlush(subReq2(send));
    }

    public static void main(String[] args) throws InterruptedException {
        init();
        Scanner scanner = new Scanner(System.in);
        String line = "";
        while (scanner.hasNext()) {
            try {
                line = scanner.nextLine();
                CliTest.sendMsg(line);
                line = "";
            } catch (Exception e) {
                log.error(e.getMessage());
                e.printStackTrace();
                //关闭连接
                f.channel().close().sync();
            }
        }

    }
    private static MessageProto.Msg subReq(Object send) {
        MessageProto.Msg.Builder builder = MessageProto.Msg.newBuilder();
        builder.setMsgId(2L);
        builder.setOrder(String.valueOf(send));
        return builder.build();
    }

    private static MessageProto2.Msg subReq2(Object send) {
        MessageProto2.Msg.Builder builder = MessageProto2.Msg.newBuilder();
        builder.setMsgId(2L);
        builder.setOrder(String.valueOf(send));
        builder.setContent("这是客户端发的2协议");
        return builder.build();
    }
}
