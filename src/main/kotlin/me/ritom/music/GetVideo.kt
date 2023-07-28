package me.ritom.music

import com.google.api.services.youtube.model.ResourceId
import com.google.api.services.youtube.model.SearchResult
import com.google.api.services.youtube.model.Thumbnail


class GetVideo {
    fun getVideos(quary:String) {
        val builder = YoutubeBuilder()
        val youtube = builder.getService()
        val search = youtube.search().list("id,snippet")
        search.key="API"
        search.q = quary
        search.type = "Video"
        search.maxResults=25
        val response = search.execute()
        val results = response.items
        if (results!=null) {
            prettyPrint(results.iterator(),quary)
        }
    }
    private fun prettyPrint(iteratorSearchResults: Iterator<SearchResult>, query: String) {
        println("\n=============================================================")
        System.out.println(
            "   First 25 videos for search on \"$query\"."
        )
        println("=============================================================\n")
        if (!iteratorSearchResults.hasNext()) {
            println(" There aren't any results for your query.")
        }
        while (iteratorSearchResults.hasNext()) {
            val singleVideo: SearchResult = iteratorSearchResults.next()
            val rId: ResourceId = singleVideo.id

            // Confirm that the result represents a video. Otherwise, the
            // item will not contain a video ID.
            if (rId.kind == "youtube#video") {
                val thumbnail = singleVideo.snippet.thumbnails.default
                println(" Video Id" + rId.videoId)
                System.out.println(" Title: " + singleVideo.getSnippet().getTitle())
                println(" Thumbnail: " + thumbnail.url)
                println("\n-------------------------------------------------------------\n")
            }
        }
    }
}