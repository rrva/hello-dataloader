package se.rrva

import se.rrva.graphql.*
import javax.inject.Singleton

@Singleton
class ContentRepository {
    fun promotedByGenres(genres: List<Genre>): List<List<PromotedItem>>? {
        return listOf(listOf(PromotedItem("123", "foo-123", "Foo", "Lorem ipsum", "Lorem ipsum dolor sit amet")))
    }

    fun myContent(n : Int?): MyContent {

        return MyContent(all = (1..(n?:10)).map { i ->
            TvSeries(i.toString(), "Series $i", listOf(
                Genre(
                    "drama",
                    "Drama"
                )
            ), (1..2).map { seasonNo ->
                Season("s$seasonNo", "Season $seasonNo", seasonNo, (1..10).map { episodeNo ->
                    Episode(
                        "S${seasonNo}E$episodeNo", "Episode $episodeNo", listOf(
                            Genre(
                                "drama",
                                "Drama"
                            )
                        ), false, episodeNo % 2 == 0
                    )
                })
            })
        })
    }

}