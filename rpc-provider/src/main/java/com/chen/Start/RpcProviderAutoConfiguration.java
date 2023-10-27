package com.chen.Start;

import com.chen.Protocol.RpcProperties;
import com.chen.RegistryFactory;
import com.chen.RegistryService;
import com.chen.RegistryType;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/10/25 19:29
 * @description
 */
@Configuration
@EnableConfigurationProperties(RpcProperties.class)
public class RpcProviderAutoConfiguration {

    @Resource
    private RpcProperties rpcProperties;

    @Bean
    public RpcProvider init() throws Exception {
        RegistryType type = RegistryType.valueOf(rpcProperties.getRegistryType());
        RegistryService serviceRegistry = RegistryFactory.getInstance(rpcProperties.getRegistryAddr(), type);
        return new RpcProvider(rpcProperties.getServicePort(), serviceRegistry);
    }

}
