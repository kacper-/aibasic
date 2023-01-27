package com.km.aibasic.interpreter.eval;

import com.km.aibasic.interpreter.InterpreterException;
import com.km.aibasic.interpreter.Line;
import com.km.aibasic.interpreter.Messages;
import com.km.aibasic.interpreter.func.Func;
import com.km.aibasic.interpreter.func.FuncException;
import com.km.aibasic.interpreter.func.FuncName;
import com.km.aibasic.interpreter.mem.*;

import java.util.ArrayList;
import java.util.List;

// TODO solve mod(i,j) = 0 evaluator issue
public class Evaluator {
    private final Line line;
    private final Heap heap;
    private String str;
    private int pos = -1, ch;

    public Evaluator(Heap heap, Line line) {
        this.line = line;
        this.heap = heap;
    }

    private void nextChar() {
        ch = (++pos < str.length()) ? str.charAt(pos) : -1;
    }

    private boolean eat(int charToEat) {
        while (ch == ' ') nextChar();
        if (ch == charToEat) {
            nextChar();
            return true;
        }
        return false;
    }

    private Value parseExpression() throws InterpreterException, ValueException, FuncException {
        Value x = parseTerm();
        for (; ; ) {
            if (eat('+')) {
                x = x.add(parseTerm());
            } else {
                if (eat('-')) {
                    x = x.deduct(parseTerm());
                } else {
                    return x;
                }
            }
        }
    }

    private Value parseTerm() throws InterpreterException, ValueException, FuncException {
        Value x = parseFactor();
        for (; ; ) {
            if (eat('*')) {
                x = x.multiply(parseFactor());
            } else {
                if (eat('/')) {
                    x = x.divide(parseFactor());
                } else {
                    return x;
                }
            }
        }
    }

    private Value parseFactor() throws InterpreterException, ValueException, FuncException {
        if (eat('+'))
            return parseFactor();
        if (eat('-'))
            return parseFactor().negate();
        return parseOperators(parseElements());
    }

    private Value parseElements() throws InterpreterException, FuncException, ValueException {
        Value x;
        int startPos = this.pos;
        if (eat('(')) {
            x = parseBrackets();
        } else {
            if (eat('"')) {
                x = parseString(startPos);
            } else {
                if (isDigit()) {
                    x = parseDigits(startPos);
                } else {
                    if (Character.isLetter(ch)) {
                        x = parseVariablesAndFunctions(startPos);
                    } else {
                        throw new InterpreterException(Messages.UNEXPECTED + (char) ch, line);
                    }
                }
            }
        }
        return x;
    }

    private Value parseOperators(Value x) throws FuncException, InterpreterException, ValueException {
        if (eat('^'))
            x = Func.exec(FuncName.POW, x, parseFactor());
        if (eat('='))
            x = Func.exec(FuncName.EQ, x, parseFactor());
        return x;
    }

    private Value parseBrackets() throws FuncException, InterpreterException, ValueException {
        List<Value> list = new ArrayList<>();
        if (!eat(')')) {
            list.add(parseExpression());
            while (eat(',')) {
                list.add(parseExpression());
            }
            eat(')');
        }
        return new Value(list, Type.STR);
    }

    private Value parseString(int startPos) {
        while (ch != '"')
            nextChar();
        Value x = new Value(str.substring(startPos + 1, this.pos), Type.STR);
        eat('"');
        return x;
    }

    private Value parseDigits(int startPos) {
        while (isDigit())
            nextChar();
        return Value.fromString(str.substring(startPos, this.pos));
    }

    private Value parseVariablesAndFunctions(int startPos) throws FuncException, InterpreterException, ValueException {
        while (Character.isLetter(ch))
            nextChar();
        String func = str.substring(startPos, this.pos);
        Variable var = heap.getVariable(func);
        if (var != null) {
            return var.getValue();
        } else {
            return Func.execWithArray(func, heap, parseFactor());
        }
    }

    private boolean isDigit() {
        return Character.isDigit(ch) || ch == '.';
    }

    public Value eval() throws InterpreterException, ValueException, FuncException {
        str = line.getContent();
        pos = -1;
        nextChar();
        return parseExpression();
    }

}

