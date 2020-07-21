package com.activedev.todo_note.login

import android.util.Patterns


class LoginValidat(
    private val usernameOrEmail: String,
    private val password: String
) {

    fun isUsernameValid(): Boolean {
        return usernameOrEmail.length >= 5 || Patterns.EMAIL_ADDRESS.matcher(usernameOrEmail)
            .matches()
    }

    fun isPassValid(): Boolean {
        return password.length >= 6
    }
}