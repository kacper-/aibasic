package com.km.aibasic.interpreter.func;

import com.km.aibasic.interpreter.mem.Value;
import com.km.aibasic.interpreter.mem.ValueException;

public interface BuildIn {
    FuncName name();

    Value run(Value... values) throws ValueException;
}
