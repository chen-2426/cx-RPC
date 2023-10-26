package com.chen.Annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/10/25 20:24
 * @description 对生产者被调用RPC方法注释
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface RpcService {
    Class<?> serviceInterface() default Object.class;
    String serviceVersion() default "1.0";
}
