package rsocket

import org.junit.jupiter.api.Assertions.assertEquals
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import rsocket.chat.ChannelClient
import rsocket.clients.ReqResClient
import rsocket.clients.ReqStreamClient
import rsocket.clients.Server
import java.util.concurrent.CountDownLatch


object MainTest : Spek({

    describe("RSocket") {

        val server = Server()

        context("ReqResClient") {

            val client = ReqResClient()

            it("call blocking with valid payload") {

                val string = "Hello RSocket"

                assertEquals(string, client.callBlocking(string))
                assertEquals(string, client.callBlocking(string))

                client.dispose()
                server.dispose()
            }
        }

        context("ReqStreamClient") {

            val client = ReqStreamClient()

            it("call with valid payload") {
                val countDownLatch = CountDownLatch(1)

                val subscription = client.dataStream
                    .index()
                    .subscribe({
                        println("Received: ${it.t2}")
                    }, {
                        it.printStackTrace()
                    }, {

                    })

                countDownLatch.await()

                subscription.dispose()
                server.dispose()
            }
        }

        context("ChannelClient") {

            val client = ChannelClient()

            it("call with valid payload") {
                client.enterChat()
                client.dispose()
            }
        }
    }
})