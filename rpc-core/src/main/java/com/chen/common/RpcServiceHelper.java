package com.chen.common;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/10/26 13:20
 * @description
 */
public class RpcServiceHelper {
        public static String buildServiceKey(String serviceName, String serviceVersion) {
            return String.join("#", serviceName, serviceVersion);
        }
        //给服务名称命名
}
