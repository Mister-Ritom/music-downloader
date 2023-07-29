package me.ritom.music.getters

import me.ritom.music.YoutubeBuilder
import me.ritom.music.models.YoutubeVideo

class GetPlaylist {
    fun getVideosFromPlayList(id:String):List<YoutubeVideo> {
        val builder = YoutubeBuilder()
        val youtube = builder.getService()
        val list:MutableList<YoutubeVideo> = ArrayList()
        // Set up the initial playlist items request
        val playlistItemsRequest = youtube.playlistItems()
            .list("snippet,contentDetails")
            .setKey(builder.getApiKey())
            .setPlaylistId(id)

        // Keep track of all video IDs
        var nextPageToken: String? = null
        do {
            playlistItemsRequest.pageToken = nextPageToken
            val playlistItemsResponse = playlistItemsRequest.execute()
            val playlistItems = playlistItemsResponse.items

            // Process each video in the current page
            playlistItems?.forEach { playlistItem ->
                val videoId = playlistItem.contentDetails.videoId
                val thumbnail = playlistItem.snippet.thumbnails.default
                val youtubeVideo = YoutubeVideo(videoId, thumbnail)
                list.add(youtubeVideo)
            }

            nextPageToken = playlistItemsResponse.nextPageToken
        } while (nextPageToken != null)
    return list
    }
}