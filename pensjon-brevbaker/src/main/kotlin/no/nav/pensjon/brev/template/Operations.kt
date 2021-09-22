package no.nav.pensjon.brev.template

import java.time.LocalDate

abstract class Operation {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}

sealed class UnaryOperation<In, out Out> : Operation() {
    abstract fun apply(input: In): Out

    class ToString<T : Any> : UnaryOperation<T, String>() {
        override fun apply(input: T): String = input.toString()
    }

    data class Select<In, Out>(val select: In.() -> Out) : UnaryOperation<In, Out>() {
        override fun apply(input: In): Out = input.select()
    }
}

sealed class BinaryOperation<in In1, in In2, out Out> : Operation() {


    abstract fun apply(first: In1, second: In2): Out

    class Equal<In : Comparable<In>> : BinaryOperation<In, In, Boolean>() {
        override fun apply(first: In, second: In): Boolean = first == second
    }

    class GreaterThan<In : Comparable<In>> : BinaryOperation<In, In, Boolean>() {
        override fun apply(first: In, second: In): Boolean = first > second
    }

    class GreaterThanOrEqual<In : Comparable<In>> : BinaryOperation<In, In, Boolean>() {
        override fun apply(first: In, second: In): Boolean = first >= second
    }

    class LesserThanOr<In : Comparable<In>> : BinaryOperation<In, In, Boolean>() {
        override fun apply(first: In, second: In): Boolean = first < second
    }

    class LesserThanOrEqual<In : Comparable<In>> : BinaryOperation<In, In, Boolean>() {
        override fun apply(first: In, second: In): Boolean = first <= second
    }

    object Concat : BinaryOperation<String, String, String>() {
        override fun apply(first: String, second: String): String = first + second
    }

    object LocalizedDateFormat : BinaryOperation<LocalDate, Language, String>() {
        override fun apply(first: LocalDate, second: Language): String =
            first.format(dateFormatter(second))
    }

}