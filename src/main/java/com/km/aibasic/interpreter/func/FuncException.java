package com.km.aibasic.interpreter.func;

public class FuncException extends Exception {
    private final String msg;

    public FuncException(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
