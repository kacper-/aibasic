package com.km.aibasic.interpreter.mem;

import java.util.HashMap;
import java.util.Map;

public class Heap {
    private final Map<String, Variable> variables;
    private final Map<String, Callable> callables;
    private HeapListener listener;

    private Heap() {
        variables = new HashMap<>();
        callables = new HashMap<>();
    }

    private Heap(HeapListener listener) {
        this.listener = listener;
        variables = new HashMap<>();
        callables = new HashMap<>();
    }

    public static Heap getPrepared() {
        return prepare(new Heap());
    }

    public static Heap getPrepared(HeapListener listener) {
        return prepare(new Heap(listener));
    }

    private static Heap prepare(Heap heap) {
        Variable t = new Variable("true", new Value(true, Type.BOOLEAN));
        heap.putVariable(t.getName(), t);
        Variable f = new Variable("false", new Value(false, Type.BOOLEAN));
        heap.putVariable(f.getName(), f);
        return heap;
    }

    public Variable getVariable(String key) {
        return variables.get(key);
    }

    public Callable getCallable(String key) {
        return callables.get(key);
    }

    public void putVariable(String key, Variable value) {
        if (variables.containsKey(key)) {
            throw new RuntimeException(String.format("Variable %s already exists", key));
        }
        variables.put(key, value);
        if (listener != null) {
            listener.listen(this);
        }
    }

    public void putCallable(String key, Callable value) {
        if (callables.containsKey(key)) {
            throw new RuntimeException(String.format("Callable %s already exists", key));
        }
        callables.put(key, value);
        if (listener != null) {
            listener.listen(this);
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("heap = {");
        builder.append("\n\tvariables :");
        for (String k : variables.keySet()) {
            builder.append("\n\t\t");
            builder.append(variables.get(k).toString());
        }
        builder.append("\n\tcallables :");
        for (String k : callables.keySet()) {
            builder.append("\n\t\t");
            builder.append(callables.get(k).toString());
        }
        builder.append("\n}");
        return builder.toString();
    }
}
