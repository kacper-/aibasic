package com.km.aibasic.interpreter.mem;

public class ValueException extends Exception {
    private final String msg;

    public ValueException(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
