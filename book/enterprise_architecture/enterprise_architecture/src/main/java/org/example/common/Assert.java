package org.example.common;

public final class Assert {
    private Assert() {
    }

    public static void notNull(String message, Object nullableObj) {
        if (nullableObj == null) {
            throw new RuntimeException(message);
        }
    }

    public static void isTrue(String message, boolean b) {
        if (!b) {
            throw new RuntimeException(message);
        }
    }
}
