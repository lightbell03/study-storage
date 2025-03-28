package org.example.common;

import java.net.IDN;

public class MapperRegistry {

    public static <T, ID> Mapper<T, ID> getMapper(Class<T> clz, Class<ID> idClz) {
        return new Mapper<>(clz);
    }
}
