package com.activedev.todo_note

import android.content.Context
import android.content.SharedPreferences


class SessionManager(_context: Context?) {
    private var pref: SharedPreferences
    private var editor: SharedPreferences.Editor

    fun setLogin(isLoggedIn: Boolean) {
        editor.putBoolean(KEY_IS_LOGGED, isLoggedIn)

        editor.commit()
    }

    val isLoggedIn: Boolean
        get() = pref.getBoolean(KEY_IS_LOGGED, false)

    companion object {
        private const val PREF_NAME = "MyPref"
        private const val KEY_IS_LOGGED = "isLoggedIn"
    }

    init {
        pref = _context!!.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        editor = pref.edit()
    }
}