package org.example.chap10.datamapper;

public class DomainObject<K, D> {
    private K id;
    private D object;

    public void setId(K id) {
        this.id = id;
    }

    public K getId() {
        return id;
    }

    public D getObject() {
        return object;
    }
}
