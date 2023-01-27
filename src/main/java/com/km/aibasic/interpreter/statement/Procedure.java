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

import static com.km.aibasic.interpreter.Keyword.END;
import static com.km.aibasic.interpreter.Keyword.PROCEDURE;

public class Procedure extends Statement {
    private static final String SEPARATOR = " ";
    private static final String PARAM_SEPARATOR = ",";
    private static final Pattern patternHeader = createPatternHeader();
    private static final Pattern patternEnd = createPatternEnd();
    protected final List<Line> body = new ArrayList<>();
    protected final List<Variable> params = new ArrayList<>();
    protected String name;
    protected Type returnType;
    private Matcher headerMatcher;
    private boolean firstLine = true;

    private static Pattern createPatternHeader() {
        String patternValue = "^\\s*" + PROCEDURE.val() + "\\s+(" + PT_TYPES + ")\\s+(" + PT_VAR_NAME + ")\\s*\\(\\s*(.+)*\\s*\\)\\s*$";
        return Pattern.compile(patternValue);
    }

    private static Pattern createPatternEnd() {
        String patternValue = "^\\s*" + END.val() + "\\s*$";
        return Pattern.compile(patternValue);
    }

    @Override
    public void execute() throws InterpreterException {
        Callable c = new Callable(name, body, returnType, params, originalCode.get(0));
        heap.putCallable(c.getName(), c);
    }

    @Override
    public boolean test(Line line) {
        headerMatcher = patternHeader.matcher(line.getContent());
        return headerMatcher.find();
    }

    @Override
    public boolean digest(Line line) throws InterpreterException {
        if (firstLine) {
            firstLine = false;
            digestHeader(line);
            return true;
        } else {
            return digestBody(line);
        }
    }

    private boolean digestBody(Line line) {
        boolean mEnd = patternEnd.matcher(line.getContent()).find();
        if (mEnd) {
            return false;
        }
        body.add(line);
        return true;
    }

    private void digestHeader(Line line) throws InterpreterException {
        originalCode = Collections.singletonList(line);
        validateHeader(line);
        validateName(name);
    }

    private void validateHeader(Line line) throws InterpreterException {
        returnType = validateType(headerMatcher.group(1), line);
        name = headerMatcher.group(2);
        validateParams(headerMatcher.group(3), line);
    }

    private void validateParams(String s, Line line) throws InterpreterException {
        if (s != null) {
            for (String p : s.split(PARAM_SEPARATOR)) {
                addParam(p, line);
            }
        }
    }

    private void addParam(String group, Line line) throws InterpreterException {
        if (group != null) {
            String[] s = group.trim().split(SEPARATOR);
            if (s.length != 2) {
                throw createException(Messages.ARGUMENT_ERROR, line, group);
            }
            params.add(new Variable(s[1], new Value(validateType(s[0], line))));
        }
    }

    @Override
    public Statement create(Heap heap) {
        Procedure procedure = new Procedure();
        procedure.heap = heap;
        return procedure;
    }
}
