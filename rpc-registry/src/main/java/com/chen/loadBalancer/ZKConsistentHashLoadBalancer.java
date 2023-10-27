package com.chen.loadBalancer;

import com.chen.common.ServiceMeta;
import org.apache.curator.x.discovery.ServiceInstance;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/10/27 0:08
 * @description
 */
public class ZKConsistentHashLoadBalancer implements ServiceLoadBalancer<ServiceInstance<ServiceMeta>>{
    private final static int VIRTUAL_NODE_SIZE = 10;
    private final static String VIRTUAL_NODE_SPLIT = "#";
    @Override
    public ServiceInstance<ServiceMeta> select(List<ServiceInstance<ServiceMeta>> servers, int hashCode) {
        TreeMap<Integer, ServiceInstance<ServiceMeta>> ring = makeConsistentHashRing(servers);
        return allocateNode(ring, hashCode);
    }
    //分配节点
    private ServiceInstance<ServiceMeta> allocateNode(TreeMap<Integer, ServiceInstance<ServiceMeta>> ring, int hashCode) {
        //ceilingEntry()方法用于返回与大于或等于给定键元素(ele)的最小键元素链接的键值对。
        Map.Entry<Integer, ServiceInstance<ServiceMeta>> entry = ring.ceilingEntry(hashCode);
        //环上无节点，放在最开始的地方。
        if (entry == null) {
            entry = ring.firstEntry();
        }
        return entry.getValue();
    }
    //构建hash环
    public TreeMap<Integer,ServiceInstance<ServiceMeta>> makeConsistentHashRing(List<ServiceInstance<ServiceMeta>> servers){
        TreeMap<Integer, ServiceInstance<ServiceMeta>> ring = new TreeMap<>(); //哈希环
        for (ServiceInstance<ServiceMeta> instance : servers) {
            for (int i = 0; i < VIRTUAL_NODE_SIZE; i++) {
                ring.put((buildServiceInstanceKey(instance)+VIRTUAL_NODE_SPLIT+i).hashCode(),instance);
            }
            
        }
        return ring;
    }
    //获取hash值
    private String buildServiceInstanceKey(ServiceInstance<ServiceMeta> instance) {
        ServiceMeta payload = instance.getPayload();
        return String.join(":", payload.getServiceAddr(), String.valueOf(payload.getServicePort()));
    }
}
