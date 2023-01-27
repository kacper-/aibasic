package com.km.aibasic.interpreter.mem;

import com.km.aibasic.interpreter.InterpreterException;
import com.km.aibasic.interpreter.Line;
import com.km.aibasic.interpreter.Messages;
import com.km.aibasic.interpreter.statement.Program;
import com.km.aibasic.interpreter.statement.Return;

import java.util.List;

public class Callable {
    protected final Type returnType;
    protected final List<Variable> params;
    protected final Line line;
    private final String name;
    private final List<Line> body;
    private final Program program;

    public Callable(String name, List<Line> body, Type returnType, List<Variable> params, Line line) throws InterpreterException {
        this.name = name;
        this.body = body;
        this.returnType = returnType;
        this.params = params;
        this.line = line;
        this.program = new Program(body, Heap.getPrepared());
        addParams();
    }

    public String getName() {
        return name;
    }

    public Value call(Value... values) throws InterpreterException {
        prepareHeap(values);
        program.execute();
        return prepareValue();
    }

    private Value prepareValue() throws InterpreterException {
        Variable r = program.getHeap().getVariable(Return.RET_VAL);
        if (r == null) {
            throw new InterpreterException(Messages.NO_RETURN_VALUE, line);
        }
        if (r.getValue().getType() != returnType) {
            throw new InterpreterException(Messages.WRONG_RETURN_TYPE, line);
        }
        return r.getValue();
    }

    private void addParams() {
        for (int i = 0; i < params.size(); i++) {
            Variable v = new Variable(params.get(i).getName(), params.get(i).getValue());
            program.getHeap().putVariable(v.getName(), v);
        }
    }

    private void prepareHeap(Value... values) {
        for (int i = 0; i < values.length; i++) {
            Variable v = program.getHeap().getVariable(params.get(i).getName());
            v.getValue().set(values[i].get());
        }
    }

    public String validateValues(Value[] values) {
        if (values.length != params.size()) {
            return Messages.WRONG_NUMBER_OF_PARAMS;
        }
        for (int i = 0; i < values.length; i++) {
            if (values[i].getType() != params.get(i).getValue().getType()) {
                return Messages.WRONG_PARAM_TYPE;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return line.getContent();
    }
}
