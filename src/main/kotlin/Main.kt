import me.ritom.downloader.YoutubeDownloader
import me.ritom.downloader.YtDlpDownloader
import me.ritom.music.getters.GetVideo
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val arguments = args.toList()
    var title:String?=null
    var outputDirectory:String=getDefaultDownloads()
    var ytDlp: File? = null
    println("Running with args ${args.joinToString()}")
    for (s in arguments) {
        if (s.startsWith("--name=")) {
            title = s.split("=")[1]
        }
        if (s.startsWith("--o=")) {
            outputDirectory=s.split("=")[1]
        }
        if (s.startsWith("--ytdlp=")) {
            ytDlp=File(s.split("=")[1])
        }
    }
    if (Files.isRegularFile(Paths.get(outputDirectory))){
        println("Detected file path as $outputDirectory using default directory instead")
        outputDirectory=getDefaultDownloads()
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
    if (ytDlp==null||ytDlp.isDirectory) {
        ytDlp=YtDlpDownloader().downloadYtDlp("$outputDirectory/ytdlp")
    }
    val down = YoutubeDownloader(ytDlp,outputDirectory)
    if (playList) {
        down.downloadPlaylist(title)
    }
    else {
        val getVideo = GetVideo()
        val videos = getVideo.getVideos(title)
        if(downloadAll) {
            println("Downloading all matching musics with $title")
            for (video in videos) {
                down.downloadSingle(title)
            }
        }
        else {
            println("Downloading $title")
            down.downloadSingle(title)
        }
    }
}
private fun getDefaultDownloads(): String {
    val os = System.getProperty("os.name").lowercase(Locale.getDefault())

    return when {
        os.contains("win") -> {
            val userProfile = System.getenv("USERPROFILE") ?: ""
            "$userProfile\\Music"
        }
        os.contains("mac") || os.contains("nix") || os.contains("nux") -> {
            val userHome = System.getProperty("user.home") ?: ""
            "$userHome/Music"
        }
        else -> {
            "Music/"
        }
    }
}