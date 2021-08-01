package braindustry.logic;

import arc.func.Func3;
import arc.util.Strings;

public enum StrOp {
    add("+", (String a, String b) -> a + "" + b, "a", "b"),
    add3("+ 3x", (String a, String b, String c) -> a + b + c, "a", "b", "c"),
    number("num", (String a) -> {
        if (Strings.canParseFloat(a)) return Strings.parseFloat(a);
        return null;
    }, "str"),
    str("str", (Object a) -> {
        return a + "";
    }, "obj"),
    length("length", (String a) -> {
        return a == null ? -1 : a.length();
    }, "text"),
    indexOf("index", (String a, String b, Double c) -> {
        return c == null || c <= 0 ? a.indexOf(b) : a.indexOf(b, c.intValue());
    }, "text", "str", "indexFrom"),
    lastIndexOf("lastIndex", (String a, String b, Double c) -> {
        return c == null || c <= 0 ? a.lastIndexOf(b) : a.lastIndexOf(b, c.intValue());
    }, "text", "str", "indexFrom"),
    substring("sub", (String a, double b, double c) -> {

        if (b >= 0) {
            if ((c < 0)) {
                return a.substring((int) b).intern();
            } else if (c < a.length()) {
                return a.substring((int) b, (int) c).intern();
            }
        }
        return null;
    }, "text", "from", "to"),
    starts("starts", (String a, String b) -> a.startsWith(b)),
    end("ends", (String a, String b) -> a.endsWith(b));

    public static final StrOp[] all = values();
    static final int strVal = 0, objVal = 1, numVal = 2;
    public final Func3<Object, Object, Object, Object> func;
    public final SrtOpType type;
    public final String symbol;
    public final String[] params;

    StrOp(String symbol, StrOpLambda1Str function, String... params) {
        this.symbol = symbol;
        func = (a, b, c) -> function.get((String) a);
        type = function.getType();
        this.params = params;
    }

    StrOp(String symbol, StrOpLambda1 function, String... params) {
        this.symbol = symbol;
        func = (a, b, c) -> function.get(a);
        type = function.getType();
        this.params = params;
    }

    StrOp(String symbol, StrOpLambda2 function, String... params) {
        this.symbol = symbol;
        func = (a, b, c) -> function.get(a, b);
        type = function.getType();
        this.params = params;
    }

    StrOp(String symbol, StrOpLambda2One function, String... params) {
        this.symbol = symbol;
        func = (a, b, c) -> function.get((String) a, (Double) b);
        type = function.getType();
        this.params = params;
    }

    StrOp(String symbol, StrOpLambda2All function, String... params) {
        this.symbol = symbol;
        func = (a, b, c) -> function.get((String) a, (String) b);
        type = function.getType();
        this.params = params;
    }

    StrOp(String symbol, StrOpLambda3 function, String... params) {
        this.symbol = symbol;
        func = (a, b, c) -> function.get((String) a, (Double) b, (Double) c);
        type = function.getType();
        this.params = params;
    }

    StrOp(String symbol, StrOpLambda3TwoStr function, String... params) {
        this.symbol = symbol;
        func = (a, b, c) -> function.get((String) a, (String) b, (Double) c);
        type = function.getType();
        this.params = params;
    }

    StrOp(String symbol, StrOpLambda3All function, String... params) {
        this.symbol = symbol;
        func = (a, b, c) -> function.get((String) a, (String) b, (String) c);
        type = function.getType();
        this.params = params;
    }

    @Override
    public String toString() {
        return symbol;
    }

    interface StrOpLambda3All {
        Object get(String a, String b, String c);

        default SrtOpType getType() {
            return new SrtOpType(strVal,strVal,strVal);
        }
    }

    interface StrOpLambda3TwoStr {
        Object get(String a, String b, Double c);

        default SrtOpType getType() {
            return new SrtOpType(strVal,strVal,numVal);
        }
    }

    interface StrOpLambda3 {
        Object get(String a, double b, double c);

        default SrtOpType getType() {
            return new SrtOpType(strVal,numVal,numVal);
        }
    }

    interface StrOpLambda2One {
        Object get(String a, double b);

        default SrtOpType getType() {
            return new SrtOpType(strVal,numVal,-1);
        }
    }

    interface StrOpLambda2 {
        Object get(Object a, Object b);

        default SrtOpType getType() {
            return new SrtOpType(objVal,objVal,-1);
        }
    }

    interface StrOpLambda2All {
        Object get(String a, String b);

        default SrtOpType getType() {
            return new SrtOpType(strVal,strVal,-1);
        }
    }

    interface StrOpLambda1Str {
        Object get(String a);

        default SrtOpType getType() {
            return new SrtOpType(strVal,-1,-1);
        }
    }


    interface StrOpLambda1 {
        String get(Object a);

        default SrtOpType getType() {
            return new SrtOpType(objVal,-1,-1);
        }
    }

    static class SrtOpType {
        final int first, second, third;

        SrtOpType(int first, int second, int third) {
            this.first = first;
            this.second = second;
            this.third = third;
        }
    }
}
