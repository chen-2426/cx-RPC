package com.chen.Protocol;

import lombok.Data;

import java.io.Serializable;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/10/26 12:07
 * @description 自定义协议头
 */
@Data
public class MsgHeader implements Serializable {
    private short magic;//魔数
    private byte version;//版本号
    private byte serialization; //序列化算法
    private byte msgType;   //报文类型
    private byte status;    //状态
    private long requestId; //消息ID
    private int msgLen;//数据长度
}

