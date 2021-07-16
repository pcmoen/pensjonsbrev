package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.something.Fraser
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsCollectionContaining
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.matchers.JUnitMatchers
import java.io.OutputStream
import kotlin.math.exp

class UtilsTest {

    object TestMaster : BaseTemplate() {
        override val parameters = emptySet<TemplateParameter>()

        override fun render(letter: Letter, out: OutputStream) {
            TODO("Not yet implemented")
        }
    }

    val frase1 = Phrase.Static("jadda", "jadda")
    val frase2 = Phrase.Static("jadda2", "jadda2")

    val templ = createTemplate("test", TestMaster) {
        parameters {
            required { PensjonInnvilget }
            required { KortNavn }
        }
        outline {
            showIf(argument(PensjonInnvilget)) {
                text("hello1")
                phrase(frase1)
            } orShow {
                title1 {
                    text("hello2")
                    phrase(frase2)
                    eval(argument(KortNavn))
                }
            }

            section {
                title1("heisann")
                text("hello3")
                phrase(frase2)

                eval(argument(KortNavn))
            }
        }
    }

    @Test
    fun `findExpressions finds all expressions`() {
        val expressions = templ.outline.flatMap { it.findExpressions() }

        assertThat(expressions, IsCollectionContaining.hasItems(argument(PensjonInnvilget), argument(KortNavn), argument(KortNavn)))
    }

    @Test
    fun `requiredArguments finds all required arguments in an expression`() {
        val expr = Expression.BinaryInvoke(
            argument(KortNavn),
            Expression.UnaryInvoke(argument(PensjonInnvilget), UnaryOperation.ToString()),
            BinaryOperation.Equal()
        )

        assertThat(expr.requiredArguments(), IsCollectionContaining.hasItems(argument(KortNavn), argument(PensjonInnvilget)))
    }
}