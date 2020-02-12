package webflux.user

import com.github.javafaker.Faker
import datadog.trace.api.Trace
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono
import reactor.core.scheduler.Schedulers
import java.util.*
import com.newrelic.api.agent.Trace as NRTrace

@RestController
@RequestMapping(value = ["users"])
class UsersController(
    private val faker: Faker = Faker(),
    private val webClient: WebClient,
    private val restTemplate: RestTemplate
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @GetMapping
    @Trace
    fun findAll(): Mono<OrderUser> {
        return getMockify()
            .flatMap { generateUser() }
            .flatMap { getMockifyResttemplate() }
            .flatMap { Mono.zip(getMockify(), getMockify()) }
            .map { OrderUser(it.t2, it.t1) }
    }

    @Trace
    private fun getMockify(): Mono<String> {
        return webClient.get()
            .uri("http://www.mocky.io/v2/5185415ba171ea3a00704eed")
            .retrieve()
            .bodyToMono(String::class.java)
            .doOnNext { log.info("Mockify: {}", it) }
    }

    @Trace
    private fun getMockifyResttemplate(): Mono<String> {
        return Mono.fromCallable {
            restTemplate.getForObject("http://www.mocky.io/v2/5185415ba171ea3a00704eed", String::class.java)
        }
            .doOnNext { log.info("Mockify: {}", it) }
            .subscribeOn(Schedulers.elastic())
    }

    @Trace
    private fun generateUser(): Mono<User> = Mono.defer {
        User(
            id = UUID.randomUUID().toString(),
            name = faker.name().fullName(),
            age = faker.number().numberBetween(15, 60)
        ).toMono()
    }
        .subscribeOn(Schedulers.elastic())
}