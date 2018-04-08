import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.coxautodev.graphql.tools.GraphQLResolver
import com.github.benmanes.caffeine.cache.Cache
import graphql.GraphQL
import graphql.execution.preparsed.PreparsedDocumentEntry
import se.rrva.ContentRepository
import se.rrva.graphql.*

fun createGraphQl(
    repository: ContentRepository,
    preParsedQueryCache: Cache<String, PreparsedDocumentEntry>
): GraphQL {

    val schema = com.coxautodev.graphql.tools.SchemaParser.newParser().file("schema.graphqls")
        .resolvers(
            QueryResolver(repository),
            GenreResolver(repository)
        )
        .build()

    return GraphQL.newGraphQL(schema.makeExecutableSchema()).build()
}

class QueryResolver(private val repo: ContentRepository) : GraphQLQueryResolver {
    fun myContent(
        n: Int?
    ) = repo.myContent(n)
}

class GenreResolver(private val repo: ContentRepository) : GraphQLResolver<Genre> {


    fun content(genre: Genre): List<TvSeries> {
        return repo.contentByGenre(genre)
    }

    fun recommended(genre: Genre): List<PromotedItem> {
        return repo.recommendedByGenre(10)
    }


}
