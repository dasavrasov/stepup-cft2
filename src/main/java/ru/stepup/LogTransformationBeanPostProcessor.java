package ru.stepup;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LogTransformationBeanPostProcessor implements BeanPostProcessor {
//    private static final Logger logger = LoggerFactory.getLogger(LogTransformationBeanPostProcessor.class);
    Map<String,Object> beans = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (Arrays.stream(bean.getClass().getDeclaredFields())
                .anyMatch(field -> field.isAnnotationPresent(LogTransformation.class)))
            beans.put(beanName, bean);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        if (beans.containsKey(beanName)) {
            long startTime = System.currentTimeMillis();
            System.out.println("Operation start time: {}" + startTime);
            System.out.println("Component class name: {}" + bean.getClass().getName());
            System.out.println("Input data: {}" + beans.get(beanName));
            System.out.println("Result data: {}" + bean);
            beans.remove(beanName);
        }

        return bean;
    }
}
