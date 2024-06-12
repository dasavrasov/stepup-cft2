package ru.stepup;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.stream.Collectors;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface Mutator {
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface Cache {
}

class State {
    private final List<Object> state=new ArrayList<>();

    public State(Object target) {
        Field[] fields= target.getClass().getDeclaredFields();

        state.clear();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                state.add(field.get(target));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state1 = (State) o;
        return Objects.equals(state, state1.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(state);
    }
}

public class Utils {

    public static Object cache(Object target) {

        class CacheHandler implements InvocationHandler {
            Object target;
            boolean stateChanged = true;

            final Map<State, Object> cache = new HashMap<>();

            public CacheHandler(Object target) {
                this.target = target;
            }

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Object result;

                Class clazz = target.getClass();
                Method method1 = clazz.getMethod(method.getName(), method.getParameterTypes());
                if (method1.isAnnotationPresent(Mutator.class)) {
                    State newState = new State(target);
                    if (!cache.containsKey(newState)) {
                        stateChanged = true;
                    }
                }
                if (method1.isAnnotationPresent(Cache.class)) {
                    if (cache.isEmpty() || stateChanged) {
                        result = method1.invoke(target, args);
                        cache.put(new State(target), result);
                        stateChanged = false;
                        return result;
                    }
                    result = cache.get(new State(target));
                    if (result != null) {
                        return result;
                    }
                }
                return method1.invoke(target, args);
            }
        }

        return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), new CacheHandler(target));
    }
}