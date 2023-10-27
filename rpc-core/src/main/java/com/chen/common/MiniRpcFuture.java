package com.chen.common;

import io.netty.util.concurrent.Promise;
import lombok.Data;

@Data
public class MiniRpcFuture<T> {
    private Promise<T> promise; //异步编程模型
    private long timeout;

    public MiniRpcFuture(Promise<T> promise, long timeout) {
        this.promise = promise;
        this.timeout = timeout;
    }
}
