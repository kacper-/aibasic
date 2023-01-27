package com.km.aibasic.interpreter.statement;

import com.km.aibasic.interpreter.InterpreterException;
import com.km.aibasic.interpreter.Line;
import com.km.aibasic.interpreter.mem.Heap;
import com.km.aibasic.interpreter.mem.Type;
import com.km.aibasic.interpreter.mem.Value;
import com.km.aibasic.interpreter.mem.Variable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.km.aibasic.interpreter.Keyword.ARR;

public class Array extends Statement {
    private static final Pattern patternNoValue = createPatternNoValue();
    private Matcher mNoValue;
    private int size;
    private Type type;
    private String name;

    public Array() {
    }

    private static Pattern createPatternNoValue() {
        String types = Arrays.stream(Type.values()).map(Type::getName).collect(Collectors.joining("|"));
        String patternValue = "^\\s*" + ARR.val() + "\\s+(" + types + ")\\s*\\(\\s*([1-9][0-9]*)\\s*\\)\\s+([a-zA-Z]\\w*)\\s*$";
        return Pattern.compile(patternValue);
    }

    @Override
    public void execute() throws InterpreterException {
        List<Value> list = new ArrayList<>();
        Variable variable;
        for (int i = 0; i < size; i++) {
            list.add(new Value(type));
        }
        variable = new Variable(name, new Value(list, type));
        heap.putVariable(name, variable);
    }

    @Override
    public boolean test(Line line) {
        mNoValue = patternNoValue.matcher(line.getContent());
        return mNoValue.find();
    }

    @Override
    public boolean digest(Line line) throws InterpreterException {
        originalCode = Collections.singletonList(line);
        validateNoValue(line);
        validateName(name);
        return false;
    }

    private void validateNoValue(Line line) throws InterpreterException {
        type = validateType(mNoValue.group(1), line);
        size = Integer.parseInt(mNoValue.group(2));
        name = mNoValue.group(3);
    }

    @Override
    public Statement create(Heap heap) {
        Array array = new Array();
        array.heap = heap;
        return array;
    }
}
