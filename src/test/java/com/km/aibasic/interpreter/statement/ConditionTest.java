package com.km.aibasic.interpreter.statement;

import com.km.aibasic.interpreter.InterpreterTestBase;
import com.km.aibasic.interpreter.mem.Heap;
import com.km.aibasic.interpreter.mem.Variable;
import org.junit.Assert;
import org.junit.Test;

public class ConditionTest extends InterpreterTestBase {

    @Test
    public void testIf() throws Exception {
        Heap heap = executeProgram("If.aib");
        Variable a = heap.getVariable("a");
        Variable b = heap.getVariable("b");
        Variable c = heap.getVariable("c");
        Variable d = heap.getVariable("d");
        Variable e = heap.getVariable("e");
        Variable f = heap.getVariable("f");
        Assert.assertNotNull(a);
        Assert.assertNotNull(b);
        Assert.assertNotNull(c);
        Assert.assertNotNull(d);
        Assert.assertNotNull(e);
        Assert.assertNotNull(f);
        Assert.assertEquals(2, a.getValue().asInt());
        Assert.assertEquals(3, b.getValue().asInt());
        Assert.assertEquals(2, c.getValue().asInt());
        Assert.assertEquals(3, d.getValue().asInt());
        Assert.assertEquals(2, e.getValue().asInt());
        Assert.assertEquals(5, f.getValue().asInt());
    }

    @Test
    public void testNestedIf() throws Exception {
        Heap heap = executeProgram("NestedIf.aib");
        Variable a = heap.getVariable("a");
        Assert.assertNotNull(a);
        Assert.assertEquals(3, a.getValue().asInt());
    }

    @Override
    protected String getType() {
        return "condition";
    }
}