package no.nav.pensjon.brev.pdfbygger

data class PdfCompilationInput(val files: Map<String, /*base64*/ String>)