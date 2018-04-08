package se.rrva

import se.rrva.graphql.*
import javax.inject.Singleton

@Singleton
class ContentRepository {



    fun myContent(n : Int?): MyContent {

        return MyContent(all = createContent(n))
    }

    private fun createContent(n: Int?): List<TvSeries> {
        return (1..(n ?: 10)).map { i ->
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
        }
    }

    fun contentByGenre(genre: Genre): List<TvSeries> {
        return createContent(10)
    }

    fun recommendedByGenre(n:Int): List<PromotedItem> {
        return (1..(n ?: 10)).map { i ->
            PromotedItem(i.toString(), "Item $i", "Lorem ipsum", "Lorem ipsum dolor sit amet")
        }
    }



}