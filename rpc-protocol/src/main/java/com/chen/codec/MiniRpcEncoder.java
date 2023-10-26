package com.chen.codec;

import com.chen.Protocol.MiniRpcProtocol;
import com.chen.Protocol.MsgHeader;
import com.chen.serialization.RpcSerialization;
import com.chen.serialization.SerializationFactory;
import com.sun.xml.internal.ws.developer.Serialization;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/10/26 14:14
 * @description MessageToByteEncoder 编解码实现基类
 */
public class MiniRpcEncoder extends MessageToByteEncoder<MiniRpcProtocol<Object>> {
    //调用write和flush前
    @Override
    protected void encode(ChannelHandlerContext ctx, MiniRpcProtocol<Object> msg, ByteBuf byteBuf) throws Exception {
        MsgHeader header = msg.getMsgHeader();
        byteBuf.writeShort(header.getMagic());
        byteBuf.writeByte(header.getVersion());
        byteBuf.writeByte(header.getSerialization());
        byteBuf.writeByte(header.getMsgType());
        byteBuf.writeByte(header.getStatus());
        byteBuf.writeLong(header.getRequestId());
        RpcSerialization rpcSerialization = SerializationFactory.getRpcSerialization(header.getSerialization());
        byte[] data = rpcSerialization.serialize(msg.getBody());
        byteBuf.writeInt(data.length);
        byteBuf.writeBytes(data);
    }

}
