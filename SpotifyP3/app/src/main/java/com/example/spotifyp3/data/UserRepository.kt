package com.example.spotifyp3.data

import androidx.lifecycle.LiveData

class UserRepository(private val userDao: UserDao) {
    val readAllData: LiveData<List<UserEntity>> = userDao.readAllData()

    suspend fun addUser(user: UserEntity){
        userDao.addUser(user)
    }
}