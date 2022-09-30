package com.example.projectmotivation.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class User (val userId: String? = null, val userName: String? = null, val userImage: String? = null)