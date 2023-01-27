package com.km.aibasic.interpreter.func.impl;

import com.km.aibasic.interpreter.func.BuildIn;
import com.km.aibasic.interpreter.func.FuncName;
import com.km.aibasic.interpreter.mem.Type;
import com.km.aibasic.interpreter.mem.Value;
import com.km.aibasic.interpreter.mem.ValueException;

public class Mod implements BuildIn {
    @Override
    public FuncName name() {
        return FuncName.MOD;
    }

    @Override
    public Value run(Value... values) throws ValueException {
        return new Value(values[0].asInt() % values[1].asInt(), Type.INT);
    }
}
