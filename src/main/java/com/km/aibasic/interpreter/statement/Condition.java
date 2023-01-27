package com.km.aibasic.interpreter.statement;

import com.km.aibasic.interpreter.InterpreterException;
import com.km.aibasic.interpreter.Line;
import com.km.aibasic.interpreter.Messages;
import com.km.aibasic.interpreter.mem.Heap;
import com.km.aibasic.interpreter.mem.ValueException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.km.aibasic.interpreter.Keyword.*;

public class Condition extends Statement {
    private static final Pattern patternSingle = createPatternSingle();
    private static final Pattern patternSingleElse = createPatternSingleElse();
    private static final Pattern patternMulti = createPatternMulti();
    private static final Pattern patternMultiElse = createPatternMultiElse();
    private static final Pattern patternMultiEnd = createPatternMultiEnd();
    private final List<Line> action = new ArrayList<>();
    private final List<Line> actionElse = new ArrayList<>();
    private Expression expression;
    private boolean firstLine = true;
    private boolean alt = false;
    private boolean multi = false;
    private boolean withElse = false;
    private int nestLevel = 0;

    private static Pattern createPatternSingle() {
        String patternValue = "^\\s*" + IF.val() + "\\s+(" + PT_EXPR + ")\\s+" + THEN.val() + "\\s+(" + PT_EXPR + ")\\s*$";
        return Pattern.compile(patternValue);
    }

    private static Pattern createPatternSingleElse() {
        String patternValue = "^\\s*" + IF.val() + "\\s+(" + PT_EXPR + ")\\s+" + THEN.val() + "\\s+(" + PT_EXPR + ")\\s+" + ELSE.val() + "\\s+(" + PT_EXPR + ")\\s*$";
        return Pattern.compile(patternValue);
    }

    private static Pattern createPatternMulti() {
        String patternValue = "^\\s*" + IF.val() + "\\s+(" + PT_EXPR + ")\\s+" + THEN.val() + "\\s*$";
        return Pattern.compile(patternValue);
    }

    private static Pattern createPatternMultiElse() {
        String patternValue = "^\\s*" + ELSE.val() + "\\s*$";
        return Pattern.compile(patternValue);
    }

    private static Pattern createPatternMultiEnd() {
        String patternValue = "^\\s*" + ENDIF.val() + "\\s*$";
        return Pattern.compile(patternValue);
    }

    @Override
    public void execute() throws InterpreterException {
        expression.execute();
        broken = false;
        try {
            Program program;
            if (expression.getValue().asBoolean()) {
                program = new Program(action, heap);
            } else {
                program = new Program(actionElse, heap);
            }
            program.execute();
            if (program.isBroken()) {
                broken = true;
            }
        } catch (ValueException e) {
            throw createException(Messages.LINE_ERROR, originalCode.get(0), e.getMsg());
        }
    }

    @Override
    public boolean test(Line line) {
        boolean m = patternSingle.matcher(line.getContent()).find();
        boolean mElse = patternSingleElse.matcher(line.getContent()).find();
        boolean mMulti = patternMulti.matcher(line.getContent()).find();
        if (m || mElse || mMulti) {

            multi = mMulti;
            withElse = mElse;
            return true;
        }
        return false;
    }

    @Override
    public boolean digest(Line line) throws InterpreterException {
        if (firstLine) {
            firstLine = false;
            expression = new Expression();
            expression.setHeap(heap);
            if (multi) {
                digestFirst(line);
            } else {
                digestSingle(line);
            }
            return multi;
        } else {
            return digestNext(line);
        }
    }

    private void digestSingle(Line line) throws InterpreterException {
        originalCode = Collections.singletonList(line);
        Matcher m;
        int groups;
        if (withElse) {
            m = patternSingleElse.matcher(line.getContent());
            groups = 3;
        } else {
            m = patternSingle.matcher(line.getContent());
            groups = 2;
        }
        if (!m.find() || m.groupCount() != groups) {
            throw createException(Messages.LINE_ERROR, line, null);
        }
        validateSingle(m, line);
    }

    private void validateSingle(Matcher m, Line line) throws InterpreterException {
        Line expLine = new Line(originalCode.get(0).getNumber(), m.group(1));
        if (!expression.test(expLine)) {
            throw expression.createException(Messages.LINE_ERROR, originalCode.get(0), null);
        }
        expression.digest(expLine);
        action.add(new Line(line.getNumber(), m.group(2)));
        if (withElse) {
            actionElse.add(new Line(line.getNumber(), m.group(3)));
        }
    }

    private void digestFirst(Line line) throws InterpreterException {
        originalCode = Collections.singletonList(line);
        Matcher m = patternMulti.matcher(line.getContent());
        if (!m.find() || m.groupCount() != 1) {
            throw createException(Messages.LINE_ERROR, line, null);
        }
        validateMultiFirst(m);
    }

    private void validateMultiFirst(Matcher m) throws InterpreterException {
        Line expLine = new Line(originalCode.get(0).getNumber(), m.group(1));
        if (!expression.test(expLine)) {
            throw expression.createException(Messages.LINE_ERROR, originalCode.get(0), null);
        }
        expression.digest(expLine);
    }

    private boolean digestNext(Line line) {
        boolean mEnd = patternMultiEnd.matcher(line.getContent()).find();
        boolean mElse = patternMultiElse.matcher(line.getContent()).find();
        boolean mIf = patternMulti.matcher(line.getContent()).find();
        if (mEnd) {
            nestLevel--;
        }
        if (mIf) {
            nestLevel++;
        }
        if (mElse && nestLevel == 0) {
            alt = true;
            return true;
        }
        if (mEnd && nestLevel < 0) {
            return false;
        }
        if (alt) {
            actionElse.add(line);
        } else {
            action.add(line);
        }
        return true;
    }

    @Override
    public Statement create(Heap heap) {
        Condition condition = new Condition();
        condition.heap = heap;
        return condition;
    }

}
