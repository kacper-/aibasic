package com.km.aibasic.interpreter.func.impl;

import com.km.aibasic.interpreter.func.BuildIn;
import com.km.aibasic.interpreter.func.FuncName;
import com.km.aibasic.interpreter.mem.Value;
import com.km.aibasic.interpreter.mem.ValueException;

public class Print implements BuildIn {
    @Override
    public FuncName name() {
        return FuncName.PRINT;
    }

    @Override
    public Value run(Value... values) throws ValueException {
        System.out.println(values[0].asString());
        return null;
    }
}
