package com.example.spotifyp3.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class User (val userName: String? = null, val userId: String? = null, val userImage: String? = null)