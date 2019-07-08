package org.sq.gameDemo.cli;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.log4j.Logger;
import org.sq.gameDemo.cli.service.SendOrderService;
import org.sq.gameDemo.common.entity.MsgEntity;
import org.sq.gameDemo.common.OrderEnum;
import org.sq.gameDemo.svr.common.PoiUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;


public class GameCli {

    private static SendOrderService sendOrderService = new SendOrderService();
    private static final Logger log = Logger.getLogger(GameCli.class);
    private static Bootstrap b;
    private static ChannelFuture f;
    private static final EventLoopGroup workerGroup = new NioEventLoopGroup();

    private static String token = null;

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
        readToken();
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

    private static void readToken() throws InterruptedException {
        InputStream in = PoiUtil.class.getClassLoader().getResourceAsStream("token");
        byte b[] = new byte[1024];
        int len = 0;
        int temp=0;          //全部读取的内容都使用temp接收
        try {
            while((temp = in.read()) != -1) {    //当没有读取完时，继续读取
                b[len] = (byte)temp;
                len++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(len <= 0) {
            System.out.println("请先登录");
        } else {
            token = new String(b, 0, len);
            sendMsg("checkToken token=" + token);
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
//            case GetRole:
//                break;
            case BindRole:
                sendOrderService.bindRole(msgEntity, input);
                break;
            case Help:
                sendOrderService.help();
                return null;
            case ErrOrder:
                System.out.println("输入指令有误");
                return null;
            default:break;
        }
        return msgEntity;
    }


}
