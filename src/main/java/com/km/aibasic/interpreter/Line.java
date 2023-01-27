package com.km.aibasic.interpreter;

public class Line {
    private final int number;
    private final String content;

    public Line(int number, String content) {
        this.number = number;
        this.content = content;
    }

    public int getNumber() {
        return number;
    }

    public String getContent() {
        return content;
    }
}
