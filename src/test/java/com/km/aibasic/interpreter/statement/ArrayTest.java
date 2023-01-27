package com.km.aibasic.interpreter.statement;

import com.km.aibasic.interpreter.InterpreterTestBase;
import com.km.aibasic.interpreter.mem.Heap;
import com.km.aibasic.interpreter.mem.Type;
import com.km.aibasic.interpreter.mem.Variable;
import org.junit.Assert;
import org.junit.Test;

public class ArrayTest extends InterpreterTestBase {
    @Test
    public void testAssign() throws Exception {
        Heap heap = executeProgram("Assign.aib");

        Variable a = heap.getVariable("a");
        Assert.assertNotNull(a);
        Assert.assertEquals(Type.INT, a.getValue().getType());
        Assert.assertEquals(3, a.getValue().getList().size());
    }

    @Test
    public void testSet() throws Exception {
        Heap heap = executeProgram("Set.aib");

        Variable a = heap.getVariable("a");
        Assert.assertNotNull(a);
        Assert.assertEquals(Type.INT, a.getValue().getType());
        Assert.assertEquals(0, a.getValue().getList().get(0).get());
        Assert.assertEquals(1, a.getValue().getList().get(1).get());
        Assert.assertEquals(2, a.getValue().getList().get(2).get());
    }

    @Test
    public void testBadName() {
        try {
            executeProgram("BadName.aib");
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage().contains("for is restricted name"));
        }
    }

    @Override
    protected String getType() {
        return "array";
    }
}
