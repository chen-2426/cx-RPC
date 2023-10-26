package com.chen.Protocol;

import lombok.Getter;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/10/26 14:35
 * @description
 */
public enum MsgType {

    REQUEST(1),
    RESPONSE(2),
    HEARTBEAT(3);
    @Getter
    private final int type;

    MsgType(int type) {
        this.type = type;
    }

    public static MsgType findByType(int type) {
        for (MsgType msgType : MsgType.values()) {
            if (msgType.getType() == type) {
                return msgType;
            }
        }
        return null;
    }

}
