import me.ritom.music.download.YoutubeDownloader
import me.ritom.music.getters.GetVideo
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val arguments = args.toList()
    var title:String?=null
    var outputDirectory:String? = null
    println("Running with args ${args.joinToString()}")
    for (s in arguments) {
        if (s.startsWith("--name=")) {
            title = s.split("=")[1]
        }
        if (s.startsWith("--o=")) {
            outputDirectory=s.split("=")[1]
        }
    }
    if (outputDirectory!=null&&Files.isRegularFile(Paths.get(outputDirectory))){
        outputDirectory=null
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
    val down = YoutubeDownloader()
    if (playList) {
        if (outputDirectory!=null) {
            down.downloadPlaylist(title,outputDirectory)
        }
        else down.downloadPlaylist(title)
    }
    else {
        val getVideo = GetVideo()
        val videos = getVideo.getVideos(title)
        if(downloadAll) {
            println("Downloading all matching musics with $title")
            for (video in videos) {
                if (outputDirectory!=null) {
                    down.downloadPlaylist(title,outputDirectory)
                }
                else down.downloadVideo(video)
            }
        }
        else {
            println("Downloading $title")
            if (outputDirectory!=null) {
                down.downloadPlaylist(title,outputDirectory)
            }
            else down.downloadVideo(videos[0])
        }
    }
}