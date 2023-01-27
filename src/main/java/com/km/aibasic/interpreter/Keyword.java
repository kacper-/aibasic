package com.km.aibasic.interpreter;

public enum Keyword {
    IF,
    THEN,
    ELSE,
    ENDIF,
    FOR,
    FROM,
    TO,
    NEXT,
    WHILE,
    DO,
    PROCEDURE,
    END,
    VAR,
    ARR,
    RETURN;

    public static Keyword fromName(String name) {
        for (Keyword f : values()) {
            if (f.name().equalsIgnoreCase(name)) {
                return f;
            }
        }
        return null;
    }

    public String val() {
        return name().toLowerCase();
    }
}
