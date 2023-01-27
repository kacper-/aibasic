package com.km.aibasic.interpreter.statement;

import com.km.aibasic.interpreter.InterpreterTestBase;
import com.km.aibasic.interpreter.mem.Heap;
import com.km.aibasic.interpreter.mem.Type;
import com.km.aibasic.interpreter.mem.Variable;
import org.junit.Assert;
import org.junit.Test;

public class VarTest extends InterpreterTestBase {

    @Test
    public void testVar() throws Exception {
        Heap heap = executeProgram("Var.aib");
        Variable a = heap.getVariable("a");
        Variable b = heap.getVariable("b");
        Variable c = heap.getVariable("c");
        Variable d = heap.getVariable("d");
        Assert.assertNotNull(a);
        Assert.assertEquals((byte) 1, a.getValue().get());
        Assert.assertNotNull(b);
        Assert.assertEquals(2, b.getValue().get());
        Assert.assertNotNull(c);
        Assert.assertEquals(9, c.getValue().get());
        Assert.assertNotNull(d);
        Assert.assertEquals("abc", d.getValue().get());
    }

    @Test
    public void testEmptyVar() throws Exception {
        Heap heap = executeProgram("EmptyVar.aib");
        Variable b = heap.getVariable("b");
        Assert.assertNotNull(b);
        Assert.assertEquals(0, b.getValue().get());
    }

    @Test
    public void testAllTypes() throws Exception {
        Heap heap = executeProgram("AllTypes.aib");

        Variable a = heap.getVariable("a");
        Assert.assertNotNull(a);
        Assert.assertEquals(Type.BYTE, a.getValue().getType());
        Assert.assertEquals((byte) 1, a.getValue().get());

        Variable b = heap.getVariable("b");
        Assert.assertNotNull(b);
        Assert.assertEquals(Type.INT, b.getValue().getType());
        Assert.assertEquals(2, b.getValue().get());

        Variable c = heap.getVariable("c");
        Assert.assertNotNull(c);
        Assert.assertEquals(Type.BOOLEAN, c.getValue().getType());
        Assert.assertEquals(true, c.getValue().get());

        Variable d = heap.getVariable("d");
        Assert.assertNotNull(d);
        Assert.assertEquals(Type.FLOAT, d.getValue().getType());
        Assert.assertEquals(2.5d, (double) d.getValue().get(), 0.01d);

        Variable e = heap.getVariable("e");
        Assert.assertNotNull(e);
        Assert.assertEquals(Type.STR, e.getValue().getType());
        Assert.assertEquals("abc", e.getValue().get());
    }

    @Override
    protected String getType() {
        return "var";
    }
}