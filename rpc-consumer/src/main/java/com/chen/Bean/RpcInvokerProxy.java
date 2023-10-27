package com.chen.Bean;

import com.chen.Protocol.MiniRpcProtocol;
import com.chen.Protocol.MsgHeader;
import com.chen.Protocol.MsgType;
import com.chen.Protocol.ProtocolConstants;
import com.chen.RegistryService;
import com.chen.common.MiniRpcFuture;
import com.chen.common.MiniRpcRequest;
import com.chen.common.MiniRpcRequestHolder;
import com.chen.common.MiniRpcResponse;
import com.chen.serialization.SerializationTypeEnum;
import io.netty.channel.DefaultEventLoop;
import io.netty.util.concurrent.DefaultPromise;
import org.springframework.cglib.proxy.InvocationHandler;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.TimeUnit;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/10/27 10:57
 * @description
 */
public class RpcInvokerProxy implements InvocationHandler {
    private final String serviceVersion;
    private final long timeout;
    private final RegistryService registryService;

    public RpcInvokerProxy(String serviceVersion, long timeout, RegistryService registryService) {
        this.serviceVersion = serviceVersion;
        this.timeout = timeout;
        this.registryService = registryService;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MiniRpcProtocol<MiniRpcRequest> protocol = new MiniRpcProtocol<>();
        //设定报头、报体
        MsgHeader header = new MsgHeader();
        MiniRpcRequest request = new MiniRpcRequest();
        long requestId = MiniRpcRequestHolder.REQUEST_ID_GEN.incrementAndGet();
        header.setMagic(ProtocolConstants.MAGIC);
        header.setVersion(ProtocolConstants.VERSION);
        header.setRequestId(requestId);
        header.setSerialization((byte) SerializationTypeEnum.HESSIAN.getType());
        header.setMsgType((byte)MsgType.REQUEST.getType());
        header.setStatus((byte)0x1);
        protocol.setMsgHeader(header);
        request.setServiceVersion(this.serviceVersion);
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParams(args);
        protocol.setBody(request);

        RpcConsumer rpcConsumer = new RpcConsumer();
        MiniRpcFuture<MiniRpcResponse> future = new MiniRpcFuture<>(new DefaultPromise<>(new DefaultEventLoop()), timeout);
        MiniRpcRequestHolder.REQUEST_MAP.put(requestId, future);

        //发起RPC远程调用
        rpcConsumer.sendRequest(protocol, this.registryService);

        // TODO hold request by ThreadLocal

        //等待RPC执行结果
        return future.getPromise().get(future.getTimeout(), TimeUnit.MILLISECONDS).getData();
    }
}
