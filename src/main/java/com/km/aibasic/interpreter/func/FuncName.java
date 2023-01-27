package com.km.aibasic.interpreter.func;

public enum FuncName {
    SQRT,
    COS,
    SIN,
    TAN,
    POW,
    MOD,
    PRINT,
    EQ;

    public static FuncName fromName(String name) {
        for (FuncName f : values()) {
            if (f.name().equalsIgnoreCase(name)) {
                return f;
            }
        }
        return null;
    }
}
