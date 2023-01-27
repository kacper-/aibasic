package com.km.aibasic.interpreter.statement;

import com.km.aibasic.interpreter.InterpreterTestBase;
import com.km.aibasic.interpreter.mem.Heap;
import com.km.aibasic.interpreter.mem.Variable;
import org.junit.Assert;
import org.junit.Test;

public class ForTest extends InterpreterTestBase {

    @Test
    public void testSimpleFor() throws Exception {
        Heap heap = executeProgram("SimpleFor.aib");
        Variable a = heap.getVariable("a");
        Assert.assertNotNull(a);
        Assert.assertEquals(5, a.getValue().get());

        Variable b = heap.getVariable("b");
        Assert.assertNotNull(b);
        Assert.assertEquals(11, b.getValue().get());
    }

    @Test
    public void testNestedFor() throws Exception {
        Heap heap = executeProgram("NestedFor.aib");
        Variable a = heap.getVariable("a");
        Assert.assertNotNull(a);
        Assert.assertEquals(31, a.getValue().get());
    }

    @Override
    protected String getType() {
        return "for";
    }
}