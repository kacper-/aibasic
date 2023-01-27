package com.km.aibasic.interpreter.statement;

import com.km.aibasic.interpreter.InterpreterException;
import com.km.aibasic.interpreter.Keyword;
import com.km.aibasic.interpreter.Line;
import com.km.aibasic.interpreter.Messages;
import com.km.aibasic.interpreter.eval.Evaluator;
import com.km.aibasic.interpreter.func.FuncException;
import com.km.aibasic.interpreter.mem.Heap;
import com.km.aibasic.interpreter.mem.Value;
import com.km.aibasic.interpreter.mem.ValueException;

import java.util.Collections;

public class Expression extends Statement {
    protected Value value;
    private Evaluator evaluator;

    @Override
    public void execute() throws InterpreterException {
        try {
            value = evaluator.eval();
        } catch (ValueException e) {
            throw createException(Messages.LINE_ERROR, originalCode.get(0), e.getMsg());
        } catch (FuncException e) {
            throw createException(Messages.LINE_ERROR, originalCode.get(0), e.getMsg());
        }
    }

    public Value getValue() {
        return value;
    }

    @Override
    public boolean test(Line line) {
        originalCode = Collections.singletonList(line);
        for (Keyword k : Keyword.values()) {
            if (line.getContent().trim().startsWith(k.val())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean digest(Line line) {
        originalCode = Collections.singletonList(line);
        evaluator = new Evaluator(heap, originalCode.get(0));
        return false;
    }

    @Override
    public Statement create(Heap heap) {
        Expression expression = new Expression();
        expression.heap = heap;
        return expression;
    }
}
