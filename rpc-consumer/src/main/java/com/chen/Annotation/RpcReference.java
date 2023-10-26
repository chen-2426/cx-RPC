package com.chen.Annotation;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/10/25 21:37
 * @description 对消费者调用RPC方法注释
 */

import org.springframework.beans.factory.annotation.Autowired;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Autowired
public @interface RpcReference {
    String serviceVersion() default "1.0";
    String registryType() default "ZOOKEEPER";
    String registryAddress() default "127.0.0.1:2181";
    long timeout() default 5000;
}
