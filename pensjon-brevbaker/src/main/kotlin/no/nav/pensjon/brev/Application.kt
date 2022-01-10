package no.nav.pensjon.brev

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.jackson.*
import io.ktor.metrics.micrometer.*
import io.ktor.request.*
import no.nav.pensjon.brev.template.brevbakerConfig
import org.slf4j.event.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun requireEnv(key: String) =
    System.getenv(key) ?: throw IllegalStateException("The environment variable $key is missing.")

@Suppress("unused") // Referenced in application.conf
fun Application.module() {

    install(CallLogging) {
        callIdMdc("x_correlationId")
    }

    install(CallId) {
        retrieveFromHeader("Nav-Call-Id")
        generate()
        verify { it.isNotEmpty() }
    }

    install(ContentNegotiation) {
        jackson {
            brevbakerConfig()
        }
    }

    val jwtConfigs = listOf(JwtConfig.requireAzureADConfig(), JwtConfig.requireStsConfig())
    install(Authentication) {
        jwtConfigs.forEach {
            brevbakerJwt(it)
        }
    }

    install(MicrometerMetrics) {
        registry = Metrics.prometheusRegistry
    }

    brevbakerRouting(jwtConfigs.map { it.name }.toTypedArray())
}