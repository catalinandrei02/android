package com.example.projectmotivation.utils

object Constants {

    object Url {
        const val IMAGE = "image/"
        const val USER = "users"
        const val USER_CHAT = "/users"
        const val CHAT = "Chat"
    }
    object Error {
        const val EMAIL = "The e-mail you entered is not valid!"
        const val NAME = "Name can't be empty"
        const val PASSWORD_CONFIRM = "Password doesn't match!"
        const val CHAR_8 = "must contain at least 8 characters!"
        const val CHAR_6 = "must contain at least 6 characters!"
        const val EMPTY = "can't be empty!"
    }
    object Text {
        const val UPLOAD = "Uploading"
    }
    object Chat{
        const val MESSAGE_TYPE_LEFT = 0
        const val MESSAGE_TYPE_RIGHT = 1
    }
    object TAG{
        const val LOGIN = "LoginFragment"
        const val REGISTER = "RegisterFragment"
        const val RESET = "ResetFragment"
    }
}