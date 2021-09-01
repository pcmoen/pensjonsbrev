package no.nav.pensjon.brev.template

fun <Lang : LanguageCombination> createTemplate(
    name: String,
    base: BaseTemplate,
    lang: Lang,
    init: LetterTemplateBuilder<Lang>.() -> Unit
): LetterTemplate<Lang> =
    with(LetterTemplateBuilder<Lang>().apply(init)) {
        return LetterTemplate(name, base, parameters, lang, outline)
    }

fun <Lang1 : Language> languages(lang1: Lang1) =
    LanguageCombination.Single(lang1)

fun <Lang1 : Language, Lang2 : Language> languages(lang1: Lang1, lang2: Lang2) =
    LanguageCombination.Double(lang1, lang2)

fun <Lang1 : Language, Lang2 : Language, Lang3 : Language> languages(lang1: Lang1, lang2: Lang2, lang3: Lang3) =
    LanguageCombination.Triple(lang1, lang2, lang3)

@LetterTemplateMarker
class ParametersBuilder(val parameters: MutableSet<TemplateParameter> = mutableSetOf()) {
    fun required(init: () -> Parameter) =
        parameters.add(RequiredParameter(init()))

    fun optional(init: () -> Parameter) =
        parameters.add(OptionalParameter(init()))
}

@LetterTemplateMarker
open class LetterTemplateBuilder<Lang : LanguageCombination>(
    val parameters: MutableSet<TemplateParameter> = mutableSetOf(),
    val outline: MutableList<Element<Lang>> = mutableListOf(),
) {
    fun parameters(init: ParametersBuilder.() -> Unit) =
        parameters.addAll(ParametersBuilder().apply(init).parameters)

    fun outline(init: ContainerElementBuilder<Lang>.() -> Unit) =
        outline.addAll(ContainerElementBuilder<Lang>().apply(init).children)

}

fun Expression<Any>.str(): Expression<String> = Expression.UnaryInvoke(this, UnaryOperation.ToString())

infix fun <T : Comparable<T>> Expression<T>.greaterThan(other: Expression<T>) =
    Expression.BinaryInvoke(this, other, BinaryOperation.GreaterThan())

infix fun <T : Comparable<T>> Expression<T>.greaterThan(other: T) =
    Expression.BinaryInvoke(this, Expression.Literal(other), BinaryOperation.GreaterThan())

fun <Param, Out> argument(parameter: Param): Expression<Out>
        where Param : Parameter,
              Param : ParameterType<Out> =
    Expression.Argument(parameter)

@LetterTemplateMarker
open class TextOnlyBuilder<Lang : LanguageCombination>(val children: MutableList<Element<Lang>> = mutableListOf()) {

    fun eval(expression: Expression<String>): Element.Text.Expression<Lang> =
        Element.Text.Expression<Lang>(expression).also { children.add(it) }

    fun phrase(phrase: Phrase<Lang>): Element.Text.Phrase<Lang> =
        Element.Text.Phrase(phrase).also { children.add(it) }

}

fun <Lang1 : Language> TextOnlyBuilder<LanguageCombination.Single<Lang1>>.text(lang1: Pair<Lang1, String>): Element.Text.Literal<LanguageCombination.Single<Lang1>> =
    Element.Text.Literal.create(lang1).also { children.add(it) }

fun <Lang1 : Language, Lang2 : Language> TextOnlyBuilder<LanguageCombination.Double<Lang1, Lang2>>.text(
    lang1: Pair<Lang1, String>,
    lang2: Pair<Lang2, String>,
): Element.Text.Literal<LanguageCombination.Double<Lang1, Lang2>> =
    Element.Text.Literal.create(lang1, lang2).also { children.add(it) }

fun <Lang1 : Language, Lang2 : Language, Lang3 : Language> TextOnlyBuilder<LanguageCombination.Triple<Lang1, Lang2, Lang3>>.text(
    lang1: Pair<Lang1, String>,
    lang2: Pair<Lang2, String>,
    lang3: Pair<Lang3, String>,
): Element.Text.Literal<LanguageCombination.Triple<Lang1, Lang2, Lang3>> =
    Element.Text.Literal.create(lang1, lang2, lang3).also { children.add(it) }


@LetterTemplateMarker
class ContainerElementBuilder<Lang : LanguageCombination> : TextOnlyBuilder<Lang>() {

    fun title1(init: TextOnlyBuilder<Lang>.() -> Element.Text<Lang>) =
        children.add(Element.Title1(addChildren(init)))

    fun paragraph(init: TextOnlyBuilder<Lang>.() -> Element.Text<Lang>) =
        children.add(Element.Paragraph(addChildren(init)))


    private fun addChildren(init: TextOnlyBuilder<Lang>.() -> Element<Lang>): List<Element<Lang>> {
        val textBuilder = TextOnlyBuilder<Lang>().apply { init() }
        val text = if (textBuilder.children.isEmpty()) {
            listOf(textBuilder.let(init))
        } else {
            textBuilder.children
        }
        return text
    }

    fun showIf(
        predicate: Expression<Boolean>,
        showIf: ContainerElementBuilder<Lang>.() -> Unit
    ): ShowElseBuilder<Lang> {
        val showElse = mutableListOf<Element<Lang>>()

        return ContainerElementBuilder<Lang>().apply { showIf() }
            .let { Element.Conditional(predicate, it.children, showElse) }
            .also { children.add(it) }
            .let { ShowElseBuilder(showElse) }
    }

}

class ShowElseBuilder<Lang : LanguageCombination>(
    val showElse: MutableList<Element<Lang>> = mutableListOf()
) {
    infix fun orShow(init: ContainerElementBuilder<Lang>.() -> Unit) =
        with(ContainerElementBuilder<Lang>().apply(init)) {
            showElse.addAll(children)
        }
}

@DslMarker
annotation class LetterTemplateMarker