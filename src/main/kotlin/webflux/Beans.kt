package webflux

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class Beans {

    @Bean
    fun webClient() = WebClient.create()

    @Bean
    fun restTempalte() = RestTemplate()
}