package com.km.aibasic.interpreter.statement;

import com.km.aibasic.interpreter.InterpreterTestBase;
import com.km.aibasic.interpreter.mem.Heap;
import com.km.aibasic.interpreter.mem.Variable;
import org.junit.Assert;
import org.junit.Test;

public class ProcedureTest extends InterpreterTestBase {

    @Test
    public void testNoParams() throws Exception {
        Heap heap = executeProgram("NoParams.aib");
        Variable a = heap.getVariable("a");

        Assert.assertNotNull(a);
        Assert.assertEquals("1", a.getValue().get());
    }

    @Test
    public void testOneParam() throws Exception {
        Heap heap = executeProgram("OneParam.aib");
        Variable a = heap.getVariable("a");

        Assert.assertNotNull(a);
        Assert.assertEquals(2, a.getValue().get());
    }

    @Test
    public void testTwoParams() throws Exception {
        Heap heap = executeProgram("TwoParams.aib");
        Variable a = heap.getVariable("a");

        Assert.assertNotNull(a);
        Assert.assertEquals(5, a.getValue().get());
    }

    @Override
    protected String getType() {
        return "procedure";
    }
}