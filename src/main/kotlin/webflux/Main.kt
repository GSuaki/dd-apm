package webflux

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import reactor.core.publisher.Hooks
import java.util.*

@SpringBootApplication
class Application

fun main(args: Array<String>) {
    setupEnvironment()

    runApplication<Application>(*args) {
        Hooks.onOperatorDebug()
    }
}

private fun setupEnvironment() {
    TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"))
}