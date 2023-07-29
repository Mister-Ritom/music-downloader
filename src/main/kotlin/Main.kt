import me.ritom.music.download.DownloadVideo
import me.ritom.music.getters.GetPlaylist
import me.ritom.music.getters.GetVideo
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val arguments = args.toList()
    var title:String?=null
    println("Running with args ${args.joinToString()}")
    for (s in arguments) {
        if (s.startsWith("--name")) {
            title = s.split("=")[1]
            break
        }
    }
    if (title==null) {
        println("Use --name to search for songs ")
        exitProcess(1)
    }
    val playList = arguments.contains("--playlist")
    if (playList) println("Downloading all musics of $title")
    else println("Searching for musics of $title")
    var downloadAll = false
    if (arguments.contains("--downloadall"))downloadAll=true
    val down = DownloadVideo()
    if (playList) {
        val getPlaylist = GetPlaylist()
        val videos = getPlaylist.getVideosFromPlayList(title)
        for (video in videos) {
            down.downloadVideo(video.id)
        }
    }
    else {
        val getVideo = GetVideo()
        val videos = getVideo.getVideos(title)
        if(downloadAll) {
            println("Downloading all matching musics with $title")
            for (video in videos) {
                down.downloadVideo(video.id)
            }
        }
        else {
            println("Downloading $title")
            down.downloadVideo(videos[0].id)
        }
    }
}