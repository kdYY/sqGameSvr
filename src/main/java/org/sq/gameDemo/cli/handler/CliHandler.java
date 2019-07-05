package org.sq.gameDemo.cli.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.sq.gameDemo.common.entity.MsgEntity;
import org.sq.gameDemo.common.proto.MessageProto;

public class CliHandler extends SimpleChannelInboundHandler<MsgEntity> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MsgEntity msgEntity) throws Exception {

        System.out.println("接受服务端信息-->" + MessageProto.Msg.parseFrom(msgEntity.getData()).getContent());
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
