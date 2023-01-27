package com.km.aibasic.interpreter.statement;

import com.km.aibasic.interpreter.InterpreterException;
import com.km.aibasic.interpreter.Line;
import com.km.aibasic.interpreter.Messages;
import com.km.aibasic.interpreter.mem.*;

import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.km.aibasic.interpreter.Keyword.VAR;

public class Var extends Statement {
    private static final Pattern patternWithValue = createPatternWithValue();
    private static final Pattern patternNoValue = createPatternNoValue();
    private Matcher mNoValue;
    private Matcher mWithValue;
    private Type type;
    private String name;
    private Expression expression;
    private boolean isWithValue;

    public Var() {
    }

    private static Pattern createPatternWithValue() {
        String patternValue = "^\\s*" + VAR.val() + "\\s+(" + PT_TYPES + ")\\s+(" + PT_VAR_NAME + ")\\s*=\\s*(" + PT_EXPR + ")\\s*$";
        return Pattern.compile(patternValue);
    }

    private static Pattern createPatternNoValue() {
        String patternValue = "^\\s*" + VAR.val() + "\\s+(" + PT_TYPES + ")\\s+(" + PT_VAR_NAME + ")\\s*$";
        return Pattern.compile(patternValue);
    }

    @Override
    public void execute() throws InterpreterException {
        Variable variable = createOnHeap(name, type);
        try {
            if (expression != null) {
                expression.execute();
                variable.setValue(expression.getValue().cast(type));
            } else {
                variable.setValue(new Value(type));
            }
        } catch (ValueException e) {
            throw createException(e.getMsg(), originalCode.get(0), null);
        }
    }

    @Override
    public boolean test(Line line) {
        mNoValue = patternNoValue.matcher(line.getContent());
        mWithValue = patternWithValue.matcher(line.getContent());
        isWithValue = mWithValue.find();
        return mNoValue.find() || isWithValue;
    }

    @Override
    public boolean digest(Line line) throws InterpreterException {
        originalCode = Collections.singletonList(line);
        expression = new Expression();
        expression.setHeap(heap);
        if (!isWithValue) {
            validateNoValue(line);
            return false;
        }
        validateWithValue(line);
        validateName(name);
        return false;
    }

    private void validateNoValue(Line line) throws InterpreterException {
        if (mNoValue.groupCount() != 2) {
            throw createException(Messages.LINE_ERROR, line, null);
        }
        type = validateType(mNoValue.group(1), line);
        name = mNoValue.group(2);
        expression = null;
    }

    @Override
    public Statement create(Heap heap) {
        Var var = new Var();
        var.heap = heap;
        return var;
    }

    private void validateWithValue(Line line) throws InterpreterException {
        if (mWithValue.groupCount() != 3) {
            throw createException(Messages.LINE_ERROR, line, null);
        }
        type = Type.fromName(mWithValue.group(1));
        if (type == null) {
            throw createException(Messages.UNKNOWN_TYPE, line, null);
        }
        name = mWithValue.group(2);
        Line expLine = new Line(originalCode.get(0).getNumber(), mWithValue.group(3));
        if (!expression.test(expLine)) {
            throw expression.createException(Messages.LINE_ERROR, originalCode.get(0), null);
        }
        expression.digest(expLine);
    }
}
