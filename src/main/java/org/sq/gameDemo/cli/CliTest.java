package org.sq.gameDemo.cli;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import org.apache.log4j.Logger;
import org.sq.gameDemo.svr.game.entity.model.MessageProto;

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
                    // 解码编码
//                    ch.pipeline().addLast(new StringDecoder(CharsetUtil.UTF_8));
//                    ch.pipeline().addLast(new StringEncoder(CharsetUtil.UTF_8));

                    // 添加ProtobufVarint32FrameDecoder，主要用于Protobuf的半包处理
                    ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
                    // 添加ProtobufDecoder×××，它的参数是com.google.protobuf.MessageLite
                    // 实际上就是要告诉ProtobufDecoder需要解码的目标类是什么，否则仅仅从字节数组中是
                    // 无法判断出要解码的目标类型信息的（客户端需要解析的是服务端请求，所以是Resp）
                    ch.pipeline().addLast(new ProtobufDecoder(MessageProto.Msg.getDefaultInstance()));
                    /**
                     * 来自源码的代码注释，用于Protobuf的半包处理
                     * * An encoder that prepends the the Google Protocol Buffers
                     * <a href="https://developers.google.com/protocol-buffers/docs/encoding?csw=1#varints">Base
                     * 128 Varints</a> integer length field. For example:
                     * <pre>
                     * BEFORE ENCODE (300 bytes)       AFTER ENCODE (302 bytes)
                     * +---------------+               +--------+---------------+
                     * | Protobuf Data |-------------->| Length | Protobuf Data |
                     * |  (300 bytes)  |               | 0xAC02 |  (300 bytes)  |
                     * +---------------+               +--------+---------------+
                     * </pre> *
                     */
                    ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
                    // 添加ProtobufEncoder编码器，这样就不需要对SubscribeResp进行手工编码
                    ch.pipeline().addLast(new ProtobufEncoder());
                    ch.pipeline().addLast(new CliHandler());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendMsg(Object send) throws InterruptedException {
        // 传数据给服务端
        f.channel().writeAndFlush(subReq(send));
    }

    public static void main(String[] args) throws InterruptedException {
        init();
        f = b.connect().sync();
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
            } finally {

            }
        }

    }
    private static MessageProto.Msg subReq(Object send) {
        MessageProto.Msg.Builder builder = MessageProto.Msg.newBuilder();
//        builder.setSubReqID(i);
//        builder.setUserName("xpleaf");
//        builder.setProductName("Netty Book For Protobuf");
//        List<String> address = new ArrayList<>();
//        address.add("NanJing YuHuaTai");
//        address.add("BeiJing LiuLiChange");
//        address.add("ShenZhen HongShuLin");
//        builder.addAllAddress(address);
        builder.setMsgId(2L);
        builder.setOrder(String.valueOf(send));
        return builder.build();
    }
}
