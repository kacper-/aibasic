package com.km.aibasic.interpreter.mem;

public enum Type {
    BOOLEAN("bool"),
    BYTE("byte"),
    INT("int"),
    STR("str"),
    FLOAT("float");

    private final String name;

    Type(String name) {
        this.name = name;
    }

    public static Type fromName(String s) {
        for (Type v : values()) {
            if (v.name.equals(s)) {
                return v;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }
}
