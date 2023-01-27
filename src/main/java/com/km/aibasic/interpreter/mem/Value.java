package com.km.aibasic.interpreter.mem;

import java.util.List;
import java.util.Objects;

import static com.km.aibasic.interpreter.Messages.CASTING_ERROR;

public class Value {
    private static final String CAST = "Cannot cast %s to %s";
    private static final String ARRAY = "This is an array";
    private static final String UNKNOWN = "Unknown type %s";
    private final Type type;
    private final List<Value> valueList;
    private final boolean array;
    private Object value;

    public Value(Object value, Type type) {
        array = false;
        this.type = type;
        this.value = value;
        this.valueList = null;
    }

    public Value(List<Value> value, Type type) {
        array = true;
        this.type = type;
        this.value = null;
        this.valueList = value;
    }

    public Value(Type type) {
        array = false;
        this.type = type;
        this.value = initByType(type);
        this.valueList = null;
    }

    public static Object initByType(Type type) {
        switch (type) {
            case INT:
                return 0;
            case STR:
                return "";
            case BYTE:
                return (byte) 0;
            case FLOAT:
                return 0d;
            case BOOLEAN:
                return false;
            default:
                throw new RuntimeException(String.format(UNKNOWN, type));
        }
    }

    public static Value fromString(String s) {
        if (s.contains(".")) {
            return new Value(Double.parseDouble(s), Type.FLOAT);
        } else {
            return new Value(Integer.parseInt(s), Type.INT);
        }
    }

    public Value cast(Type toType) throws ValueException {
        switch (toType) {
            case BOOLEAN:
                return new Value(asBoolean(), toType);
            case BYTE:
                return new Value(asByte(), toType);
            case INT:
                return new Value(asInt(), toType);
            case STR:
                return new Value(asString(), toType);
            case FLOAT:
                return new Value(asDouble(), toType);
            default:
                throw new ValueException(CASTING_ERROR);
        }
    }

    public Object get() {
        return value;
    }

    public void set(Object value) {
        this.value = value;
    }

    public List<Value> getList() {
        return valueList;
    }

    public Type getType() {
        return type;
    }

    public Value add(Value v) throws ValueException {
        validateNonArray();
        switch (type) {
            case STR:
                return new Value(asString() + v.asString(), type);
            case INT:
                return new Value(asInt() + v.asInt(), type);
            case FLOAT:
                return new Value(asDouble() + v.asDouble(), type);
            case BYTE:
                return new Value(asByte() + v.asByte(), type);
            default:
                throw new ValueException("Cannot add " + type);
        }
    }

    public Value deduct(Value v) throws ValueException {
        validateNonArray();
        switch (type) {
            case INT:
                return new Value(asInt() - v.asInt(), type);
            case FLOAT:
                return new Value(asDouble() - v.asDouble(), type);
            case BYTE:
                return new Value(asByte() - v.asByte(), type);
            default:
                throw new ValueException("Cannot deduct " + type);
        }
    }

    public Value multiply(Value v) throws ValueException {
        validateNonArray();
        switch (type) {
            case INT:
                return new Value(asInt() * v.asInt(), type);
            case FLOAT:
                return new Value(asDouble() * v.asDouble(), type);
            case BYTE:
                return new Value(asByte() * v.asByte(), type);
            default:
                throw new ValueException("Cannot multiply " + type);
        }
    }

    public Value divide(Value v) throws ValueException {
        validateNonArray();
        switch (type) {
            case INT:
                return new Value(asInt() / v.asInt(), type);
            case FLOAT:
                return new Value(asDouble() / v.asDouble(), type);
            case BYTE:
                return new Value(asByte() / v.asByte(), type);
            default:
                throw new ValueException("Cannot divide " + type);
        }
    }

    public Value negate() throws ValueException {
        validateNonArray();
        switch (type) {
            case BOOLEAN:
                return new Value(!asBoolean(), type);
            case INT:
                return new Value(-asInt(), type);
            case FLOAT:
                return new Value(-asDouble(), type);
            case BYTE:
                return new Value(-asByte(), type);
            default:
                throw new ValueException("Cannot negate " + type);
        }
    }

    public int asInt() throws ValueException {
        validateNonArray();
        try {
            switch (type) {
                case FLOAT:
                    double d = (double) value;
                    return (int) d;
                case BOOLEAN:
                    return asBoolean() ? 1 : 0;
                default:
                    return Integer.parseInt(value.toString());
            }
        } catch (NumberFormatException e) {
            throw new ValueException(String.format(CAST, type.name(), Type.INT.name()));
        }
    }

    public double asDouble() throws ValueException {
        validateNonArray();
        try {
            if (type == Type.BOOLEAN) {
                return asBoolean() ? 1d : 0d;
            }
            return Float.parseFloat(value.toString());
        } catch (NumberFormatException e) {
            throw new ValueException(String.format(CAST, type.name(), Type.FLOAT.name()));
        }
    }

    public String asString() throws ValueException {
        validateNonArray();
        return value.toString();
    }

    public boolean asBoolean() throws ValueException {
        validateNonArray();
        try {
            switch (type) {
                case FLOAT:
                    return asDouble() != 0d;
                case INT:
                    return asInt() == 1;
                case BYTE:
                    return asByte() == (byte) 1;
                default:
                    return Boolean.parseBoolean(value.toString());
            }
        } catch (NumberFormatException e) {
            throw new ValueException(String.format(CAST, type.name(), Type.BOOLEAN.name()));
        }
    }

    public byte asByte() throws ValueException {
        validateNonArray();
        try {
            switch (type) {
                case FLOAT:
                    double d = (double) value;
                    return (byte) d;
                case BOOLEAN:
                    return asBoolean() ? (byte) 1 : 0;
                default:
                    return Byte.parseByte(value.toString());
            }
        } catch (NumberFormatException e) {
            throw new ValueException(String.format(CAST, type.name(), Type.BYTE.name()));
        }
    }

    private void validateNonArray() throws ValueException {
        if (array) {
            throw new ValueException(ARRAY);
        }
    }

    public boolean isArray() {
        return array;
    }

    @Override
    public String toString() {
        if (value != null) {
            return value.toString();
        } else {
            return "null";
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Value value1 = (Value) o;

        if (value == null ^ value1.value == null) return false;

        if (type == value1.type) return Objects.equals(value, value1.value);

        try {
            return Objects.equals(value1, value1.cast(type));
        } catch (ValueException e) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
