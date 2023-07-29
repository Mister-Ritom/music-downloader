package me.ritom.music.getters

import me.ritom.music.YoutubeBuilder
import me.ritom.music.models.YoutubeVideo
import kotlin.streams.toList


class GetVideo {
    fun getVideos(quary:String,maxResults:Long=10):List<YoutubeVideo> {
        val builder = YoutubeBuilder()
        val youtube = builder.getService()
        val search = youtube.search().list("id,snippet")
            .setKey(builder.getApiKey())
            .setQ(quary)
            .setType("Video")
            .setMaxResults(maxResults)
        val response = search.execute()
        val results = response.items
        return results.stream().map {
            YoutubeVideo(it.snippet.title,it.id.videoId, it.snippet.thumbnails.default)
        }.toList()
    }
}