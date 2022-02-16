
@file:JvmMultifileClass
package braindustry.logic

import arc.func.Func3
import mindustry.logic.LExecutor.PrintI

@Suppress("ClassName")
internal fun interface StrOpLambda3All : (String?, String?, String?) -> Any?
internal typealias   `(str?, str?, str?)` = StrOpLambda3All

@Suppress("ClassName")
internal fun interface StrOpLambda3TwoStr : (String?, String?, Double?) -> Any?
internal typealias   `(str?, str?, num?)` = StrOpLambda3TwoStr

@Suppress("ClassName")
internal fun interface StrOpLambda3TwoStr2 : (String?, Double?, String?) -> Any?
internal typealias   `(str?, num?, str?)` = StrOpLambda3TwoStr2

@Suppress("ClassName")
internal fun interface StrOpLambda3 : (String?, Double, Double) -> Any?
internal typealias   `(str?, num, num)` = StrOpLambda3

@Suppress("ClassName")
internal fun interface StrOpLambda2One : (String?, Double) -> Any?
internal typealias   `(str?, num)` = StrOpLambda2One

@Suppress("ClassName")
internal fun interface StrOpLambda2 : (Any?, Any?) -> Any?
internal typealias   `(Any?, Any?)` = StrOpLambda2

@Suppress("ClassName")
internal fun interface StrOpLambda2All : (String?, String?) -> Any?
internal typealias   `(str?, str?)` = StrOpLambda2All

@Suppress("ClassName")
internal fun interface StrOpLambda1Str : (String?) -> Any?
internal typealias   `(str?)` = StrOpLambda1Str

@Suppress("ClassName")
internal fun interface StrOpLambda1 : (Any?) -> Any?
internal typealias   `(Any?)` = StrOpLambda1

@Suppress("EnumEntryName")
enum class StrOp {
    add("+", `(str?, str?)` { a, b -> a + "" + b }, "a", "b"),
    add3("+ 3x", `(str?, str?, str?)` { a, b, c -> "$a$b$c" }, "a", "b", "c"),
    number("num", `(str?)` { a -> a?.toFloatOrNull() }, "str"),
    str("str", `(Any?)` { obj -> PrintI.toString(obj) }, "obj"),
    length("length", `(str?)`{ a -> a?.length ?: -1 }, "text"),
    indexOf("index", `(str?, str?, num?)` { a, b, c -> if (c == null || c <= 0) a?.indexOf(b!!) else a?.indexOf(b!!, c.toInt()) },
        "text", "str", "indexFrom"
    ),
    lastIndexOf("lastIndex",
        `(str?, str?, num?)` { a, b, c: Double? -> if (c == null || c <= 0) a?.lastIndexOf(b!!) else a?.lastIndexOf(b!!, c.toInt()) },
        "text", "str", "indexFrom"
    ),
    substring("sub", `(str?, num, num)` { a, b, c ->
        if (b < 0) {
            null
        } else if (c < 0) {
            a?.substring(b.toInt())
        } else if (c < a?.length ?: 0 && b <= c && c <= a?.length ?: 0) {
            a?.substring(b.toInt(), c.toInt())
        } else {
            null
        }
    }, "text", "from", "to"),
    starts("starts", `(str?, str?)` { obj, prefix -> obj?.startsWith(prefix!!) }),
    end("ends", `(str?, str?)` { obj, suffix -> obj?.endsWith(suffix!!) }),
    chatAt("chatAt", `(str?, num)` { a, b -> "${a?.get(b.toInt())}" }),
    insert("insert", `(str?, num?, str?)` { a, b, c -> "${a?.substring(0, b?.toInt()?:0)}$c${a?.substring(b?.toInt()?:0 + 1)}" }),
    replace("replace", `(str?, str?, str?)` { obj, target, replacement -> obj.takeIf { target != null && replacement != null }?.replace(target!!, replacement!!) });

    @JvmField
    val func: Func3<Any, Any, Any, Any>

    @JvmField
    val type: IntArray

    @JvmField
    val symbol: String

    @JvmField
    val params: Array<out String>

    constructor(symbol: String, function: `(str?)`, vararg params: String) {
        this.symbol = symbol
        func = Func3 { a: Any?, b: Any?, c: Any? -> function(a as String?) }
        type = type(strVal)
        this.params = params
    }

    constructor(symbol: String, function: `(Any?)`, vararg params: String) {
        this.symbol = symbol
        func = Func3 { a: Any?, b: Any?, c: Any? -> function(a) }
        type = type(objVal)
        this.params = params
    }

    constructor(symbol: String, function: `(Any?, Any?)`, vararg params: String) {
        this.symbol = symbol
        func = Func3 { a: Any?, b: Any?, c: Any? -> function(a, b) }
        type = type(objVal, objVal)
        this.params = params
    }

    constructor(symbol: String, function: `(str?, num)`, vararg params: String) {
        this.symbol = symbol
        func = Func3 { a: Any?, b: Any, c: Any? -> function(a as String?, b as Double) }
        type = type(strVal, numVal)
        this.params = params
    }

    constructor(symbol: String, function: `(str?, str?)`, vararg params: String) {
        this.symbol = symbol
        func = Func3 { a: Any?, b: Any?, c: Any? -> function(a as String?, b as String?) }
        type = type(strVal, strVal)
        this.params = params
    }

    constructor(symbol: String, function: `(str?, num, num)`, vararg params: String) {
        this.symbol = symbol
        func = Func3 { a: Any?, b: Any, c: Any -> function(a as String?, b as Double, c as Double) }
        type = type(strVal, numVal, numVal)
        this.params = params
    }

    constructor(symbol: String, function: `(str?, str?, num?)`, vararg params: String) {
        this.symbol = symbol
        func = Func3 { a: Any?, b: Any?, c: Any? -> function.invoke(a as String?, b as String?, c as Double?) }
        type = type(strVal, strVal, numVal)
        this.params = params
    }

    constructor(symbol: String, function: `(str?, str?, str?)`, vararg params: String) {
        this.symbol = symbol
        func = Func3 { a: Any?, b: Any?, c: Any? -> function(a as String?, b as String?, c as String?) }
        type = type(strVal, strVal, strVal)
        this.params = params
    }

    constructor(symbol: String, function: `(str?, num?, str?)`, vararg params: String) {
        this.symbol = symbol
        func = Func3 { a: Any?, b: Any?, c: Any? -> function(a as String?, b as Double?, c as String?) }
        type = type(strVal, numVal, strVal)
        this.params = params
    }

    constructor(symbol: String, func: Func3<Any, Any, Any, Any>, vararg params: String) {
        this.symbol = symbol
        this.func = func
        type = type(objVal, objVal, objVal)
        this.params = params
    }

    override fun toString(): String {
        return symbol
    }


    companion object {
        @JvmField
        val all = values()

        const val strVal = 0
        const val objVal = 1
        const val numVal = 2
        private fun sd(o: (String, Double) -> Any): Func3<Any?, Any?, Any?, Any> {
            return Func3 { a: Any?, b: Any?, c: Any? ->
                o.invoke(PrintI.toString(a), b.double)
            }
        }

        private fun sds(func: (String, Double, String) -> Any): Func3<Any?, Any?, Any?, Any> {
            return Func3 { a: Any?, b: Any?, c: Any? ->
                func.invoke(PrintI.toString(a), b.double, PrintI.toString(c))
            }
        }
        private val Any?.double: Double
            get() = if (this == null) 0.0 else toString().toDoubleOrNull() ?: 0.0
    }
}

private fun type(param1:Int=-1, param2:Int=-1, param3:Int=-1)= intArrayOf(param1,param2,param3)

