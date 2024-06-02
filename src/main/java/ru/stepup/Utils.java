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
            private final Object target;
            final Map<Method, Object> cache = new HashMap<>();

            public CacheHandler(Object target) {
                this.target = target;
            }

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Object result;
                System.out.println("Вызов метода " +  method.getName());
                //проверка @Mutator
                if (method.isAnnotationPresent(Mutator.class)) {
                    System.out.println(method.getName()+" помечен аннотацией Mutator - сбрасываем кэш");
                    cache.clear(); // Сбросить кеш при наличии аннотации @Mutator
                }
                //проверка @Cache
                if (method.isAnnotationPresent(Cache.class)) {
                    System.out.println(method.getName()+" помечен аннотацией Cache");
                    if (cache.isEmpty()) {
                        System.out.println("Первый вызов - запись в кэш " + method.getName());
                        result=method.invoke(target, args); //первый вызов - выполняем
                        cache.put(method, result); //запись в кэш
                        return result;
                    }
                    result = cache.get(method); //поиск в кэше
                    if (result != null) {
                        System.out.println("нашли - возвращаем из кэша " + method.getName()); //нашли - возвращаем из кэша
                        return result;
                    } else {
                        System.out.println("не нашли - выполняем метод без кэширования");
                        result = method.invoke(target, args); //не нашли - выполняем метод без кэширования
                        cache.put(method, result); //запись в кэш
                        return result;
                    }
                }
                else {
                    System.out.println(method.getName()+" не помечен аннотацией Cache - выполняем метод без кэширования");
                    return method.invoke(target, args); //не помечен аннотацией Cache - выполняем метод без кэширования
                }
            }
        }

        return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(),
                new CacheHandler(target));
    }
}
