package ru.stepup;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
    private final List<Object> state=new ArrayList<>();

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
        System.out.println("CacheClearer started");
        cache.entrySet().removeIf(entry -> entry.getValue().getExpirationTime() <= currentTime);
    }
}

public class Utils {

    public static Object cache(Object target) {

        class CacheHandler implements InvocationHandler {
            Object target;
            boolean stateChanged = true;

            final Map<State, CacheValue> cache = new HashMap<>();
            final CacheClearer cacheClearer;
            final ScheduledExecutorService scheduler;

            public CacheHandler(Object target) {
                this.target = target;
                this.cacheClearer = new CacheClearer(cache);
                this.scheduler = Executors.newScheduledThreadPool(1);
                scheduler.scheduleAtFixedRate(cacheClearer, 500, 500, TimeUnit.MILLISECONDS);
            }

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Object result;

                Class clazz = target.getClass();
                Method method1 = clazz.getMethod(method.getName(), method.getParameterTypes());
                if (method1.isAnnotationPresent(Mutator.class)) {
                    stateChanged = true;
                }
                if (method1.isAnnotationPresent(Cache.class)) {
                    long expiration = method1.getAnnotation(Cache.class).expiration();
                    if (stateChanged) {
                        State newState = new State(target);
                        if (!cache.containsKey(newState)) {
                            result = method1.invoke(target, args);
                            cache.put(new State(target), new CacheValue(result, System.currentTimeMillis() + expiration));
                            stateChanged = false;
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