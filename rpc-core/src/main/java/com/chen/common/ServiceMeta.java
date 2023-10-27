package com.chen.common;

import io.netty.util.concurrent.Promise;
import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/10/25 21:11
 * @description
 */
@Data
public class ServiceMeta {
    private String serviceName;

    private String serviceVersion;

    private String serviceAddr;

    private int servicePort;
}


