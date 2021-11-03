package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.template.base.BaseTemplate
import kotlin.reflect.KClass

data class LetterTemplate<Lang : LanguageCombination, LetterData : Any>(
    val name: String,
    //TODO: Lag støtte for kombinert literal og expression
    val title: Element.Text.Literal<Lang>,
    val base: BaseTemplate,
    val letterDataType: KClass<LetterData>,
    val language: Lang,
    val outline: List<Element<Lang>>,
    val attachments: List<AttachmentTemplate<Lang, LetterData>> = emptyList(),
) {

    fun render(letter: Letter<*>) =
        base.render(letter)
}

data class AttachmentTemplate<Lang : LanguageCombination, ParameterType : Any>(
    val title: Element.Text.Literal<Lang>,
    val outline: List<Element<Lang>>,
    val includeSakspart: Boolean = false,
)

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
        val operation: UnaryOperation<In, Out>
    ) : Expression<Out>() {
        override fun eval(scope: ExpressionScope<*, *>): Out = operation.apply(value.eval(scope))
    }

    data class BinaryInvoke<In1, In2, out Out>(
        val first: Expression<In1>,
        val second: Expression<In2>,
        val operation: BinaryOperation<In1, In2, Out>
    ) : Expression<Out>() {
        override fun eval(scope: ExpressionScope<*, *>): Out {
            return operation.apply(first.eval(scope), second.eval(scope))
        }
    }

}

typealias StringExpression = Expression<String>

sealed class Element<Lang : LanguageCombination> {
    val schema: String = this::class.java.name.removePrefix(this::class.java.`package`.name + '.')

    data class Title1<Lang : LanguageCombination>(val title1: List<Element<Lang>>) : Element<Lang>()
    data class Paragraph<Lang : LanguageCombination>(val paragraph: List<Element<Lang>>) : Element<Lang>()

    sealed class Form<Lang : LanguageCombination> : Element<Lang>() {
        data class Text<Lang : LanguageCombination>(
            val prompt: Element.Text<Lang>,
            val size: Int,
            val vspace: Boolean = true
        ) : Form<Lang>()

        data class MultipleChoice<Lang : LanguageCombination>(
            val prompt: Element.Text<Lang>,
            val choices: List<Element.Text<Lang>>,
            val vspace: Boolean = true
        ) : Element.Form<Lang>()
    }

    data class IncludePhrase<Lang : LanguageCombination, PhraseData: Any>(
        val data: Expression<PhraseData>,
        val phrase: Phrase<PhraseData>
    ): Element<Lang>()

    class NewLine<Lang : LanguageCombination> : Element<Lang>()

    sealed class Text<Lang : LanguageCombination> : Element<Lang>() {
        data class Literal<Lang : LanguageCombination> private constructor(private val text: Map<Language, String>) :
            Text<Lang>() {

            fun text(language: Language): String =
                text[language]
                    ?: throw IllegalArgumentException("Text.Literal doesn't contain language: ${language::class.qualifiedName}")

            companion object {
                fun <Lang1 : Language> create(lang1: Pair<Lang1, String>) =
                    Literal<LanguageCombination.Single<Lang1>>(mapOf(lang1))

                fun <Lang1 : Language, Lang2 : Language> create(
                    lang1: Pair<Lang1, String>,
                    lang2: Pair<Lang2, String>,
                ) = Literal<LanguageCombination.Double<Lang1, Lang2>>(mapOf(lang1, lang2))

                fun <Lang1 : Language, Lang2 : Language, Lang3 : Language> create(
                    lang1: Pair<Lang1, String>,
                    lang2: Pair<Lang2, String>,
                    lang3: Pair<Lang3, String>,
                ) = Literal<LanguageCombination.Triple<Lang1, Lang2, Lang3>>(mapOf(lang1, lang2, lang3))
            }
        }

        data class Expression<Lang : LanguageCombination>(val expression: StringExpression) :
            Text<Lang>() {

            data class ByLanguage<Lang : LanguageCombination> private constructor(
                val expression: Map<Language, StringExpression>
            ) : Text<Lang>() {

                fun expr(language: Language): StringExpression =
                    expression[language]
                        ?: throw IllegalArgumentException("Text.Expression.ByLanguage doesn't contain language: ${language::class.qualifiedName}")

                companion object {
                    fun <Lang1 : Language> create(lang1: Pair<Lang1, StringExpression>) =
                        ByLanguage<LanguageCombination.Single<Lang1>>(mapOf(lang1))

                    fun <Lang1 : Language, Lang2 : Language> create(
                        lang1: Pair<Lang1, StringExpression>,
                        lang2: Pair<Lang2, StringExpression>,
                    ) = ByLanguage<LanguageCombination.Double<Lang1, Lang2>>(mapOf(lang1, lang2))

                    fun <Lang1 : Language, Lang2 : Language, Lang3 : Language> create(
                        lang1: Pair<Lang1, StringExpression>,
                        lang2: Pair<Lang2, StringExpression>,
                        lang3: Pair<Lang3, StringExpression>,
                    ) = ByLanguage<LanguageCombination.Triple<Lang1, Lang2, Lang3>>(mapOf(lang1, lang2, lang3))
                }
            }
        }


    }

    data class Conditional<Lang : LanguageCombination>(
        val predicate: Expression<Boolean>,
        val showIf: List<Element<Lang>>,
        val showElse: List<Element<Lang>>,
    ) : Element<Lang>()

}
