package com.chen.loadBalancer;

import java.util.List;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/10/26 23:52
 * @description
 */
public interface ServiceLoadBalancer<T> {
    //传入服务节点和hashcode
    T select(List<T> servers, int hashCode);
}
