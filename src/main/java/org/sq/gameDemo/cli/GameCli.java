package org.sq.gameDemo.cli;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.log4j.Logger;
import org.sq.gameDemo.cli.service.SendOrderService;
import org.sq.gameDemo.common.entity.MsgEntity;
import org.sq.gameDemo.common.OrderEnum;
import org.sq.gameDemo.common.proto.MessageProto;

import java.util.Scanner;


public class GameCli {

    private static SendOrderService sendOrderService = new SendOrderService();
    private static final Logger log = Logger.getLogger(GameCli.class);
    private static Bootstrap b;
    private static ChannelFuture f;
    private static final EventLoopGroup workerGroup = new NioEventLoopGroup();

    /**
     * 初始化客户端
     */
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
               // line = "";
            } catch (Exception e) {
                log.error(e.getMessage());
                e.printStackTrace();
                //关闭连接
                f.channel().close().sync();
            }
        }

    }

    /**
     * 发送请求
     * @param send
     * @return
     */
    public static void sendMsg(String send) throws InterruptedException {
        // 传数据给服务端
        MsgEntity sendMsgEntity = sendMsgEntity(send);
        if(sendMsgEntity == null) {
            System.out.println("输入指令有误");
            return;
        }
        f.channel().writeAndFlush(sendMsgEntity);
    }

    private static MsgEntity sendMsgEntity(String request) {

        //register name=kevins&password=123456
        //login name=kevins&password=123456
        //move sence=1
        //
        if(request == null || request.equals("")) {
            return null;
        }
        String[] input = request.split(" ");
        MsgEntity msgEntity = new MsgEntity();
        if(input == null || input.length == 0)
            return null;
        OrderEnum orderEnum = OrderEnum.getOrderEnumByOrder(input[0]);
        msgEntity.setCmdCode(orderEnum.getOrderCode());

        switch (orderEnum) {
            case Register:
                sendOrderService.register(msgEntity, input);
                break;
            case Login:
                sendOrderService.login(msgEntity, input);
                break;
            case ErrOrder:
                return null;

            default:break;
        }
        return msgEntity;
    }


}
