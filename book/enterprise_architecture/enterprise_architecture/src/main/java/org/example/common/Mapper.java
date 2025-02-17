package org.example.common;

public class Mapper<T, ID> {
    private final Class<T> clz;

    public Mapper(Class<T> clz) {
        this.clz = clz;
    }

    public void insert(Object obj) {

    }

    public void update(Object obj) {

    }

    public void delete(Object obj) {

    }

    public T find(ID id) {
        return null;
    }
}
