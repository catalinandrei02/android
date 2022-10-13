package com.example.spotifyp3.utils

object Constants {

    object Url {
        const val SONG_COLLECTION = "songs"
        const val USER = "USER"
    }

    object Error {
        const val EMAIL = "The e-mail you entered is not valid!"
        const val NAME = "Name can't be empty"
        const val CHAR_8 = "must contain at least 8 characters!"
        const val EMPTY = "can't be empty!"
    }
    object TAG{
        const val LOGIN = "LoginFragment"
        const val REGISTER = "RegisterFragment"
        const val SERVICE_TAG = "MusicService"
    }

    const val NOTIFICATION_CHANNEL_ID = "music"
    const val NOTIFICATION_ID = 1
}