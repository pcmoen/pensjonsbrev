package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.template.Language

internal val bokmalTittel = newText(Language.Bokmal to "test brev")
internal val nynorskTittel = newText(Language.Nynorsk to "test brev")
internal val testLetterMetadata = LetterMetadata(
    displayTitle = "En fin display tittel",
    isSensitiv = false,
)

internal data class SomeDto(val name: String, val pensjonInnvilget: Boolean, val kortNavn: String? = null)