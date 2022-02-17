@file:JvmMultifileClass

package braindustry.logic

import arc.func.Func3
import arc.math.Mathf
import mindustry.logic.LExecutor
import kotlin.math.min

object StringOperationScope {
    var a: Any? = null
    var b: Any? = null
    var c: Any? = null
    inline val strA: String
        get() = a.asLogicString();
    inline val strB: String
        get() = b.asLogicString();
    inline val strC: String
        get() = c.asLogicString();
    inline val numA: Double
        get() = strA.toDoubleOrNull() ?: 0.0;
    inline val numB: Double
        get() = strB.toDoubleOrNull() ?: 0.0;
    inline val numC: Double
        get() = strC.toDoubleOrNull() ?: 0.0;

    operator fun invoke(a: Any?, b: Any? = null, c: Any? = null): StringOperationScope {
        this.a = a;
        this.b = b;
        this.c = c;
        return StringOperationScope;
    }

    operator fun invoke(a: LExecutor.Var?, b: LExecutor.Var? = null, c: LExecutor.Var? = null): StringOperationScope {
        return this(a.unwrap(),b.unwrap(),c.unwrap())
    }

    inline fun <reified T> T.asLogicString(): String = if (this is Number) this.toString() else LExecutor.PrintI.toString(this)
    private fun LExecutor.Var?.unwrap(): Any? {
        return if (this == null) {
            null
        } else {
            (if (isobj) objval else numval)
        }
    }
}


@Suppress("EnumEntryName","unused")
enum class StrOp(@JvmField val symbol: String,@JvmField val paramsAmount: Int, block: StringOperationScope.() -> Any?, @JvmField vararg val params: String) {
    add("+", 2, { "$strA$strB" }, "a", "b"),
    add3("+ 3x", 3, { "$strA$strB$strC" }, "a", "b", "c"),
    number("num", 1, { numA }, "str"),
    str("str", 1, { strA }, "obj"),
    length("length", 1, { strA.length }, "text"),
    indexOf("index", 3, { if (numC <= 0) strA.indexOf(strB) else strA.indexOf(strB, numC.toInt()) },
        "text", "str", "indexFrom"
    ),
    lastIndexOf("lastIndex", 3, { if (numC <= 0) strA.lastIndexOf(strB) else strA.lastIndexOf(strB, numC.toInt()) },
        "text", "str", "indexFrom"
    ),
    substring("sub", 3, {
        if (numB < 0) {
            null
        } else if (numC < 0) {
            strA.substring(numB.toInt())
        } else if (numC < strA.length && numB <= numC && numC <= strA.length) {
            strA.substring(numB.toInt(), numC.toInt())
        } else {
            null
        }
    }, "text", "from", "to"),
    starts("starts", 2, { strA.startsWith(strB) }),
    end("ends", 2, { strA.endsWith(strB) }),
    chatAt("chatAt", 2, { "${strA[Mathf.clamp(numB.toInt(), 0, strA.length - 1)]}" }),
    insert("insert", 3,
        { "${strA.substring(0, min(strA.length, numB.toInt()))}$strC${strA.substring(min(strA.length-1, numB.toInt()))}" }),
    replace("replace", 3, { strA.replace(strB, strC) });

    @JvmField
    val func: Func3<LExecutor.Var, LExecutor.Var, LExecutor.Var, Any?> = when (paramsAmount) {
        1 -> Func3 { a, _, _ -> StringOperationScope(a).block() }
        2 -> Func3 { a, b, _ -> StringOperationScope(a, b).block() }
        3 -> Func3 { a, b, c -> StringOperationScope(a, b, c).block() }
        else -> {
            throw IllegalArgumentException("illegal params amount: $paramsAmount")
        }
    }


    override fun toString(): String {
        return symbol
    }


    companion object {
        @JvmField
        val all = values()
    }
}