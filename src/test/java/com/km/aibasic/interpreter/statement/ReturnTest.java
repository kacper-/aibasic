package com.km.aibasic.interpreter.statement;

import com.km.aibasic.interpreter.InterpreterTestBase;
import com.km.aibasic.interpreter.mem.Heap;
import com.km.aibasic.interpreter.mem.Variable;
import org.junit.Assert;
import org.junit.Test;

public class ReturnTest extends InterpreterTestBase {

    @Test
    public void testOneParam() throws Exception {
        Heap heap = executeProgram("Int.aib");
        Variable a = heap.getVariable("a");

        Assert.assertNotNull(a);
        Assert.assertEquals(2, a.getValue().get());
    }

    @Override
    protected String getType() {
        return "return";
    }
}