package com.chen;

import com.chen.common.ServiceMeta;

import java.io.IOException;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/10/25 21:23
 * @description
 */
public interface RegistryService {

    void register(ServiceMeta serviceMeta) throws Exception;

    void unRegister(ServiceMeta serviceMeta) throws Exception;

    ServiceMeta discovery(String serviceName, int invokerHashCode) throws Exception;

    void destroy() throws IOException;
}
