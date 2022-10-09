package com.example.projectmotivation.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class User (val name: String? = null, val userId: String? = null, val userImage: String? = null)