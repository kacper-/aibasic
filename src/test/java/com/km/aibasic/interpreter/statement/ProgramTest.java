package com.km.aibasic.interpreter.statement;

import com.km.aibasic.interpreter.InterpreterTestBase;
import com.km.aibasic.interpreter.mem.Heap;
import com.km.aibasic.interpreter.mem.Variable;
import org.junit.Assert;
import org.junit.Test;

public class ProgramTest extends InterpreterTestBase {

    @Test
    public void testNoParams() throws Exception {
        Heap heap = executeProgram("SimpleProgram.aib");
        Variable a = heap.getVariable("a");

        Assert.assertNotNull(a);
        Assert.assertEquals(11, a.getValue().get());
    }

    @Override
    protected String getType() {
        return "program";
    }
}