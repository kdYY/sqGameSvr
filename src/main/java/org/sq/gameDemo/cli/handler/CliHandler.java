package org.sq.gameDemo.cli.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.sq.gameDemo.common.entity.MsgEntity;
import org.sq.gameDemo.common.proto.MessageProto;
import org.sq.gameDemo.svr.common.PoiUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class CliHandler extends SimpleChannelInboundHandler<MsgEntity> {


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MsgEntity msgEntity) throws Exception {

        MessageProto.Msg msg = MessageProto.Msg.parseFrom(msgEntity.getData());

        System.out.println("接受服务端信息-->" + msg.getContent());
        System.out.println("返回用户token->" + msg.getToken());

//        if (OrderEnum.getOrder(msgEntity.getCmdCode()).equals("site")) {// 名字检查回包
//            NameCheckResp resp = null;
//            resp = NameCheckResp.parseFrom(msgEntity.getData());
//            if (resp.getIsExist()) {
//                System.out.println("改名字已经存在,请换一个名字");
//            } else {// 如果链接成功,则发言
//                gameClient.sendHello("hello");
//            }
//        }
    }
}
