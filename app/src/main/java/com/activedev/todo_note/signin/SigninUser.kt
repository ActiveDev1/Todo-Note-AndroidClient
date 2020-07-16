package com.activedev.todo_note.signin

import android.util.Patterns


class SigninUser(var username: String, var email: String, var password: String) {

    fun isUsernameValid(): Boolean {
        return username.length > 5
    }

    fun isEmailValid(): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isPassValid(): Boolean {
        return password.length > 6
    }
}