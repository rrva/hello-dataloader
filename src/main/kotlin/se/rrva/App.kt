package se.rrva

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import createGraphQl
import graphql.ExecutionInput
import graphql.GraphQL
import graphql.execution.preparsed.PreparsedDocumentEntry
import org.jooby.AsyncMapper
import org.jooby.handlers.CorsHandler
import org.jooby.json.Jackson
import org.jooby.run
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object App {

    private val log: Logger = LoggerFactory.getLogger(App.javaClass)

    private val om = ObjectMapper().apply {
        registerKotlinModule()
        registerModule(Jdk8Module())
        registerModule(JavaTimeModule())
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        run(*args) {
            use(Jackson(om))
            use("*", CorsHandler())
            map(AsyncMapper())

            var repository: ContentRepository?
            var graphQl: GraphQL? = null
            val preParsedQueryCache: Cache<String, PreparsedDocumentEntry> =
                Caffeine.newBuilder().maximumSize(10_000).build()

            onStart { _ ->
                repository = require(ContentRepository::class.java)
                graphQl = createGraphQl(repository!!, preParsedQueryCache)
            }

            assets("/", "index.html")
            assets("/**")

            options("/graphql") { _, rsp, _ ->
                rsp.header("Access-Control-Allow-Origin", "*")
                rsp.header("Access-Control-Allow-Credentials", "true")
                rsp.header("Access-Control-Allow-Headers", "content-type, authorization")
                rsp.header("Access-Control-Allow-Methods", "DELETE, GET, OPTIONS, PATCH, POST, PUT")
                rsp.status(204)
                rsp.send("hej")

            }

            post("/graphql") { req ->
                log.debug("Received graphql query")
                val body = req.body(GraphRequest::class.java)
                val (query, variables) = body
                val executionInput = ExecutionInput.Builder()
                    .query(query)
                    .variables(variables)
                graphQl!!.executeAsync(executionInput).thenApply { it.toSpecification() }
            }
        }
    }

}

data class GraphRequest(
    val query: String,
    val variables: Map<String, Any>?
)

