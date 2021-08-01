package braindustry.logic;

import arc.func.Func3;
import arc.util.Strings;

public enum StrOp {
    add("+", (String a, String b) -> a + "" + b,"a","b"),
    number("num", (String a) -> {
        if (Strings.canParseFloat(a)) return Strings.parseFloat(a);
        return null;
    },"str"),
    str("str", (Object a) -> {
        return a + "";
    },"obj"),
    length("length", (String a) -> {
        return a == null ? -1 : a.length();
    },"text"),
    indexOf("index", (String a, String b, Double c) -> {
        return c == null || c <= 0 ? a.indexOf(b) : a.indexOf(b, c.intValue());
    },"text","str","indexFrom"),
    lastIndexOf("lastIndex", (String a, String b, Double c) -> {
        return c == null || c <= 0 ? a.lastIndexOf(b) : a.lastIndexOf(b, c.intValue());
    },"text","str","indexFrom"),
    substring("sub", (String a, double b, double c) -> {

        if (b >= 0) {
            if ((c < 0)) {
                return a.substring((int) b).intern();
            } else if (c < a.length()) {
                return a.substring((int) b, (int) c).intern();
            }
        }
        return null;
    },"text","from","to"),
    starts("starts", (String a, String b) -> a.startsWith(b)),
    end("ends", (String a, String b) -> a.endsWith(b));

    public static final StrOp[] all = values();

    public final Func3<Object, Object, Object, Object> func;
    public final float type;
    public final String symbol;
    public final String[] params;

    StrOp(String symbol, StrOpLambda1Str function,String... params) {
        this.symbol = symbol;
        func = (a, b, c) -> function.get((String) a);
        type = 1.1f;
        this.params=params;
    }

    StrOp(String symbol, StrOpLambda1 function,String... params) {
        this.symbol = symbol;
        func = (a, b, c) -> function.get(a);
        type = 1f;
        this.params=params;
    }

    StrOp(String symbol, StrOpLambda2 function,String... params) {
        this.symbol = symbol;
        func = (a, b, c) -> function.get(a, b);
        type = 2f;
        this.params=params;
    }

    StrOp(String symbol, StrOpLambda2One function,String... params) {
        this.symbol = symbol;
        func = (a, b, c) -> function.get((String) a, (Double) b);
        type = 2.1f;
        this.params=params;
    }

    StrOp(String symbol, StrOpLambda2Str function,String... params) {
        this.symbol = symbol;
        func = (a, b, c) -> function.get((String) a, (String) b);
        type = 2.2f;
        this.params=params;
    }

    StrOp(String symbol, StrOpLambda3 function,String... params) {
        this.symbol = symbol;
        func = (a, b, c) -> function.get((String) a, (Double) b, (Double) c);
        type = 3f;
        this.params=params;
    }

    StrOp(String symbol, StrOpLambda3TwoStr function,String... params) {
        this.symbol = symbol;
        func = (a, b, c) -> function.get((String) a, (String) b, (Double) c);
        type = 3.2f;
        this.params=params;
    }

    @Override
    public String toString() {
        return symbol;
    }

    interface StrOpLambda3TwoStr {
        Object get(String a, String b, Double c);
    }

    interface StrOpLambda3 {
        Object get(String a, double b, double c);
    }

    interface StrOpLambda2One {
        Object get(String a, double b);
    }

    interface StrOpLambda2 {
        Object get(Object a, Object b);
    }

    interface StrOpLambda2Str {
        Object get(String a, String b);
    }


    interface StrOpLambda1Str {
        Object get(String a);
    }

    interface StrOpLambda1 {
        String get(Object a);
    }
}
