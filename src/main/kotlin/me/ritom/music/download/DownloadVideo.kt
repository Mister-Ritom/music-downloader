package me.ritom.music.download

import java.io.File
import java.util.*

class DownloadVideo {

    fun downloadVideo(id:String,outputDirectory:String = getMusicFolderPath()) {
        println("Downloading $id to $outputDirectory")
        val videoUrl = "https://www.youtube.com/watch?v=$id"
        val url = DownloadVideo::class.java.getResource("client_secret.json")
        if (url != null) {
            println(url.path)
        }

        try {
            val processBuilder = ProcessBuilder("python",
                "/home/ritom/Downloads/yt-dlp",
                "--extract-audio", // Extract audio only
                "--audio-format", "mp3",
                videoUrl)
            processBuilder.redirectErrorStream(true)
            processBuilder.directory(File(outputDirectory))

            val process = processBuilder.start()
            process.waitFor()

            println("YouTube to MP3 conversion complete!")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getMusicFolderPath(): String {
        val os = System.getProperty("os.name").lowercase(Locale.getDefault())

        return when {
            os.contains("win") -> {
                // On Windows, the "Music" folder is usually under the user's profile directory
                val userProfile = System.getenv("USERPROFILE") ?: ""
                "$userProfile\\Music"
            }
            os.contains("mac") || os.contains("nix") || os.contains("nux") -> {
                // On macOS and Linux, the "Music" folder is under the user's home directory
                val userHome = System.getProperty("user.home") ?: ""
                "$userHome/Music"
            }
            else -> {
                // If the OS is not recognized, you can return a default path or an empty string
                "" // Or any other default path as per your requirement
            }
        }
    }

}