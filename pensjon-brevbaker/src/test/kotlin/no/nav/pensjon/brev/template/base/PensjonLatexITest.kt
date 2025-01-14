package no.nav.pensjon.brev.template.base

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.isEmpty
import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.*
import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.latex.PdfCompilationInput
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.select
import no.nav.pensjon.brev.template.latexEscape
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.opentest4j.AssertionFailedError
import kotlin.collections.ArrayList

data class TestTemplateDto(val etNavn: String)

@Tag(TestTags.PDF_BYGGER)
class PensjonLatexITest {

    val brevData = TestTemplateDto("Ole")
    @Test
    fun canRender() {
        val template = createTemplate(
            name = "test-template",
            base = PensjonLatex,
            letterDataType = TestTemplateDto::class,
            title = newText(Bokmal to "En fin tittel"),
            letterMetadata = LetterMetadata(
                displayTitle = "En fin display tittel",
                isSensitiv = false,
            )
        ) {
            outline {
                text(Bokmal to "Argumentet etNavn er: ")
                eval { argument().select(TestTemplateDto::etNavn) }
            }
        }
        Letter(template, brevData, Bokmal, Fixtures.felles)
            .render()
            .let { PdfCompilationInput(it.base64EncodedFiles()) }
            .let { LaTeXCompilerService(PDF_BUILDER_URL).producePdfSync(it).base64PDF }
            .also { writeTestPDF("pensjonLatexITest_canRender" ,it) }
    }

    @Test
    fun `Ping pdf builder`() {
        runBlocking { LaTeXCompilerService(PDF_BUILDER_URL).ping()}
    }

    @Test
    fun `try different characters to attempt escaping LaTeX`() {
        val invalidCharacters = ArrayList<Int>()
        isValidCharacters(0,Char.MAX_VALUE.code, invalidCharacters)
        if(invalidCharacters.isNotEmpty()) {
            throw AssertionFailedError(
                """
                    Escaped characters managed to crash the letter compilation:
                    ${invalidCharacters.joinToString()}}
                """.trimIndent()
            )
        }
        assertThat(invalidCharacters, isEmpty)


    }

    private fun isValidCharacters(begin: Int, end: Int, invalidCharacters: ArrayList<Int>) {
        if (testCharacters(addChars(begin, end))) {
            //All characters are valid
            return
        }else {
            if (begin - end == 0) {
                //Failed at single character
                invalidCharacters.add(begin)
                return
            }
            // there is some invalid character in the range
            val separationPoint = begin + ((end - begin) / 2)
            isValidCharacters(begin, separationPoint, invalidCharacters)
            isValidCharacters(separationPoint + 1, end, invalidCharacters)
            return
        }
    }

    private fun testCharacters(addChars: String): Boolean {
        try {
            val testTemplate = createTemplate(
                name = "test-template",
                base = PensjonLatex,
                letterDataType = TestTemplateDto::class,
                title = newText(Bokmal to "En fin tittel"),
                letterMetadata = LetterMetadata(
                    displayTitle = "En fin display tittel",
                    isSensitiv = false,
                )
            ) {
                outline {
                    text(Bokmal to addChars.latexEscape() + "test")
                    eval { argument().select(TestTemplateDto::etNavn) }
                }
            }

            Letter(testTemplate, brevData, Bokmal, Fixtures.felles)
                .render()
                .let { PdfCompilationInput(it.base64EncodedFiles()) }
                .let { LaTeXCompilerService(PDF_BUILDER_URL).producePdfSync(it).base64PDF }
            return true
        } catch (e: Throwable) {
            return false
        }
    }

    private fun addChars(from: Int, to: Int): String {
        val stringBuilder = StringBuilder()
        for (i in from..to) {
            stringBuilder.append(Char(i)).append(" ")
        }
        return stringBuilder.toString()
    }
}