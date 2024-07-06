package ru.stepup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
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
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (beansWithLogFile.containsKey(beanName)) {
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(bean.getClass());
            enhancer.setCallback((MethodInterceptor) (obj, method, args, proxy) -> {
                String logFile = beansWithLogFile.get(beanName);
                try (FileWriter writer = new FileWriter(logFile, true)) {
                    writer.write("Дата начала операции: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\n");
                    writer.write("Компонент: " + beanName + "\n");
                    writer.write("Метод: " + method.getName() + "\n");
                    writer.write("Аргументы: " + Arrays.toString(args) + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return proxy.invokeSuper(obj, args);
            });
            return enhancer.create();
        }
        return bean;
    }
}