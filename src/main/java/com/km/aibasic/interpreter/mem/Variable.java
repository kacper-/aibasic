package com.km.aibasic.interpreter.mem;

import java.util.stream.Collectors;

public class Variable {
    private final String name;
    private Value value;

    public Variable(String name, Value value) {
        this.value = value;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    @Override
    public String toString() {
        if (value.get() == null) {
            return String.format("%s %s = null", value.getType().getName(), name);
        }
        if (getValue().isArray()) {
            return getArray();
        } else {
            return getNonArray();
        }
    }

    private String getArray() {
        return String.format("Array of %s = [%s]", value.getType().getName(), getValue().getList().stream().map(v -> {
            try {
                return v.asString();
            } catch (ValueException e) {
                e.printStackTrace();
                return "";
            }
        }).collect(Collectors.joining(",")));
    }

    private String getNonArray() {
        switch (value.getType()) {
            case FLOAT:
                return String.format("%s %s = %sd", value.getType().getName(), name, value.get().toString());
            case INT:
                return String.format("%s %s = %s", value.getType().getName(), name, value.get().toString());
            case BYTE:
                return String.format("%s %s = %sb", value.getType().getName(), name, value.get().toString());
            case BOOLEAN:
                return String.format("%s %s = .%s", value.getType().getName(), name, value.get().toString());
            case STR:
                return String.format("%s %s = \"%s\"", value.getType().getName(), name, value.get().toString());
            default:
                return "*** Unknown variable type ***";
        }
    }
}
