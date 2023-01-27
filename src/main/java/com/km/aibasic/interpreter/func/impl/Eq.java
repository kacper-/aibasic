package com.km.aibasic.interpreter.func.impl;

import com.km.aibasic.interpreter.func.BuildIn;
import com.km.aibasic.interpreter.func.FuncName;
import com.km.aibasic.interpreter.mem.Type;
import com.km.aibasic.interpreter.mem.Value;

public class Eq implements BuildIn {
    @Override
    public FuncName name() {
        return FuncName.EQ;
    }

    @Override
    public Value run(Value... values) {
        if (values[0].equals(values[1])) {
            return new Value(true, Type.BOOLEAN);
        } else {
            return new Value(false, Type.BOOLEAN);
        }
    }
}
