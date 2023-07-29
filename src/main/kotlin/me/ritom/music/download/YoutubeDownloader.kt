package me.ritom.music.download

import me.ritom.music.models.YoutubeVideo
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.util.*


class YoutubeDownloader {

    fun downloadVideo(video:YoutubeVideo,outputDirectory:String = getMusicFolderPath()) {
        println("Downloading ${video.name} to $outputDirectory")
        val videoUrl = "https://www.youtube.com/watch?v=${video.id}"
        try {
            val processBuilder = buildProcess(videoUrl,outputDirectory)

            val process = processBuilder.start()
            runProcess(process)

            println("YouTube to MP3 conversion complete!")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun buildProcess(url:String, runningDirectory:String):ProcessBuilder {
         return ProcessBuilder("python",
            "/home/ritom/Downloads/yt-dlp",
            "--extract-audio", // Extract audio only
            "--audio-format", "mp3",
            url).redirectErrorStream(true).directory(File(runningDirectory))
    }

    private fun runProcess(process:Process) {

        // Get the process output stream

        // Get the process output stream
        val inputStream = process.inputStream
        val reader = BufferedReader(InputStreamReader(inputStream))

        // Read the output

        // Read the output
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            println(line)
        }

        // Wait for the process to complete

        // Wait for the process to complete
        val exitCode: Int = process.waitFor()
        println("Process exited with code: $exitCode")
    }

    fun downloadPlaylist(id:String,outputDirectory:String = getMusicFolderPath()) {
        println("Downloading playList $id to $outputDirectory")
        val playlistUrl = "https://www.youtube.com/playlist?list=$id"
        try {
            val processBuilder = buildProcess(playlistUrl,outputDirectory)
            println("Running ${processBuilder.command().joinToString(" ")}")
            val process = processBuilder.start()
            runProcess(process)
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