package com.km.aibasic.interpreter.statement;

import com.km.aibasic.interpreter.InterpreterException;
import com.km.aibasic.interpreter.Line;
import com.km.aibasic.interpreter.Messages;
import com.km.aibasic.interpreter.mem.Heap;

import java.util.Queue;

public class StatementCreator {
    private static final Statement[] TYPES = new Statement[]{new Var(), new Array(), new Assignment(), new Condition(), new While(), new For(), new Procedure(), new Return(), new Expression()};

    public static Statement create(Queue<Line> q, Heap heap) throws InterpreterException {
        Statement statement = null;
        Line line = q.remove();
        for (Statement t : TYPES) {
            Statement test = t.create(heap);
            if (test.test(line)) {
                while (test.digest(line)) {
                    if (q.isEmpty()) {
                        throw new InterpreterException(Messages.PREMATURE_END, line);
                    }
                    line = q.remove();
                }
                statement = test;
                break;
            }
        }
        if (statement == null) {
            throw new InterpreterException(Messages.UNKNOWN_LINE, line);
        }
        return statement;
    }

}
