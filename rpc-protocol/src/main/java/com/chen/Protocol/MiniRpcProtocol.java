package com.chen.Protocol;

import lombok.Data;

import java.io.Serializable;



/**
 * @author chenxi
 * @version 1.0
 * @date 2023/10/26 12:07
 * @description  自定义协议
 */
@Data
public class MiniRpcProtocol<T> implements Serializable {
    private MsgHeader msgHeader;  //协议头
    private T Body;  //协议体 与MiniRpcRequest、MiniRpcResponse 对应

}
