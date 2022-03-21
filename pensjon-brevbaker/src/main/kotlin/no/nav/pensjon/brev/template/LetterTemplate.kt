package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.api.model.*
import no.nav.pensjon.brev.template.base.BaseTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import kotlin.reflect.KClass

data class LetterTemplate<Lang : LanguageSupport, LetterData : Any>(
    val name: String,
    //TODO: Lag støtte for kombinert literal og expression for title
    val title: Element.Text.Literal<Lang>,
    val base: BaseTemplate,
    val letterDataType: KClass<LetterData>,
    val language: Lang,
    val outline: List<Element<Lang>>,
    val attachments: List<IncludeAttachment<Lang, *>> = emptyList(),
    val letterMetadata: LetterMetadata,
) {

    fun render(letter: Letter<*>) =
        base.render(letter)
}

sealed class Expression<out Out> {
    val schema: String = this::class.java.name.removePrefix(this::class.java.`package`.name + '.')

    abstract fun eval(scope: ExpressionScope<*, *>): Out

    data class Literal<out Out>(val value: Out) : Expression<Out>() {
        override fun eval(scope: ExpressionScope<*, *>): Out = value
    }

    data class FromScope<ParameterType : Any, out Out>(val selector: ExpressionScope<ParameterType, *>.() -> Out) :
        Expression<Out>() {
        override fun eval(scope: ExpressionScope<*, *>): Out {
            @Suppress("UNCHECKED_CAST")
            return (scope as ExpressionScope<ParameterType, *>).selector()
        }
    }

    data class UnaryInvoke<In, Out>(
        val value: Expression<In>,
        val operation: UnaryOperation<In, Out>,
    ) : Expression<Out>() {
        override fun eval(scope: ExpressionScope<*, *>): Out = operation.apply(value.eval(scope))
        override fun toString(): String = "$operation($value)"
    }

    data class BinaryInvoke<In1, In2, out Out>(
        val first: Expression<In1>,
        val second: Expression<In2>,
        val operation: BinaryOperation<In1, In2, Out>
    ) : Expression<Out>() {
        override fun eval(scope: ExpressionScope<*, *>): Out = operation.apply(first.eval(scope), second.eval(scope))
        override fun toString(): String = "$operation($first, $second)"
    }

}

typealias StringExpression = Expression<String>

sealed class Element<out Lang : LanguageSupport> {
    val schema: String = this::class.java.name.removePrefix(this::class.java.`package`.name + '.')

    data class Title1<out Lang : LanguageSupport>(val title1: List<Element<Lang>>) : Element<Lang>()
    data class Paragraph<out Lang : LanguageSupport>(val paragraph: List<Element<Lang>>) : Element<Lang>()

    data class ItemList<Lang : LanguageSupport>(
        val items: List<Item<Lang>>
    ) : Element<Lang>() {
        init {
            if (items.isEmpty()) throw IllegalArgumentException("List has no items")
        }

        data class Item<Lang : LanguageSupport>(
            val elements: List<Element<Lang>>,
            val condition: Expression<Boolean>? = null
        )
    }

    data class Table<Lang : LanguageSupport>(
        val rows: List<Row<Lang>>,
        val header: Header<Lang>,
    ) : Element<Lang>() {

        init {
            val cellCounts = rows.map { it.cells.size }.distinct()
            if (cellCounts.size > 1) {
                throw InvalidTableDeclarationException("rows in the table needs to have the same number of cells")
            }
            val cellcount = cellCounts.firstOrNull()
                ?: throw InvalidTableDeclarationException("A table must have at least one row")

            if (cellcount == 0) {
                throw InvalidTableDeclarationException("The table rows must have at least one cell/column")
            }
        }

        data class Row<Lang : LanguageSupport>(val cells: List<Cell<Lang>>, val condition: Expression<Boolean>? = null)
        data class Header<Lang : LanguageSupport>(val colSpec: List<ColumnSpec<Lang>>)

        data class Cell<Lang : LanguageSupport>(
            val elements: List<Element<Lang>>
        )

        data class ColumnSpec<Lang : LanguageSupport>(
            val headerContent: Cell<Lang>,
            val alignment: ColumnAlignment,
            val columnSpan: Int = 1
        )

        enum class ColumnAlignment {
            LEFT, RIGHT
        }
    }

    sealed class Form<out Lang : LanguageSupport> : Element<Lang>() {
        data class Text<out Lang : LanguageSupport>(
            val prompt: Element.Text<Lang>,
            val size: Int,
            val vspace: Boolean = true,
        ) : Form<Lang>()

        data class MultipleChoice<out Lang : LanguageSupport>(
            val prompt: Element.Text<Lang>,
            val choices: List<Element.Text<Lang>>,
            val vspace: Boolean = true,
        ) : Form<Lang>()
    }

    @Deprecated("Deprekert til fordel for 'scope-modyfing' fraser TextOnlyPhrase, ParagraphPhrase og OutlinePhrase")
    data class IncludePhrase<out Lang : LanguageSupport, PhraseData : Any>(
        val data: Expression<PhraseData>,
        val phrase: Phrase<Lang, PhraseData>,
    ) : Element<Lang>()

    class NewLine<out Lang : LanguageSupport> : Element<Lang>()

    sealed class Text<out Lang : LanguageSupport> : Element<Lang>() {
        @Suppress("DataClassPrivateConstructor")
        data class Literal<out Lang : LanguageSupport> private constructor(
            private val text: Map<Language, String>,
            val languages: Lang,
            val fontType: FontType
        ) : Text<Lang>() {

            fun text(language: Language): String =
                text[language]
                    ?: throw IllegalArgumentException("Text.Literal doesn't contain language: ${language::class.qualifiedName}")

            companion object {
                fun <Lang1 : Language> create(
                    lang1: Pair<Lang1, String>,
                    fontType: FontType = FontType.PLAIN
                ) = Literal<LanguageSupport.Single<Lang1>>(
                    text = mapOf(lang1),
                    languages = LanguageCombination.Single(lang1.first),
                    fontType = fontType
                )

                fun <Lang1 : Language, Lang2 : Language> create(
                    lang1: Pair<Lang1, String>,
                    lang2: Pair<Lang2, String>,
                    fontType: FontType = FontType.PLAIN,
                ) = Literal<LanguageSupport.Double<Lang1, Lang2>>(
                    text = mapOf(lang1, lang2),
                    languages = LanguageCombination.Double(lang1.first, lang2.first),
                    fontType = fontType
                )

                fun <Lang1 : Language, Lang2 : Language, Lang3 : Language> create(
                    lang1: Pair<Lang1, String>,
                    lang2: Pair<Lang2, String>,
                    lang3: Pair<Lang3, String>,
                    fontType: FontType = FontType.PLAIN,
                ) = Literal<LanguageSupport.Triple<Lang1, Lang2, Lang3>>(
                    text = mapOf(lang1, lang2, lang3),
                    languages = LanguageCombination.Triple(lang1.first, lang2.first, lang3.first),
                    fontType = fontType
                )
            }
        }

        enum class FontType {
            PLAIN,
            BOLD,
            ITALIC
        }

        data class Expression<out Lang : LanguageSupport>(val expression: StringExpression) :
            Text<Lang>() {

            @Suppress("DataClassPrivateConstructor")
            data class ByLanguage<out Lang : LanguageSupport> private constructor(
                val expression: Map<Language, StringExpression>
            ) : Text<Lang>() {

                fun expr(language: Language): StringExpression =
                    expression[language]
                        ?: throw IllegalArgumentException("Text.Expression.ByLanguage doesn't contain language: ${language::class.qualifiedName}")

                companion object {
                    fun <Lang1 : Language> create(lang1: Pair<Lang1, StringExpression>) =
                        ByLanguage<LanguageSupport.Single<Lang1>>(mapOf(lang1))

                    fun <Lang1 : Language, Lang2 : Language> create(
                        lang1: Pair<Lang1, StringExpression>,
                        lang2: Pair<Lang2, StringExpression>,
                    ) = ByLanguage<LanguageSupport.Double<Lang1, Lang2>>(mapOf(lang1, lang2))

                    fun <Lang1 : Language, Lang2 : Language, Lang3 : Language> create(
                        lang1: Pair<Lang1, StringExpression>,
                        lang2: Pair<Lang2, StringExpression>,
                        lang3: Pair<Lang3, StringExpression>,
                    ) = ByLanguage<LanguageSupport.Triple<Lang1, Lang2, Lang3>>(mapOf(lang1, lang2, lang3))
                }
            }
        }
    }

    data class Conditional<out Lang : LanguageSupport>(
        val predicate: Expression<Boolean>,
        val showIf: List<Element<Lang>>,
        val showElse: List<Element<Lang>>,
    ) : Element<Lang>()

    @Suppress("DataClassPrivateConstructor")
    data class ForEachView<out Lang : LanguageSupport, Item : Any> private constructor(val items: Expression<List<Item>>, val body: List<Element<Lang>>, private val next: NextExpression<Item>) : Element<Lang>() {

        fun render(scope: ExpressionScope<*, *>, renderElement: (scope: ExpressionScope<*, *>, element: Element<*>) -> Unit) {
            items.eval(scope).forEach { item ->
                val iteratorScope = ForEachExpressionScope(item to next, scope)
                body.forEach { element ->
                    renderElement(iteratorScope, element)
                }
            }
        }

        companion object {
            fun <Lang : LanguageSupport, Item : Any> create(items: Expression<List<Item>>, createView: (item: Expression<Item>) -> List<Element<Lang>>): ForEachView<Lang, Item> =
                NextExpression<Item>().let { ForEachView(items, createView(it), it) }
        }

        private class NextExpression<Item : Any> : Expression<Item>() {
            override fun eval(scope: ExpressionScope<*, *>): Item =
                if (scope is ForEachExpressionScope<*>) {
                    @Suppress("UNCHECKED_CAST")
                    (scope as ForEachExpressionScope<Item>).evalNext(this)
                } else {
                    throw InvalidScopeTypeException("Requires scope to be ForEachExpressionScope, but was: ${scope::class.qualifiedName}")
                }
        }

        private class ForEachExpressionScope<Item : Any>(val next: Pair<Item, NextExpression<Item>>, val parent: ExpressionScope<*, *>) : ExpressionScope<Any, Language>(parent.argument, parent.felles, parent.language) {
            fun evalNext(expr: NextExpression<Item>): Item =
                if (expr == next.second) {
                    next.first
                } else if(parent is ForEachExpressionScope<*>) {
                    @Suppress("UNCHECKED_CAST")
                    (parent as ForEachExpressionScope<Item>).evalNext(expr)
                } else {
                    throw MissingScopeForNextItemEvaluationException("Could not find scope matching: $expr")
                }
        }
    }
}

class MissingScopeForNextItemEvaluationException(msg: String) : Exception(msg)
class InvalidScopeTypeException(msg: String) : Exception(msg)
class InvalidTableDeclarationException(private val msg: String): Exception(msg)