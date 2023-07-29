package me.ritom.music

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.youtube.YouTube
import java.io.IOException
import java.io.InputStreamReader
import java.security.GeneralSecurityException


class YoutubeBuilder {

    private val CLIENT_SECRETS = "client_secret.json"
    private val SCOPES: Collection<String> = mutableListOf("https://www.googleapis.com/auth/youtube.readonly")

    private val APPLICATION_NAME = "Music Downloader"
    private val JSON_FACTORY: JsonFactory = JacksonFactory.getDefaultInstance()

    /**
     * Create an authorized Credential object.
     *
     * @return an authorized Credential object.
     * @throws IOException
     */
    @Throws(IOException::class)
    fun authorize(httpTransport: NetHttpTransport?): Credential? {
        // Load client secrets.
        val stream = YoutubeBuilder::class.java.getResourceAsStream(CLIENT_SECRETS)
        if (stream!=null) {
            val clientSecrets =
                GoogleClientSecrets.load(
                    JSON_FACTORY,
                    InputStreamReader(stream)
                )
            // Build flow and trigger user authorization request.
            val flow =
                GoogleAuthorizationCodeFlow.Builder(
                    httpTransport,
                    JSON_FACTORY,
                    clientSecrets,
                    SCOPES
                )
                    .build()
            return AuthorizationCodeInstalledApp(flow, LocalServerReceiver()).authorize("user")
        }
        return null
    }

    fun getApiKey():String {
        return "API_KEY"
    }

    @Throws(GeneralSecurityException::class, IOException::class)
    fun getService(): YouTube {
        val httpTransport = GoogleNetHttpTransport.newTrustedTransport()
        val credential = authorize(httpTransport)
        return YouTube.Builder(httpTransport, JSON_FACTORY, credential)
            .setApplicationName(APPLICATION_NAME)
            .build()
    }
}