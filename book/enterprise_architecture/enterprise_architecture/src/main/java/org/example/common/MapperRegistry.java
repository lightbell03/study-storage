package org.example.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MapperRegistry {
    private static Map<Class<? extends Mapper>, Mapper> registry = new ConcurrentHashMap<>();

    public static <T extends Mapper> void putMapper(T mapper) {
        registry.put(mapper.getClass(), mapper);
    }


    public static <T extends Mapper> T getMapper(Class<T> clz) {
        return (T) registry.get(clz);
    }
}
