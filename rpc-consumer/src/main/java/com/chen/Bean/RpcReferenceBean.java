package com.chen.Bean;

import org.springframework.beans.factory.FactoryBean;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/10/25 21:44
 * @description
 */

public class RpcReferenceBean implements FactoryBean<Object> {



    //可修改的参数信息
    private String serviceVersion;
    private String registryType;
    private String registryAddr;
    private long timeout;


    private Object object;
    private Class<?> interfaceClass;
    //返回有FactoryBean创建的Bean实例
    @Override
    public Object getObject() throws Exception {
        return this.object;
    }
    //返回有FactoryBean创建的Bean类型，这里返回的是接口类型
    @Override
    public Class<?> getObjectType() {
        return interfaceClass;
    }

    public void init() throws Exception{
        //TODO 生成动态代理类，并赋值给Object;
    }

    public void setServiceVersion(String serviceVersion) {
        this.serviceVersion = serviceVersion;
    }

    public void setRegistryType(String registryType) {
        this.registryType = registryType;
    }

    public void setRegistryAddr(String registryAddr) {
        this.registryAddr = registryAddr;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
}
