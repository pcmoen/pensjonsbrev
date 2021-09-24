package no.nav.pensjon.brev.api

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.TestTags
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.latex.PdfCompilationInput
import no.nav.pensjon.brev.template.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

@Tag(TestTags.PDF_BYGGER)
class TemplateResourceITest {

    @Test
    fun `all templates can render and compile`() {
        TemplateResource.getTemplates()
            .associateWith { TemplateResource.getTemplate(it) }
            .forEach { testTemplate(it.key, it.value) }
    }

    private fun testTemplate(name: String, template: LetterTemplate<*, *>?) {
        if (template == null) {
            fail { "TemplateResource.getTemplates() returned a template name that doesnt exist: $name" }
        }
        val letter = createLetter(template)
        val rendered = render(letter)
        try {
            compile(rendered)
        } catch (failedCompile: Exception) {
            fail("Failed to compile template($name) with argument: ${letter.argument}", failedCompile)
        }
    }

    private fun compile(rendered: RenderedLetter) {
        val input = PdfCompilationInput(rendered.base64EncodedFiles())
        LaTeXCompilerService().producePDF(input)
    }

    private fun render(letter: Letter<Any>): RenderedLetter =
        try {
            letter.render()
        } catch (renderFailed: Exception) {
            fail("Failed to render latex for template: ${letter.template.name}", renderFailed)
        }

    private fun createLetter(template: LetterTemplate<*, *>): Letter<Any> = Letter(
        template = template,
        argument = createArgument(template.letterDataType),
        language = selectLang(template.language),
        felles = Fixtures.felles
    )

    private fun createArgument(letterDataType: KClass<out Any>): Any {
        try {
            return letterDataType.createInstance()
        } catch (noDefaultConstruct: IllegalArgumentException) {
            fail("LetterData data class doesn't have no-arg constructor for test data: ${letterDataType.qualifiedName}", noDefaultConstruct)
        }
    }

    private fun selectLang(lang: LanguageCombination): Language = when (lang) {
        is LanguageCombination.Single<*> -> lang.first
        is LanguageCombination.Double<*, *> -> lang.first
        is LanguageCombination.Triple<*, *, *> -> lang.first
    }

}