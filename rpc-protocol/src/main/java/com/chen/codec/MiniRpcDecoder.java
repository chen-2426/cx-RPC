package com.chen.codec;

import com.chen.Protocol.MiniRpcProtocol;
import com.chen.Protocol.MsgHeader;
import com.chen.Protocol.MsgType;
import com.chen.Protocol.ProtocolConstants;
import com.chen.common.MiniRpcRequest;
import com.chen.common.MiniRpcResponse;
import com.chen.serialization.RpcSerialization;
import com.chen.serialization.SerializationFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.List;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/10/26 14:14
 * @description
 */
public class MiniRpcDecoder extends ByteToMessageDecoder {
    //解码消息，传递个下一个InBound处理器
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
//        ByteBuf大于协议头时才开始读取数据
        if(in.readableBytes()< ProtocolConstants.HEADER_TOTAL_LEN){
            return;
        }
        //标记读指针位置
        in.markReaderIndex();
        short magic = in.readShort();
        if(magic!=ProtocolConstants.MAGIC){
            throw new IllegalArgumentException("magic number is illegal,"+magic);
        }
        byte version = in.readByte();
        byte serializeType = in.readByte();
        byte msgType = in.readByte();
        byte status = in.readByte();
        long requestId = in.readLong();
        int dataLength = in.readInt();
        if(in.readableBytes()<dataLength){
            //小于协议体长度，还原读指针位置，数据包不完整
            in.resetReaderIndex();
            return;
        }
        //协议体内容反序列化
        byte[] data = new byte[dataLength];
        in.readBytes(data);


        //判断请求类型
        MsgType msgTypeEnum = MsgType.findByType(msgType);
        if(msgTypeEnum == null){
            return;
        }

        MsgHeader header = new MsgHeader();
        header.setMagic(magic);
        header.setVersion(version);
        header.setSerialization(serializeType);
        header.setStatus(status);
        header.setRequestId(requestId);
        header.setMsgType(msgType);
        header.setMsgLen(dataLength);
        RpcSerialization rpcSerialization = SerializationFactory.getRpcSerialization(serializeType);
        switch (msgTypeEnum){
            case REQUEST: //请求体为REQUEST
                MiniRpcRequest request = rpcSerialization.deserialize(data, MiniRpcRequest.class);
                if(request!=null){
                    MiniRpcProtocol<MiniRpcRequest> protocol = new MiniRpcProtocol<>();
                    protocol.setMsgHeader(header);
                    protocol.setBody(request);
                    out.add(protocol);
                }
            case RESPONSE: //请求体为RESPONSE
                MiniRpcResponse response = rpcSerialization.deserialize(data, MiniRpcResponse.class);
                if(response!=null){
                    MiniRpcProtocol<MiniRpcResponse> protocol = new MiniRpcProtocol<>();
                    protocol.setMsgHeader(header);
                    protocol.setBody(response);
                    out.add(protocol);
                }

            case HEARTBEAT: //验证存活


        }
    }
}
