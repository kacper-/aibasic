package com.km.aibasic.interpreter.statement;

import com.km.aibasic.interpreter.InterpreterException;
import com.km.aibasic.interpreter.Line;
import com.km.aibasic.interpreter.Messages;
import com.km.aibasic.interpreter.mem.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.km.aibasic.interpreter.Keyword.*;

public class For extends Statement {
    private static final Pattern patternStart = createPatternStart();
    private static final Pattern patternEnd = createPatternEnd();
    private final List<Line> action = new ArrayList<>();
    private Matcher startMatcher;
    private Expression expressionFrom;
    private Expression expressionTo;
    private boolean firstLine = true;
    private int nestLevel = 0;
    private String name;

    private static Pattern createPatternStart() {
        String patternValue = "^\\s*" + FOR.val() + "\\s+(" + PT_VAR_NAME + ")\\s+" + FROM.val() + "\\s+(" + PT_EXPR + ")\\s+" + TO.val() + "\\s+(" + PT_EXPR + ")\\s*$";
        return Pattern.compile(patternValue);
    }

    private static Pattern createPatternEnd() {
        String patternValue = "^\\s*" + NEXT.val() + "\\s*$";
        return Pattern.compile(patternValue);
    }

    @Override
    public void execute() throws InterpreterException {
        expressionFrom.execute();
        expressionTo.execute();
        broken = false;
        Value from = expressionFrom.getValue();
        Value to = expressionTo.getValue();
        try {
            Variable variable = createOnHeap(name, Type.INT);
            variable.setValue(from);
            Program program = new Program(action, heap);
            while (variable.getValue().asInt() <= to.asInt()) {
                program.execute();
                if (program.isBroken()) {
                    broken = true;
                    return;
                }
                int val = variable.getValue().asInt();
                variable.getValue().set(val + 1);
            }
        } catch (ValueException e) {
            throw createException(Messages.LINE_ERROR, originalCode.get(0), e.getMsg());
        }
    }

    @Override
    public boolean test(Line line) {
        startMatcher = patternStart.matcher(line.getContent());
        boolean end = patternEnd.matcher(line.getContent()).find();
        return startMatcher.find() ^ end;
    }

    @Override
    public boolean digest(Line line) throws InterpreterException {
        if (firstLine) {
            firstLine = false;
            expressionFrom = new Expression();
            expressionFrom.setHeap(heap);
            expressionTo = new Expression();
            expressionTo.setHeap(heap);
            digestFirst(line);
            return true;
        } else {
            return digestNext(line);
        }
    }

    private boolean digestNext(Line line) {
        boolean mEnd = patternEnd.matcher(line.getContent()).find();
        boolean mStart = patternStart.matcher(line.getContent()).find();
        if (mStart) {
            nestLevel++;
        }
        if (mEnd) {
            nestLevel--;
        }
        if (mEnd && nestLevel < 0) {
            return false;
        }
        action.add(line);
        return true;
    }

    private void digestFirst(Line line) throws InterpreterException {
        originalCode = Collections.singletonList(line);
        validateFirst();
        validateName(name);
    }

    private void validateFirst() throws InterpreterException {
        name = startMatcher.group(1);
        Line expLineFrom = new Line(originalCode.get(0).getNumber(), startMatcher.group(2));
        if (!expressionFrom.test(expLineFrom)) {
            throw expressionFrom.createException(Messages.LINE_ERROR, originalCode.get(0), null);
        }
        expressionFrom.digest(expLineFrom);
        Line expLineTo = new Line(originalCode.get(0).getNumber(), startMatcher.group(3));
        if (!expressionTo.test(expLineTo)) {
            throw expressionTo.createException(Messages.LINE_ERROR, originalCode.get(0), null);
        }
        expressionTo.digest(expLineTo);
    }

    @Override
    public Statement create(Heap heap) {
        For f = new For();
        f.heap = heap;
        return f;
    }
}
