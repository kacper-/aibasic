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

import static com.km.aibasic.interpreter.Keyword.DO;
import static com.km.aibasic.interpreter.Keyword.WHILE;

public class While extends Statement {
    private static final Pattern patternStart = createPatternStart();
    private static final Pattern patternEnd = createPatternEnd();
    private final List<Line> action = new ArrayList<>();
    private Expression expression;
    private boolean firstLine = true;
    private int nestLevel = 0;

    private static Pattern createPatternStart() {
        String patternValue = "^\\s*" + WHILE.val() + "\\s+(" + PT_EXPR + ")\\s*$";
        return Pattern.compile(patternValue);
    }

    private static Pattern createPatternEnd() {
        String patternValue = "^\\s*" + DO.val() + "\\s*$";
        return Pattern.compile(patternValue);
    }

    @Override
    public void execute() throws InterpreterException {
        broken = false;
        try {
            Program program = new Program(action, heap);
            while (getExprValue()) {
                program.execute();
                if (program.isBroken()) {
                    broken = true;
                    return;
                }
            }
        } catch (ValueException e) {
            throw createException(Messages.LINE_ERROR, originalCode.get(0), e.getMsg());
        }
    }

    private boolean getExprValue() throws InterpreterException, ValueException {
        expression.execute();
        return expression.getValue().asBoolean();
    }

    @Override
    public boolean test(Line line) {
        boolean start = patternStart.matcher(line.getContent()).find();
        boolean end = patternEnd.matcher(line.getContent()).find();
        return start ^ end;
    }

    @Override
    public boolean digest(Line line) throws InterpreterException {
        if (firstLine) {
            firstLine = false;
            expression = new Expression();
            expression.setHeap(heap);
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
        Matcher m = patternStart.matcher(line.getContent());
        if (!m.find() || m.groupCount() != 1) {
            throw createException(Messages.LINE_ERROR, line, null);
        }
        validateFirst(m);
    }

    private void validateFirst(Matcher m) throws InterpreterException {
        Line expLine = new Line(originalCode.get(0).getNumber(), m.group(1));
        if (!expression.test(expLine)) {
            throw expression.createException(Messages.LINE_ERROR, originalCode.get(0), null);
        }
        expression.digest(expLine);
    }

    @Override
    public Statement create(Heap heap) {
        While w = new While();
        w.heap = heap;
        return w;
    }
}
