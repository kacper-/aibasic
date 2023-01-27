package com.km.aibasic.interpreter.statement;

import com.km.aibasic.interpreter.InterpreterTestBase;
import com.km.aibasic.interpreter.mem.Heap;
import com.km.aibasic.interpreter.mem.Variable;
import org.junit.Assert;
import org.junit.Test;

public class ExpressionTest extends InterpreterTestBase {

    @Test
    public void testBuildInFunc() throws Exception {
        Heap heap = executeProgram("BuildInFunc.aib");

        Variable a = heap.getVariable("a");
        Variable b = heap.getVariable("b");
        Variable c = heap.getVariable("c");
        Variable d = heap.getVariable("d");
        Variable e = heap.getVariable("e");
        Variable f = heap.getVariable("f");

        Assert.assertNotNull(a);
        Assert.assertEquals(2, a.getValue().get());

        Assert.assertNotNull(b);
        Assert.assertEquals(0.5, b.getValue().get());

        Assert.assertNotNull(c);
        Assert.assertEquals(0.866, c.getValue().asDouble(), 0.001);

        Assert.assertNotNull(d);
        Assert.assertEquals(1.0, d.getValue().get());

        Assert.assertNotNull(e);
        Assert.assertEquals(8, e.getValue().get());

        Assert.assertNotNull(f);
        Assert.assertEquals(true, f.getValue().get());
    }

    @Test
    public void testOperators() throws Exception {
        Heap heap = executeProgram("Operators.aib");

        Variable a = heap.getVariable("a");
        Variable b = heap.getVariable("b");
        Variable c = heap.getVariable("c");
        Variable d = heap.getVariable("d");
        Variable e = heap.getVariable("e");
        Variable f = heap.getVariable("f");

        Assert.assertNotNull(a);
        Assert.assertEquals(7, a.getValue().get());

        Assert.assertNotNull(b);
        Assert.assertEquals(1, b.getValue().get());

        Assert.assertNotNull(c);
        Assert.assertEquals(0.5, c.getValue().asDouble(), 0.001);

        Assert.assertNotNull(d);
        Assert.assertEquals(20, d.getValue().get());

        Assert.assertNotNull(e);
        Assert.assertEquals(8, e.getValue().get());

        Assert.assertNotNull(f);
        Assert.assertEquals(true, f.getValue().get());
    }

    @Override
    protected String getType() {
        return "expression";
    }
}