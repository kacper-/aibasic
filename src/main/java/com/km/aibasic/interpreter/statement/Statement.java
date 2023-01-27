package com.km.aibasic.interpreter.statement;

import com.km.aibasic.interpreter.InterpreterException;
import com.km.aibasic.interpreter.Keyword;
import com.km.aibasic.interpreter.Line;
import com.km.aibasic.interpreter.Messages;
import com.km.aibasic.interpreter.func.FuncName;
import com.km.aibasic.interpreter.mem.Heap;
import com.km.aibasic.interpreter.mem.Type;
import com.km.aibasic.interpreter.mem.Value;
import com.km.aibasic.interpreter.mem.Variable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Statement {
    public static final String PT_VAR_NAME = "[a-zA-Z]\\w*";
    public static final String PT_EXPR = ".+";
    public static final String PT_TYPES = Arrays.stream(Type.values()).map(Type::getName).collect(Collectors.joining("|"));
    protected List<Line> originalCode = new ArrayList<>();
    protected Heap heap;
    protected boolean broken = false;

    public abstract void execute() throws InterpreterException;

    public abstract boolean test(Line line);

    public abstract boolean digest(Line line) throws InterpreterException;

    public abstract Statement create(Heap heap);

    public InterpreterException createException(String msg, Line line, String additionalMsg) {
        if (additionalMsg == null || additionalMsg.isEmpty()) {
            return new InterpreterException(msg, line);
        } else {
            return new InterpreterException(msg + " : " + additionalMsg, line);
        }
    }

    public Heap getHeap() {
        return heap;
    }

    public void setHeap(Heap heap) {
        this.heap = heap;
    }

    protected Type validateType(String s, Line line) throws InterpreterException {
        Type type = Type.fromName(s);
        if (type == null) {
            throw createException(Messages.UNKNOWN_TYPE, line, null);
        }
        return type;
    }

    protected void validateName(String name) throws InterpreterException {
        if (Keyword.fromName(name) != null || FuncName.fromName(name) != null) {
            throw createException(String.format(Messages.IS_RESTRICTED, name), originalCode.get(0), null);
        }
    }

    protected Variable createOnHeap(String name, Type type) {
        Variable variable = heap.getVariable(name);
        if (variable == null) {
            variable = new Variable(name, new Value(type));
            heap.putVariable(name, variable);
        }
        return variable;
    }

    public boolean isBroken() {
        return broken;
    }
}
