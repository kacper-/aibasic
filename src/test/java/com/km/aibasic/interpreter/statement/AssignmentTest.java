package com.km.aibasic.interpreter.statement;

import com.km.aibasic.interpreter.InterpreterTestBase;
import com.km.aibasic.interpreter.mem.Heap;
import com.km.aibasic.interpreter.mem.Type;
import com.km.aibasic.interpreter.mem.Value;
import com.km.aibasic.interpreter.mem.Variable;
import org.junit.Assert;
import org.junit.Test;

public class AssignmentTest extends InterpreterTestBase {

    @Test
    public void testCastToStr() throws Exception {
        Heap heap = executeProgram("CastToStr.aib");
        Variable sa = heap.getVariable("sa");
        Variable sb = heap.getVariable("sb");
        Variable sc = heap.getVariable("sc");
        Variable sd = heap.getVariable("sd");
        Assert.assertNotNull(sa);
        Assert.assertEquals("1", sa.getValue().get());

        Assert.assertNotNull(sb);
        Assert.assertEquals("2", sb.getValue().get());

        Assert.assertNotNull(sc);
        Assert.assertEquals("false", sc.getValue().get());

        Assert.assertNotNull(sd);
        Assert.assertEquals("2.5", sd.getValue().get());
    }

    @Test
    public void testCastToInt() throws Exception {
        Heap heap = executeProgram("CastToInt.aib");
        Variable sa = heap.getVariable("sa");
        Variable sb = heap.getVariable("sb");
        Variable sc = heap.getVariable("sc");
        Variable sd = heap.getVariable("sd");
        Assert.assertNotNull(sa);
        Assert.assertEquals(1, sa.getValue().get());

        Assert.assertNotNull(sb);
        Assert.assertEquals(2, sb.getValue().get());

        Assert.assertNotNull(sc);
        Assert.assertEquals(0, sc.getValue().get());

        Assert.assertNotNull(sd);
        Assert.assertEquals(3, sd.getValue().get());
    }

    @Test
    public void testCastToByte() throws Exception {
        Heap heap = executeProgram("CastToByte.aib");
        Variable sa = heap.getVariable("sa");
        Variable sb = heap.getVariable("sb");
        Variable sc = heap.getVariable("sc");
        Variable sd = heap.getVariable("sd");
        Assert.assertNotNull(sa);
        Assert.assertEquals((byte) 1, sa.getValue().get());

        Assert.assertNotNull(sb);
        Assert.assertEquals((byte) 2, sb.getValue().get());

        Assert.assertNotNull(sc);
        Assert.assertEquals((byte) 0, sc.getValue().get());

        Assert.assertNotNull(sd);
        Assert.assertEquals((byte) 3, sd.getValue().get());
    }

    @Test
    public void testCastToBool() throws Exception {
        Heap heap = executeProgram("CastToBool.aib");

        Variable sa = heap.getVariable("sa");
        Variable sb = heap.getVariable("sb");
        Variable sc = heap.getVariable("sc");
        Variable sd = heap.getVariable("sd");

        Variable ssa = heap.getVariable("ssa");
        Variable ssb = heap.getVariable("ssb");
        Variable ssc = heap.getVariable("ssc");
        Variable ssd = heap.getVariable("ssd");

        Assert.assertNotNull(sa);
        Assert.assertEquals(new Value(true, Type.BOOLEAN), sa.getValue());

        Assert.assertNotNull(sb);
        Assert.assertEquals(new Value(true, Type.BOOLEAN), sb.getValue());

        Assert.assertNotNull(sc);
        Assert.assertEquals(new Value(true, Type.BOOLEAN), sc.getValue());

        Assert.assertNotNull(sd);
        Assert.assertEquals(new Value(true, Type.BOOLEAN), sd.getValue());

        Assert.assertNotNull(ssa);
        Assert.assertEquals(new Value(false, Type.BOOLEAN), ssa.getValue());

        Assert.assertNotNull(ssb);
        Assert.assertEquals(new Value(false, Type.BOOLEAN), ssb.getValue());

        Assert.assertNotNull(ssc);
        Assert.assertEquals(new Value(false, Type.BOOLEAN), ssc.getValue());

        Assert.assertNotNull(ssd);
        Assert.assertEquals(new Value(false, Type.BOOLEAN), ssd.getValue());
    }

    @Test
    public void testCastToFloat() throws Exception {
        Heap heap = executeProgram("CastToFloat.aib");
        Variable sa = heap.getVariable("sa");
        Variable sb = heap.getVariable("sb");
        Variable sc = heap.getVariable("sc");
        Variable sd = heap.getVariable("sd");
        Assert.assertNotNull(sa);
        Assert.assertEquals(1d, sa.getValue().asDouble(), 0.01d);

        Assert.assertNotNull(sb);
        Assert.assertEquals(2.5d, sb.getValue().asDouble(), 0.01d);

        Assert.assertNotNull(sc);
        Assert.assertEquals(0d, sc.getValue().asDouble(), 0.01d);

        Assert.assertNotNull(sd);
        Assert.assertEquals(3d, sd.getValue().asDouble(), 0.01d);
    }

    @Override
    protected String getType() {
        return "assignment";
    }
}