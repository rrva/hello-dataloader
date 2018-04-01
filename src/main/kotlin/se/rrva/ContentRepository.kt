package se.rrva

import se.rrva.graphql.Genre
import se.rrva.graphql.MyContent
import se.rrva.graphql.PromotedItem
import javax.inject.Singleton

@Singleton
class ContentRepository {
    fun promotedByGenres(genres: List<Genre>): List<List<PromotedItem>>? {
        return emptyList()
    }

    fun myContent(): MyContent {
        return MyContent(all = emptyList())
    }

}