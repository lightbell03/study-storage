package org.example.chap11.identitykey;

public class Key {
    private Object[] fields;

    public Key(Object[] fields) {
        checkKeNonnull(fields);

        this.fields = fields;
    }

    private void checkKeNonnull(Object[] fields) {
        if (fields == null) {
            throw new IllegalArgumentException("Cannot have a null key");
        }

        for (int i=0; i<fields.length; i++) {
            if (fields[i] == null) {
                throw new IllegalArgumentException("Cannot have a null element of key");
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof  Key)) {
            return false;
        }
        Key otherKey = (Key) obj;
        if (this.fields.length != otherKey.fields.length) {
            return false;
        }
        for (int i=0; i<fields.length; i++) {
            if (!this.fields[i].equals(otherKey.fields[i])) {
                return false;
            }
        }

        return true;
    }
}
