package com.chen.Protocol;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/10/25 19:04
 * @description 配置注入
 */
@Data
@ConfigurationProperties(prefix = "chen.rpc")
public class RpcProperties {
    private int servicePort;
    private String registryAddr;
    private String registryType;
}
