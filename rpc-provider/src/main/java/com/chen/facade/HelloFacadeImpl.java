package com.chen.facade;

import com.chen.Annotation.RpcService;
import com.chen.Facade.HelloFacade;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/10/27 14:57
 * @description
 */

@RpcService(serviceInterface = HelloFacade.class, serviceVersion = "1.0.0")
public class HelloFacadeImpl implements HelloFacade {
    @Override
    public String hello(String name) {
        return "hello" + name;
    }
}