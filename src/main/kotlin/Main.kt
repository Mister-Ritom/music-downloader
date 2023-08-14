import me.ritom.music.getters.GetVideo
import site.ritom.youtubedownloader.YoutubeDownloader
import site.ritom.youtubedownloader.YtDlpDownloader
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val arguments = args.toList()
    var id:String?=null
    var outputDirectory:String? = null
    println("Running with args ${args.joinToString()}")
    for (s in arguments) {
        if (s.startsWith("--name=")) {
            id = s.split("=")[1]
        }
        if (s.startsWith("--o=")) {
            outputDirectory=s.split("=")[1]
        }
    }
    if (outputDirectory!=null&&Files.isRegularFile(Paths.get(outputDirectory))){
        outputDirectory=null
    }
    if (id==null) {
        println("Use --name to search for songs ")
        exitProcess(1)
    }
    val playList = arguments.contains("--playlist")
    if (playList) println("Downloading all musics of $id")
    else println("Searching for musics of $id")
    var downloadAll = false
    if (arguments.contains("--downloadall"))downloadAll=true


    //Output directory
    val output = outputDirectory ?: "./Downloads"

    //Download Ytdlp
    val ytdlp = YtDlpDownloader().downloadYtDlp("./Ytdlp")
    //Instance of downloader
    val youtubeDownloader = YoutubeDownloader(ytdlp,output)

    //Download playlist
    if (playList) {
        youtubeDownloader.downloadPlaylist(id)
    }
    //Download video
    else {
        val getVideo = GetVideo()
        val videos = getVideo.getVideos(id)
        //Downloads all matching video
        if(downloadAll) {
            println("Downloading all matching musics with name $id")
            for (video in videos) {
                    youtubeDownloader.downloadSingle(video.id)
            }
        }
        //Download only first video from search
        else {
            println("Downloading $id")
            youtubeDownloader.downloadSingle(videos[0].id)
        }
    }
}