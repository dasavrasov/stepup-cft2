package ru.stepup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
public class LogTransformationBeanPostProcessor implements BeanPostProcessor {
    Map<String, String> beansWithLogFile = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(LogTransformation.class)) {
            LogTransformation annotation = bean.getClass().getAnnotation(LogTransformation.class);
            beansWithLogFile.put(beanName, annotation.logFile());
        }
        return bean;
    }

    private String getLogFile(Field field) {
        LogTransformation annotation = field.getAnnotation(LogTransformation.class);
        return annotation.logFile();
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        if (beansWithLogFile.containsKey(beanName)) {
            String logFile = beansWithLogFile.get(beanName);
            Logger logger = LoggerFactory.getLogger(bean.getClass());
            logger.info("Дата начала операции", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            logger.info("Компонент ", beanName);
            logger.info("Входящие данные ", bean.toString());
            System.out.println("Дата начала операции " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            System.out.println("Компонент " + beanName);
            System.out.println("Входящие данные " + bean.toString());
            beansWithLogFile.remove(beanName);
        }
        return bean;
    }
}