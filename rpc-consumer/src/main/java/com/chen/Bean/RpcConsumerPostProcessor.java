package com.chen.Bean;

import com.chen.Annotation.RpcReference;
import com.chen.common.RpcConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import sun.plugin.com.BeanClass;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/10/25 22:05
 * @description
 */
@Component
@Slf4j
public class RpcConsumerPostProcessor implements ApplicationContextAware, BeanClassLoaderAware, BeanFactoryPostProcessor {
    private ApplicationContext context;
    private ClassLoader classLoader;
    private final Map<String, BeanDefinition> rpcRefBeanDefinitions = new LinkedHashMap<>();

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        for (String beanDefinitionName : beanFactory.getBeanDefinitionNames()) {
            //获取存储的Bean的相关信息
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanDefinitionName);
            String beanClassName = beanDefinition.getBeanClassName();
            if(beanClassName!=null){
                //通过指定的classloader加载对应的类
                Class<?> clazz = ClassUtils.resolveClassName(beanClassName, classLoader);
                //对类的每个属性执行回调方法parseRpcReference;检测每个Bean的所有field;
                ReflectionUtils.doWithFields(clazz,this::parseRpcReference);
            }
        }
        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;
        this.rpcRefBeanDefinitions.forEach((beanName,beanDefinition)->{
                if(context.containsBean(beanName)){
                    throw new IllegalArgumentException(
                            "spring context already has a bean named"+beanName
                    );
                }
                registry.registerBeanDefinition(beanName,rpcRefBeanDefinitions.get(beanName));
            log.info("registered RpcReferenceBean {} success.", beanName);
        });
    }

    /**
     * 修改Bean的定义
     * @param field
     */
    private void parseRpcReference(Field field) {
        RpcReference annotation = AnnotationUtils.getAnnotation(field, RpcReference.class);
        if(annotation != null){
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(RpcReferenceBean.class);
            builder.setInitMethodName(RpcConstants.INIT_METHOD_NAME);
            builder.addPropertyValue("interfaceClass",field.getType());
            builder.addPropertyValue("serviceVersion",annotation.serviceVersion());
            builder.addPropertyValue("registryType",annotation.registryType());
            builder.addPropertyValue("registryAddr",annotation.registryAddress());
            builder.addPropertyValue("timeout",annotation.timeout());
            AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
            rpcRefBeanDefinitions.put(field.getName(),beanDefinition);
        }

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

}
