package org.sq.gameDemo.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.sq.gameDemo.common.entity.MsgEntity;

/**
 * 服务端这里继承<code>MessageToByteEncoder</code>更加方便
 */
public class MsgEncoder extends MessageToByteEncoder<MsgEntity> {

	@Override
	protected void encode(ChannelHandlerContext ctx, MsgEntity msg, ByteBuf byteBuf) throws Exception {
//	    int a = 0xa5a9;
//
//	    byteBuf.writeInt(msg.getHead());
//	    byteBuf.writeShort(msg.getType());
//	    byteBuf.writeShort(msg.getLength());
//        int dataLength = msg.getData() == null ? 0 : msg.getData().length;
//        if (dataLength > 0) {
//            byteBuf.writeBytes(msg.getData());
//        }


		int dataLength = msg.getData() == null ? 0 : msg.getData().length;
		byteBuf.ensureWritable(4 + dataLength);


		byteBuf.writeInt(dataLength);
		byteBuf.writeShort(msg.getCmdCode());

		if (dataLength > 0) {
			byteBuf.writeBytes(msg.getData());
		}
	}

}
