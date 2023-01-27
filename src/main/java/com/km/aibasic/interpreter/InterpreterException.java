package com.km.aibasic.interpreter;

public class InterpreterException extends Exception {
    private final String line;
    private final String msg;
    private final int number;

    public InterpreterException(String msg, Line line) {
        super(String.format("%s in line : %d : %s", msg, line.getNumber(), line.getContent()));
        this.line = line.getContent();
        this.msg = msg;
        this.number = line.getNumber();
    }

    public String getLine() {
        return line;
    }

    public String getMsg() {
        return msg;
    }

    public int getNumber() {
        return number;
    }
}
