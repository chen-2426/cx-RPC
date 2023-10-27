package com.chen;

import com.chen.RegistryService;
import com.chen.common.RpcServiceHelper;
import com.chen.common.ServiceMeta;
import com.chen.loadBalancer.ZKConsistentHashLoadBalancer;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/10/26 23:12
 * @description
 */
public class ZookeeperRegistryService implements RegistryService {
    public static final int BASE_SLEEP_TIME_MS = 1000;
    public static final int MAX_RETIES = 3;
    public static final String ZK_BASE_PATH = "/mini_rpc";
    private final ServiceDiscovery<ServiceMeta> serviceDiscovery;

    public ZookeeperRegistryService(String registryAddr) throws Exception {
        //创建Framework实例
        CuratorFramework client = CuratorFrameworkFactory.newClient(registryAddr, new ExponentialBackoffRetry(BASE_SLEEP_TIME_MS, MAX_RETIES));
        client.start();
        JsonInstanceSerializer<ServiceMeta> serializer = new JsonInstanceSerializer<>(ServiceMeta.class);
        this.serviceDiscovery = ServiceDiscoveryBuilder
                .builder(ServiceMeta.class)
                .client(client)
                .serializer(serializer)
                .build();
        this.serviceDiscovery.start();
    }

    //注册服务实例
    @Override
    public void register(ServiceMeta serviceMeta) throws Exception {
        ServiceInstance<ServiceMeta> serviceInstance = ServiceInstance
                .<ServiceMeta>builder()
                .name(RpcServiceHelper.buildServiceKey(serviceMeta.getServiceName(), serviceMeta.getServiceVersion()))
                .address(serviceMeta.getServiceAddr())
                .port(serviceMeta.getServicePort())
                .payload(serviceMeta)
                .build();
        serviceDiscovery.registerService(serviceInstance);
    }

    @Override
    public void unRegister(ServiceMeta serviceMeta) throws Exception {
        ServiceInstance<ServiceMeta> serviceInstance = ServiceInstance
                .<ServiceMeta>builder()
                .name(RpcServiceHelper.buildServiceKey(serviceMeta.getServiceName(), serviceMeta.getServiceVersion()))
                .address(serviceMeta.getServiceAddr())
                .port(serviceMeta.getServicePort())
                .payload(serviceMeta)
                .build();
        serviceDiscovery.unregisterService(serviceInstance);
    }

    @Override
    public ServiceMeta discovery(String serviceName, int invokerHashCode) throws Exception {
        Collection<ServiceInstance<ServiceMeta>> serviceInstances = serviceDiscovery.queryForInstances(serviceName);
        //负载均衡
        ServiceInstance<ServiceMeta> instance = new ZKConsistentHashLoadBalancer().select((List<ServiceInstance<ServiceMeta>>) serviceInstances, invokerHashCode);
        if (instance != null) {
            return instance.getPayload();
        }
        return null;
    }

    @Override
    public void destroy() throws IOException {
        serviceDiscovery.close();
    }
}
