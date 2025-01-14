package no.nav.pensjon.brev.pdfbygger

import java.io.File
import java.nio.file.Path
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.io.path.absolutePathString
import kotlin.io.path.createTempDirectory
import kotlin.io.path.nameWithoutExtension

private const val COMPILATION_RUNS = 2

class LaTeXService {
    private val decoder = Base64.getDecoder()
    private val encoder = Base64.getEncoder()

    fun producePDF(latexFiles: Map<String, String>): PDFCompilationResponse {
        val tmpDir = createTempDirectory()

        latexFiles.forEach {
            tmpDir.resolve(it.key).toFile().apply {
                createNewFile()
                writeBytes(decoder.decode(it.value))
            }
        }

        return try {
            createLetter(tmpDir)
        } finally {
            tmpDir.toFile().deleteRecursively()
        }
    }

    private fun createLetter(executionFolder: Path): PDFCompilationResponse {

        //Compile multiple times to resolve references such as number of pages
        val result = (1..COMPILATION_RUNS)
            .map { executeCompileProcess(executionFolder) }
            .lastOrNull() ?: throw IllegalStateException("Did not attempt to compile pdf from latex, compilation runs: ${1..COMPILATION_RUNS}")

        return when (result) {
            is Execution.Success ->
                result.pdf.toFile().readBytes()
                    .let { encoder.encodeToString(it) }
                    .let { PDFCompilationResponse.Base64PDF(it) }

            is Execution.Failure.Compilation ->
                PDFCompilationResponse.Failure.Client(reason = "PDF compilation failed", output = result.output, error = result.error)

            is Execution.Failure.Execution -> {
                //TODO: log exception
                PDFCompilationResponse.Failure.Server(reason = "Compilation process execution failed: see logs")
            }

            is Execution.Failure.Timeout ->
                PDFCompilationResponse.Failure.Server(reason = "Compilation timed out - spent more than: ${result.timeout} ${result.unit}")
        }
    }

    private fun executeCompileProcess(
        workingDir: Path,
        texFilename: String = "letter",
        timeout: Long = 30,
        timeoutUnit: TimeUnit = TimeUnit.SECONDS,
        output: Path = workingDir.resolve("process.out"),
        error: Path = workingDir.resolve("process.err"),
    ): Execution =
        runCatching {
            ProcessBuilder(*("xelatex --interaction=nonstopmode -halt-on-error $texFilename.tex").split(" ").toTypedArray())
                .directory(workingDir.toFile())
                .redirectOutput(output.toFile())
                .redirectError(error.toFile())
                .start()
                .let {
                    if (!it.waitFor(timeout, timeoutUnit)) {
                        it.destroy()
                        Execution.Failure.Timeout(timeout, timeoutUnit)
                    } else if (it.exitValue() == 0) {
                        Execution.Success(pdf = workingDir.resolve("${File(texFilename).nameWithoutExtension}.pdf"))
                    } else {
                        Execution.Failure.Compilation(output = output.toFile().readText(), error = error.toFile().readText())
                    }
                }
        }.getOrElse { Execution.Failure.Execution(it) }
}

private sealed class Execution {
    data class Success(val pdf: Path) : Execution()
    sealed class Failure : Execution() {
        data class Timeout(val timeout: Long, val unit: TimeUnit) : Failure()
        data class Compilation(val output: String, val error: String) : Failure()
        data class Execution(val cause: Throwable) : Failure()
    }
}