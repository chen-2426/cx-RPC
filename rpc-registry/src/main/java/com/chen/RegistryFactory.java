package com.chen;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/10/27 11:13
 * @description
 */
public class RegistryFactory {
    public static RegistryService getInstance(String registryAddr, RegistryType type) throws Exception {
        switch (type){
            case ZOOKEEPER:
                return new ZookeeperRegistryService(registryAddr);
            case NACOS:
                return null;
            default:
                throw new IllegalArgumentException("registry type is illegal " + type);
        }

    }
}
