package com.km.aibasic.interpreter.mem;

import org.junit.Assert;
import org.junit.Test;

public class ValueTest {
    @Test
    public void testInit() {
        Value value = new Value(Type.STR);
        Assert.assertEquals("", value.get());

        value = new Value(Type.INT);
        Assert.assertEquals(0, value.get());

        value = new Value(Type.FLOAT);
        Assert.assertEquals(0d, value.get());

        value = new Value(Type.BOOLEAN);
        Assert.assertEquals(false, value.get());

        value = new Value(Type.BYTE);
        Assert.assertEquals((byte) 0, value.get());
    }
}