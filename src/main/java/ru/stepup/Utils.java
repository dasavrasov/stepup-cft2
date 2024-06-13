package ru.stepup;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.*;
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
                        stateChanged = true;
                }
                if (method1.isAnnotationPresent(Cache.class)) {
                    if (stateChanged) {
                        State newState = new State(target);
                        if (!cache.containsKey(newState)) {
                            result = method1.invoke(target, args);
                            cache.put(new State(target), result);
                            stateChanged = false;
                            return result;
                        }
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