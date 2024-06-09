package ru.stepup;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface Mutator {
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface Cache {
}

public class Utils {

    public static Object cache(Object target) {

        class CacheHandler implements InvocationHandler {
            Object target;

            final Map<Method, Object> cache = new HashMap<>();

            public CacheHandler(Object target) {
                this.target = target;
            }

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Object result;

                Class clazz = target.getClass();
                Method method1 = clazz.getMethod(method.getName(), method.getParameterTypes());
                if (method1.isAnnotationPresent(Mutator.class)) {
//                    System.out.println(method.getName()+" помечен аннотацией Mutator - сбрасываем кэш");
                    cache.clear(); // Сбросить кеш при наличии аннотации @Mutator
                }
                //проверка @Cache
                if (method1.isAnnotationPresent(Cache.class)) {
//                    System.out.println(method.getName() + " помечен аннотацией Cache");
                    if (!cache.containsKey(method1)) {
//                        System.out.println("Первый вызов - запись в кэш " + method.getName());
                        result = method1.invoke(target, args); //первый вызов - выполняем
                        cache.put(method1, result); //запись в кэш
                        return result;
                    }
                    result = cache.get(method1); //поиск в кэше
                    if (result != null) {
//                        System.out.println("нашли - возвращаем из кэша " + method.getName()); //нашли - возвращаем из кэша
                        return result;
                    }
                }
                return method1.invoke(target, args); //не помечен @Cache либо нет в кеше- выполняем без кэша
            }
        }

        return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), new CacheHandler(target));
    }
}

