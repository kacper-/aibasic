package com.km.aibasic.interpreter.statement;

import com.km.aibasic.interpreter.InterpreterException;
import com.km.aibasic.interpreter.Line;
import com.km.aibasic.interpreter.mem.Heap;

import java.util.*;

public class Program extends Statement {
    private static final String NL = "\n";
    private static final String COMMENT = "#";
    private final List<Statement> list;

    public Program(List<Line> list, Heap heap) throws InterpreterException {
        this.heap = heap;
        this.list = createStatements(list);
    }

    public Program(String code, Heap heap) throws InterpreterException {
        this.heap = heap;
        this.list = createStatements(code);
    }

    @Override
    public void execute() throws InterpreterException {
        broken = false;
        for (Statement s : list) {
            s.execute();
            if (s.isBroken()) {
                broken = true;
                return;
            }
        }
    }

    private List<Statement> createStatements(String code) throws InterpreterException {
        Queue<Line> q = createQueue(code);
        List<Statement> list = new ArrayList<>();
        while (!q.isEmpty()) {
            list.add(StatementCreator.create(q, heap));
        }
        return list;
    }

    private List<Statement> createStatements(List<Line> lines) throws InterpreterException {
        Queue<Line> q = new LinkedList<>(lines);
        List<Statement> list = new ArrayList<>();
        while (!q.isEmpty()) {
            list.add(StatementCreator.create(q, heap));
        }
        return list;
    }

    private Queue<Line> createQueue(String code) {
        List<String> list = Arrays.asList(code.split(NL));
        Queue<Line> queue = new LinkedList<>();
        for (int i = 0; i < list.size(); i++) {
            String line = preprocessLine(list.get(i));
            if (line != null) {
                queue.add(new Line(i + 1, line));
            }
        }
        return queue;
    }

    private String preprocessLine(String line) {
        if (line == null)
            return null;
        if (line.trim().isEmpty())
            return null;
        if (line.trim().startsWith(COMMENT))
            return null;
        return line;
    }

    @Override
    public boolean test(Line line) {
        return false;
    }

    @Override
    public boolean digest(Line line) {
        return true;
    }

    @Override
    public Statement create(Heap heap) {
        return null;
    }

}
