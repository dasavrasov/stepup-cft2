package ru.stepup;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface Mutator {
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface Cache {
    long expiration() default 0;
}

class State {
    private final List<Object> state=new CopyOnWriteArrayList<>();

    public List<Object> getState() {
        return state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state1 = (State) o;
        //сравнить 2 листа по значениям
        if (state.size()!=state1.getState().size())
            return false;
        for(int i=0;i<state.size();i++){
            if (!state.get(i).equals(state1.getState().get(i)))
                return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(state);
    }

    public State(Object target) {
        Field[] fields= target.getClass().getDeclaredFields();

        state.clear();
        for (Field field : fields) {
            field.setAccessible(true);
            if (Modifier.isStatic(field.getModifiers()))
                continue; //статческие поля нельзя кэшировать
            try {
                state.add(field.get(target));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

    }

}

class CacheValue {
    private final Object value;
    private final long expirationTime;

    public CacheValue(Object value, long expirationTime) {
        this.value = value;
        this.expirationTime = expirationTime;
    }

    public Object getValue() {
        return value;
    }

    public long getExpirationTime() {
        return expirationTime;
    }
}

class CacheClearer implements Runnable {
    private final Map<State, CacheValue> cache;

    public CacheClearer(Map<State, CacheValue> cache) {
        this.cache = cache;
    }

    @Override
    public void run() {
        long currentTime = System.currentTimeMillis();
        long cSize=cache.size();
//        System.out.println("CacheClearer started with size="+cSize);
        cache.entrySet().removeIf(entry -> {
//            System.out.println("Expiration time: " + entry.getValue().getExpirationTime() + ", Current time: " + currentTime);
            if (entry.getValue().getExpirationTime() <= currentTime) {
                return true;
            }
            return false;
        });
//        cSize=cache.size();
//        System.out.println("CacheClearer ended with size="+cSize);
    }
}

public class Utils {

    public static Object cache(Object target) {
        class CacheHandler implements InvocationHandler {
            Object target;
            AtomicBoolean stateChanged = new AtomicBoolean(true);

            final Map<State, CacheValue> cache = new ConcurrentHashMap<>();
            final CacheClearer cacheClearer;
            final ExecutorService scheduler;

            private static final int CACHE_SIZE_THRESHOLD = 2; //размер кеша при которм запускать чистку

            public CacheHandler(Object target) {
                this.target = target;
                this.cacheClearer = new CacheClearer(cache);
                this.scheduler = Executors.newSingleThreadExecutor();
            }

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Object result;

                Class clazz = target.getClass();
                Method method1 = clazz.getMethod(method.getName(), method.getParameterTypes());
                if (method1.isAnnotationPresent(Mutator.class)) {
                    stateChanged.set(true);
                }
                if (method1.isAnnotationPresent(Cache.class)) {
                    if (cache.size() >= CACHE_SIZE_THRESHOLD) {
                        scheduler.submit(cacheClearer); // запускаем чистку кеша
                    }
                    long expiration = method1.getAnnotation(Cache.class).expiration();
                    if (stateChanged.get()) {
                        State newState = new State(target);
                        if (!cache.containsKey(newState)) {
                            result = method1.invoke(target, args);
                            cache.putIfAbsent(new State(target), new CacheValue(result, System.currentTimeMillis() + expiration));
                            stateChanged.set(false);
                            return result;
                        }
                    }
                    CacheValue cacheValue = cache.get(new State(target));
                    if (cacheValue != null) {
                        return cacheValue.getValue();
                    }
                }
                return method1.invoke(target, args);
            }
        }

        return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), new CacheHandler(target));
    }

}