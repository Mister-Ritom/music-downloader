package me.ritom.music

import com.google.api.services.youtube.model.SearchResult
import kotlin.streams.toList


class GetVideo {
    fun getVideos(quary:String):List<YoutubeVideo> {
        val builder = YoutubeBuilder()
        val youtube = builder.getService()
        val search = youtube.search().list("id,snippet")
        search.key = "API_KEY"
        search.q = quary
        search.type = "Video"
        search.maxResults = 25
        val response = search.execute()
        val results = response.items
        if (results != null) {
            prettyPrint(results.iterator(), quary)
        }
        return results.stream().map {
            YoutubeVideo(it.id.videoId, it.snippet.thumbnails.default)
        }.toList()
    }
    private fun prettyPrint(iteratorSearchResults: Iterator<SearchResult>, query: String) {
        println("\n=============================================================")
        println(
            "   First 25 videos for search on \"$query\"."
        )
        println("=============================================================\n")
        if (!iteratorSearchResults.hasNext()) {
            println(" There aren't any results for your query.")
        }
        while (iteratorSearchResults.hasNext()) {
            val singleVideo: SearchResult = iteratorSearchResults.next()
            val rId = singleVideo.id

            // Confirm that the result represents a video. Otherwise, the
            // item will not contain a video ID.
            if (rId.kind == "youtube#video") {
                val thumbnail = singleVideo.snippet.thumbnails.default
                println(" Video Id: " + rId.videoId)
                println(" Title: " + singleVideo.snippet.title)
                println(" Thumbnail: " + thumbnail.url)
                println("\n-------------------------------------------------------------\n")
            }
        }
    }
}