package com.activedev.todo_note.signUp

import android.util.Patterns

class SignUpValidat(
    private val name: String,
    private val userName: String,
    private val email: String,
    private val password: String
) {

    fun isNameValid(): Boolean {
        return name.length >= 3
    }

    fun isUsernameValid(): Boolean {
        return userName.length >= 5
    }

    fun isEmailValid(): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isPassValid(): Boolean {
        return password.length >= 6
    }
}