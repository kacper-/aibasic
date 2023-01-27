package com.km.aibasic.interpreter.func;

import com.km.aibasic.interpreter.mem.Type;
import com.km.aibasic.interpreter.mem.Value;
import com.km.aibasic.interpreter.mem.ValueException;
import org.junit.Assert;
import org.junit.Test;

public class FuncTest {

    @Test
    public void exec() {
        try {
            Object result = Func.exec(FuncName.PRINT, new Value("test", Type.STR));
            Assert.assertNull(result);
        } catch (FuncException | ValueException e) {
            Assert.fail();
        }
    }
}