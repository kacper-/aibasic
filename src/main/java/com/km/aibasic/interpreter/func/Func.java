package com.km.aibasic.interpreter.func;

import com.km.aibasic.interpreter.InterpreterException;
import com.km.aibasic.interpreter.func.impl.*;
import com.km.aibasic.interpreter.mem.*;

import java.util.HashMap;
import java.util.Map;

// TODO add IO functions
public class Func {
    private static final Map<FuncName, BuildIn> map = createMap();

    private static Map<FuncName, BuildIn> createMap() {
        BuildIn[] buildIn = new BuildIn[]{new Cos(), new Eq(), new Mod(), new Pow(), new Print(), new Sin(), new Sqrt(), new Tan()};
        Map<FuncName, BuildIn> map = new HashMap<>();
        for (BuildIn b : buildIn) {
            map.put(b.name(), b);
        }
        return map;
    }

    public static Value exec(String name, Heap heap, Value... values) throws FuncException, InterpreterException, ValueException {
        FuncName f = FuncName.fromName(name);
        if (f == null) {
            Callable c = heap.getCallable(name);
            if (c == null) {
                return callVariable(name, heap, values);
            }
            return call(c, values);
        } else {
            return exec(f, values);
        }
    }

    private static Value callVariable(String name, Heap heap, Value... values) throws FuncException, ValueException {
        Variable v = heap.getVariable(name);
        if (v == null || v.getValue() == null || !v.getValue().isArray()) {
            throw new FuncException("Unknown custom func or array " + name);
        } else {
            return call(v, values);
        }
    }

    public static Value call(Variable v, Value... values) throws FuncException, ValueException {
        if (values.length != 1 || values[0].getType() != Type.INT) {
            throw new FuncException("Bad array argument");
        }
        return v.getValue().getList().get(values[0].asInt());
    }

    public static Value call(Callable c, Value... values) throws FuncException, InterpreterException {
        String error = c.validateValues(values);
        if (error != null) {
            throw new FuncException(error);
        }
        return c.call(values);
    }

    public static Value exec(FuncName k, Value... values) throws FuncException, ValueException {
        BuildIn buildIn = map.get(k);
        if (buildIn != null) {
            return buildIn.run(values);
        } else {
            throw new FuncException("Unknown build-in func " + k);
        }
    }

    public static Value execWithArray(String name, Heap heap, Value values) throws FuncException, InterpreterException, ValueException {
        if (values.isArray()) {
            Value[] v = new Value[values.getList().size()];
            values.getList().toArray(v);
            return exec(name, heap, v);
        } else {
            return exec(name, heap, values);
        }
    }
}
