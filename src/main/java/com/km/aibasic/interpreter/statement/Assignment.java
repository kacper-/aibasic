package com.km.aibasic.interpreter.statement;

import com.km.aibasic.interpreter.InterpreterException;
import com.km.aibasic.interpreter.Line;
import com.km.aibasic.interpreter.Messages;
import com.km.aibasic.interpreter.mem.Heap;
import com.km.aibasic.interpreter.mem.ValueException;
import com.km.aibasic.interpreter.mem.Variable;

import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Assignment extends Statement {
    private static final Pattern patternVariable = createPatternVariable();
    private static final Pattern patternArray = createPatternArray();
    private Matcher variable;
    private Matcher array;
    private String name;
    private Expression expression;
    private int index;
    private boolean isArray;

    private static Pattern createPatternVariable() {
        String patternValue = "^\\s*(" + PT_VAR_NAME + ")\\s*=\\s*(" + PT_EXPR + ")\\s*$";
        return Pattern.compile(patternValue);
    }

    private static Pattern createPatternArray() {
        String patternValue = "^\\s*(" + PT_VAR_NAME + ")\\s*\\(\\s*(" + PT_EXPR + ")\\s*\\)\\s*=\\s*(" + PT_EXPR + ")\\s*$";
        return Pattern.compile(patternValue);
    }

    @Override
    public void execute() throws InterpreterException {
        Variable variable = heap.getVariable(name);
        if (variable == null) {
            throw createException(Messages.LINE_ERROR, originalCode.get(0), String.format("No variable with name %s", name));
        }
        expression.execute();
        try {
            if (index < 0) {
                variable.getValue().set(expression.getValue().cast(variable.getValue().getType()).get());
            } else {
                variable.getValue().getList().get(index).set(expression.getValue().cast(variable.getValue().getType()).get());
            }
        } catch (ValueException e) {
            throw createException(Messages.LINE_ERROR, originalCode.get(0), e.getMsg());
        }
    }

    @Override
    public boolean test(Line line) {
        variable = patternVariable.matcher(line.getContent());
        array = patternArray.matcher(line.getContent());
        isArray = array.find();
        return variable.find() || isArray;
    }

    @Override
    public boolean digest(Line line) throws InterpreterException {
        originalCode = Collections.singletonList(line);
        if (isArray) {
            if (array.groupCount() == 3) {
                validateArray(array);
                return false;
            }
        } else {
            if (variable.groupCount() == 2) {
                validate(variable);
                return false;
            }
        }
        throw createException(Messages.LINE_ERROR, line, null);
    }

    private void createExpression(String s) throws InterpreterException {
        expression = new Expression();
        expression.setHeap(heap);
        Line expLine = new Line(originalCode.get(0).getNumber(), s);
        if (!expression.test(expLine)) {
            throw expression.createException(Messages.LINE_ERROR, originalCode.get(0), null);
        }
        expression.digest(expLine);
    }

    private void validateArray(Matcher m) throws InterpreterException {
        name = m.group(1);
        index = Integer.parseInt(m.group(2));
        createExpression(m.group(3));
    }

    private void validate(Matcher m) throws InterpreterException {
        name = m.group(1);
        index = -1;
        createExpression(m.group(2));
    }

    @Override
    public Statement create(Heap heap) {
        Assignment assignment = new Assignment();
        assignment.heap = heap;
        return assignment;
    }
}
