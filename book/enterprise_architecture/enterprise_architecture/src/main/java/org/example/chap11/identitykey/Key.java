package org.example.chap11.identitykey;

import java.lang.instrument.IllegalClassFormatException;

public class Key {
    private Object[] fields;

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Key)) return false;
        Key otherKey = (Key) obj;
        if (this.fields.length != otherKey.fields.length) return false;

        for (int i = 0; i < this.fields.length; i++) {
            if (!this.fields[i].equals(otherKey.fields[i])) return false;
        }

        return true;
    }

    public Key(Object[] key) {
        checkKeyNotNull(key);
        this.fields = key;
    }

    public Key(Object key, Object... keys) {
        this.fields = new Object[keys.length + 1];
        fields[0] = key;
        for (int i = 0; i < keys.length; i++) {
            fields[i + 1] = keys[i];
        }

        checkKeyNotNull(fields);
    }

    public <R> R getValue(int idx, Class<R> clz) {
        if (idx < 0 || idx >= this.fields.length) {
            throw new IndexOutOfBoundsException("");
        }

        if (fields[idx].getClass() != clz) {
          throw new IllegalStateException("");
        }

        return clz.cast(fields[idx]);
    }

    private void checkKeyNotNull(Object[] fields) {
        if (fields == null) throw new IllegalArgumentException("Cannot have a null key");

        for (int i = 0; i < fields.length; i++) {
            if (fields[i] == null) {
                throw new IllegalArgumentException("Cannot have a null element of key");
            }
        }
    }
}
