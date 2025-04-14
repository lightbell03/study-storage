package org.example.chap11.identitykey;

public class Key {
    private Object[] fields;

    public Key(Object[] fields) {
        checkNonNull(fields);

        this.fields = fields;
    }

    public Key(Object field, Object... fields) {
        checkNonNull(field);
        checkNonNull(fields);

        this.fields = new Object[fields.length + 1];

        this.fields[0] = field;
        for (int i = 0; i < fields.length; i++) {
            fields[i + 1] = fields[i];
        }
    }

    private void checkNonNull(Object field) {
        if (field == null) {
            throw new IllegalArgumentException("Cannot have a null key");
        }
    }

    private void checkNonNull(Object[] fields) {
        checkNonNull(fields);

        for (int i = 0; i < fields.length; i++) {
            checkNonNull(fields[i]);
        }
    }

    public Object getField(int index) {
        if (index < 0 || index >= fields.length) {
            throw new IndexOutOfBoundsException("index under or exceed, length" + fields.length + ", index = " + index);
        }

        return fields[index];
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Key)) {
            return false;
        }
        Key otherKey = (Key) obj;
        if (this.fields.length != otherKey.fields.length) {
            return false;
        }
        for (int i = 0; i < fields.length; i++) {
            if (!this.fields[i].equals(otherKey.fields[i])) {
                return false;
            }
        }

        return true;
    }
}
