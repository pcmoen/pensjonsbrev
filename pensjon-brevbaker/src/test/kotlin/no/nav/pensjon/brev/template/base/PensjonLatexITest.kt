package no.nav.pensjon.brev.template.base

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.TestTags
import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.latex.PdfCompilationInput
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.dsl.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.io.File
import java.util.*

data class TestTemplateDto(val etNavn: String)

@Tag(TestTags.PDF_BYGGER)
class PensjonLatexITest {

    val brevData = TestTemplateDto("Ole")

    val template = createTemplate(
        name = "test-template",
        base = PensjonLatex,
        letterDataType = TestTemplateDto::class,
        lang = languages(Bokmal),
        title = newText(Bokmal to "En fin tittel"),
        letterMetadata = LetterMetadata("En fin display tittel")
    ) {
        outline {
            text(Bokmal to "Argumentet etNavn er: ")
            eval { argument().select(TestTemplateDto::etNavn) }
        }
    }

    @Test
    fun canRender() {
        Letter(template, brevData, Bokmal, Fixtures.felles)
            .render()
            .let { PdfCompilationInput(it.base64EncodedFiles()) }
            .let { LaTeXCompilerService().producePDF(it).base64PDF }
            .also {
                val file = File("build/test_pdf/pensjonLatexITest_canRender.pdf")
                file.parentFile.mkdirs()
                file.writeBytes(Base64.getDecoder().decode(it))
                println("Test-file written to: ${file.path}")
            }
    }

}