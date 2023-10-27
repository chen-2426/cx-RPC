package com.chen.Protocol;

import lombok.Getter;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/10/27 13:53
 * @description
 */
public enum MsgStatus {
    SUCCESS(0),
    FAIL(1);

    @Getter
    private final int code;

    MsgStatus(int code) {
        this.code = code;
    }

}
