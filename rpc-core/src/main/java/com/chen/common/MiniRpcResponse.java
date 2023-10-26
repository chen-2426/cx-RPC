package com.chen.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/10/26 12:22
 * @description
 */
@Data
public class MiniRpcResponse implements Serializable {
    private Object data; //请求结果;
    private String message;  // 错误信息;
}
