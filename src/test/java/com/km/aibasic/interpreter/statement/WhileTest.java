package com.km.aibasic.interpreter.statement;

import com.km.aibasic.interpreter.InterpreterTestBase;
import com.km.aibasic.interpreter.mem.Heap;
import com.km.aibasic.interpreter.mem.Variable;
import org.junit.Assert;
import org.junit.Test;

public class WhileTest extends InterpreterTestBase {

    @Test
    public void testEmptyVar() throws Exception {
        Heap heap = executeProgram("SimpleWhile.aib");
        Variable a = heap.getVariable("a");
        Assert.assertNotNull(a);
        Assert.assertEquals(2, a.getValue().get());

        Variable b = heap.getVariable("b");
        Assert.assertNotNull(b);
        Assert.assertEquals(2, b.getValue().get());
    }

    @Override
    protected String getType() {
        return "while";
    }
}
