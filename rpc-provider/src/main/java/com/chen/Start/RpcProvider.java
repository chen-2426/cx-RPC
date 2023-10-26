package com.chen.Start;

import com.chen.Annotation.RpcService;
import com.chen.RegistryService;
import com.chen.common.RpcServiceHelper;
import com.chen.common.ServiceMeta;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/10/25 18:34
 * @description
 */

@Slf4j
public class RpcProvider implements InitializingBean, BeanPostProcessor {
    private String serverAddress;
    private final int serverPort;
    private final RegistryService serviceRegistry;
    private final Map<String ,Object> rpcServiceMap = new HashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        new Thread(() -> {
            try {
                startRpcServer();
            } catch (Exception e) {
                log.error("start rpc server error.", e);
            }
        }).start();
    }

    public RpcProvider(int serverPort, RegistryService serviceRegistry) {
        this.serverPort = serverPort;
        this.serviceRegistry = serviceRegistry;
    }

    private void startRpcServer() throws UnknownHostException, InterruptedException { //throws Exception
        this.serverAddress = InetAddress.getLocalHost().getHostAddress();
        NioEventLoopGroup boss = new NioEventLoopGroup(); //主reactor 负责接收请求并将channel注册到从reactor上
        NioEventLoopGroup worker = new NioEventLoopGroup(); // 从reactor 负责进行IO


        try {
            ServerBootstrap bootstrap = new ServerBootstrap();  //初始化引导器
            bootstrap.group(boss, worker) // 绑定reactor
                    .channel(NioServerSocketChannel.class) //初始化channel
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //自定义业务处理器


                            //利用channel的pipline注册handler

                        }
                    })
                    .childOption(ChannelOption.SO_KEEPALIVE,true);
            ChannelFuture channelFuture = bootstrap.bind(this.serverAddress, this.serverPort).sync();
            log.info("server addr {} started on port{}",this.serverAddress,this.serverPort);
            channelFuture.channel().closeFuture().sync();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }

    }

    //把bean实例传递bean后置处理器的方法
    //扫描@RpcService注解
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        //检查注解
        RpcService rpcService = bean.getClass().getAnnotation(RpcService.class);
        if(rpcService!=null){
            String serviceName = rpcService.serviceInterface().getName();
            String serviceVersion = rpcService.serviceInterface().getName();
            //封装为元数据
            try{
                ServiceMeta serviceMeta = new ServiceMeta();
                serviceMeta.setServiceAddr(serverAddress);
                serviceMeta.setServicePort(serverPort);
                serviceMeta.setServiceName(serviceName);
                serviceMeta.setServiceVersion(serviceVersion);
                // 发布服务元数据至注册中心
                rpcServiceMap.put(RpcServiceHelper.buildServiceKey(serviceMeta.getServiceName(),serviceMeta.getServiceVersion()),bean);
            }catch (Exception e){
                log.error("failed to register service {}#{}",serviceName,serviceVersion,e);
            }
        }
        return bean;
    }

}
