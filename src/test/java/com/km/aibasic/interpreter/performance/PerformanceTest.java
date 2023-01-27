package com.km.aibasic.interpreter.performance;

import com.km.aibasic.interpreter.InterpreterTestBase;
import com.km.aibasic.interpreter.mem.Heap;
import com.km.aibasic.interpreter.mem.Variable;
import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;

// TODO analyze performance bottlenecks
public class PerformanceTest extends InterpreterTestBase {
    @Test
    public void testSimpleFor() throws Exception {
        long start = Calendar.getInstance().getTimeInMillis();
        Heap heap = executeProgram("Primes.aib", false);
        long stop = Calendar.getInstance().getTimeInMillis();
        System.out.printf("Execution time = %d%n", stop - start);

        Variable count = heap.getVariable("count");
        Assert.assertNotNull(count);
        Assert.assertEquals(1229, count.getValue().get());
    }

    @Override
    protected String getType() {
        return "performance";
    }
}
