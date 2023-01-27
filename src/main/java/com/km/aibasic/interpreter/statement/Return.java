package com.km.aibasic.interpreter.statement;

import com.km.aibasic.interpreter.InterpreterException;
import com.km.aibasic.interpreter.Keyword;
import com.km.aibasic.interpreter.Line;
import com.km.aibasic.interpreter.Messages;
import com.km.aibasic.interpreter.mem.Heap;
import com.km.aibasic.interpreter.mem.Variable;

import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.km.aibasic.interpreter.Keyword.RETURN;

public class Return extends Statement {
    public static final String RET_VAL = Keyword.RETURN.val();
    private static final Pattern patternWithValue = createPatternWithValue();
    private static final Pattern patternNoValue = createPatternNoValue();
    private Expression expression;
    private boolean valuePresent = false;

    private static Pattern createPatternWithValue() {
        String patternValue = "^\\s*" + RETURN.val() + "\\s+(" + PT_EXPR + ")\\s*$";
        return Pattern.compile(patternValue);
    }

    private static Pattern createPatternNoValue() {
        String patternValue = "^\\s*" + RETURN.val() + "\\s*$";
        return Pattern.compile(patternValue);
    }

    @Override
    public void execute() throws InterpreterException {
        broken = false;
        if (valuePresent) {
            expression.execute();
            Variable variable = createOnHeap(RET_VAL, expression.getValue().getType());
            variable.setValue(expression.getValue());
        }
        broken = true;
    }

    @Override
    public boolean test(Line line) {
        Matcher mNoValue = patternNoValue.matcher(line.getContent());
        Matcher mWithValue = patternWithValue.matcher(line.getContent());
        valuePresent = mWithValue.find();
        return mNoValue.find() || valuePresent;
    }

    @Override
    public boolean digest(Line line) throws InterpreterException {
        originalCode = Collections.singletonList(line);
        Matcher m = patternWithValue.matcher(line.getContent());
        if (!m.find() || m.groupCount() != 1) {
            m = patternNoValue.matcher(line.getContent());
            if (!m.find()) {
                throw createException(Messages.LINE_ERROR, line, null);
            }
            return false;
        }
        validateWithValue(m.group(1));
        return false;
    }

    private void validateWithValue(String exp) throws InterpreterException {
        expression = new Expression();
        expression.setHeap(heap);
        Line expLine = new Line(originalCode.get(0).getNumber(), exp);
        if (!expression.test(expLine)) {
            throw expression.createException(Messages.LINE_ERROR, originalCode.get(0), null);
        }
        expression.digest(expLine);
    }

    @Override
    public Statement create(Heap heap) {
        Return r = new Return();
        r.heap = heap;
        return r;
    }
}
