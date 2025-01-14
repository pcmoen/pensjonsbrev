package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.template.base.BaseTemplate
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
        val elements: List<Element<Lang>>
    ) : Element<Lang>() {
        init {
            if (elements.flatMap { getItems(it) }.isEmpty()) throw InvalidListDeclarationException("List has no items")
        }

        data class Item<Lang : LanguageSupport>(
            val elements: List<Element<Lang>>
        ) : Element<Lang>()

        private fun getItems(element: Element<Lang>): List<Item<Lang>> =
            when (element) {
                is Conditional<Lang> -> element.showIf.plus(element.showElse).flatMap { getItems(it) }
                is ForEachView<Lang, *> -> element.body.flatMap { getItems(it) }
                is Item -> listOf(element)
                else -> throw InvalidListDeclarationException("Unsupported element-kind within list: ${element.javaClass.name}")
            }
    }

    data class Table<Lang : LanguageSupport>(
        val children: List<Element<Lang>>,
        val header: Header<Lang>,
    ) : Element<Lang>() {

        init {
            if (children.flatMap { getRows(it) }.isEmpty()) {
                throw InvalidTableDeclarationException("A table must have at least one row")
            }
        }

        private fun getRows(element: Element<Lang>): List<Row<Lang>> =
            when (element) {
                is Conditional<Lang> -> element.showIf.plus(element.showElse).flatMap { getRows(it) }
                is ForEachView<Lang, *> -> element.body.flatMap { getRows(it) }
                is Row<Lang> -> listOf(element)
                else -> throw InvalidTableDeclarationException("Unhandled element type within table " + element.javaClass.toString())
            }

        data class Row<Lang : LanguageSupport>(
            val cells: List<Cell<Lang>>,
            val colSpec: List<ColumnSpec<Lang>>
        ) : Element<Lang>() {
            init {
                if (cells.isEmpty()) {
                    throw InvalidTableDeclarationException("Rows need at least one cell")
                }
                if (cells.size != colSpec.size) {
                    throw InvalidTableDeclarationException("The number of cells in the row(${cells.size}) does not match the number of columns in the specification(${colSpec.size})")
                }
            }
        }

        data class Header<Lang : LanguageSupport>(val colSpec: List<ColumnSpec<Lang>>)

        data class Cell<Lang : LanguageSupport>(
            val elements: List<Element<Lang>>
        )

        data class ColumnSpec<Lang : LanguageSupport>(
            val headerContent: Cell<Lang>,
            val alignment: ColumnAlignment,
            val columnSpan: Int = 1
        ){
            init {
                if (headerContent.elements.isEmpty()) {
                    throw InvalidTableDeclarationException("Column specification needs at least one column")
                }
            }
        }

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
        abstract val fontType: FontType

        @Suppress("DataClassPrivateConstructor")
        data class Literal<out Lang : LanguageSupport> private constructor(
            private val text: Map<Language, String>,
            val languages: Lang,
            override var fontType: FontType,
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

        data class Expression<out Lang : LanguageSupport>(
            val expression: StringExpression,
            override val fontType: FontType = FontType.PLAIN
        ) :
            Text<Lang>() {

            @Suppress("DataClassPrivateConstructor")
            data class ByLanguage<out Lang : LanguageSupport> private constructor(
                val expression: Map<Language, StringExpression>,
                override val fontType: FontType
            ) : Text<Lang>() {

                fun expr(language: Language): StringExpression =
                    expression[language]
                        ?: throw IllegalArgumentException("Text.Expression.ByLanguage doesn't contain language: ${language::class.qualifiedName}")

                companion object {
                    fun <Lang1 : Language> create(
                        lang1: Pair<Lang1, StringExpression>,
                        fontType: FontType = FontType.PLAIN
                    ) = ByLanguage<LanguageSupport.Single<Lang1>>(mapOf(lang1), fontType)

                    fun <Lang1 : Language, Lang2 : Language> create(
                        lang1: Pair<Lang1, StringExpression>,
                        lang2: Pair<Lang2, StringExpression>,
                        fontType: FontType = FontType.PLAIN,
                    ) = ByLanguage<LanguageSupport.Double<Lang1, Lang2>>(mapOf(lang1, lang2), fontType)

                    fun <Lang1 : Language, Lang2 : Language, Lang3 : Language> create(
                        lang1: Pair<Lang1, StringExpression>,
                        lang2: Pair<Lang2, StringExpression>,
                        lang3: Pair<Lang3, StringExpression>,
                        fontType: FontType = FontType.PLAIN,
                    ) = ByLanguage<LanguageSupport.Triple<Lang1, Lang2, Lang3>>(mapOf(lang1, lang2, lang3), fontType)
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
    data class ForEachView<out Lang : LanguageSupport, Item : Any> private constructor(
        val items: Expression<List<Item>>,
        val body: List<Element<Lang>>,
        private val next: NextExpression<Item>
    ) : Element<Lang>() {

        fun render(
            scope: ExpressionScope<*, *>,
            renderElement: (scope: ExpressionScope<*, *>, element: Element<*>) -> Unit
        ) {
            items.eval(scope).forEach { item ->
                val iteratorScope = ForEachExpressionScope(item to next, scope)
                body.forEach { element ->
                    renderElement(iteratorScope, element)
                }
            }
        }

        companion object {
            fun <Lang : LanguageSupport, Item : Any> create(
                items: Expression<List<Item>>,
                createView: (item: Expression<Item>) -> List<Element<Lang>>
            ): ForEachView<Lang, Item> =
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

        private class ForEachExpressionScope<Item : Any>(
            val next: Pair<Item, NextExpression<Item>>,
            val parent: ExpressionScope<*, *>
        ) : ExpressionScope<Any, Language>(parent.argument, parent.felles, parent.language) {
            fun evalNext(expr: NextExpression<Item>): Item =
                if (expr == next.second) {
                    next.first
                } else if (parent is ForEachExpressionScope<*>) {
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
class InvalidTableDeclarationException(msg: String) : Exception(msg)
class InvalidListDeclarationException(msg: String) : Exception(msg)