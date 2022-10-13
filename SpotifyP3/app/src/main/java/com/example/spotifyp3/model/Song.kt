package com.example.spotifyp3.model

data class Song(
    var artist: String? = null,
    var title: String? = null,
    var mediaID: String? = null,
    var songUrl: String? = null,
    var albumUrl: String? = null
)