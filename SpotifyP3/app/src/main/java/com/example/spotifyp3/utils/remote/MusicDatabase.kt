package com.example.spotifyp3.utils.remote

import com.example.spotifyp3.model.Song
import com.example.spotifyp3.utils.Constants.Url.SONG_COLLECTION
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class MusicDatabase {

    private val fireStore = FirebaseFirestore.getInstance()
    private val songCollection = fireStore.collection(SONG_COLLECTION)

    suspend fun getAllSongs(): List<Song>{
        return try {
            songCollection.get().await().toObjects(Song::class.java)
        } catch (e: java.lang.Exception){
            emptyList()
        }
    }

}