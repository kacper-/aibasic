package com.km.aibasic.interpreter.func.impl;

import com.km.aibasic.interpreter.func.BuildIn;
import com.km.aibasic.interpreter.func.FuncName;
import com.km.aibasic.interpreter.mem.Type;
import com.km.aibasic.interpreter.mem.Value;
import com.km.aibasic.interpreter.mem.ValueException;

public class Sin implements BuildIn {
    @Override
    public FuncName name() {
        return FuncName.SIN;
    }

    @Override
    public Value run(Value... values) throws ValueException {
        return new Value(Math.sin(Math.toRadians(values[0].asDouble())), Type.FLOAT);
    }
}
