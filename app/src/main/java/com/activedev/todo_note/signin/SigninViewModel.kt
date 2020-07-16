package com.activedev.todo_note.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SigninViewModel : ViewModel() {

    private var _username = MutableLiveData<String>()
    val username: LiveData<String>
        get() = _username

    private var _email = MutableLiveData<String>()
    val email: LiveData<String>
        get() = _email

    private var _password = MutableLiveData<String>()
    val password: LiveData<String>
        get() = _password

    private val _userMutableLiveData = MutableLiveData<SigninUser>()
    val userMutableLiveData: LiveData<SigninUser>
        get() = _userMutableLiveData

    private var buttonClick = MutableLiveData<Boolean>()
    /*   var sigin: SiginFields? = null
       var usernameOnFocusChangeListener: View.OnFocusChangeListener? = null
       var passwordOnFocusChangeListener: View.OnFocusChangeListener? = null
       val buttonClick = MutableLiveData<SiginFields?>()*/

    /* init {
          sigin = SiginFields()
          usernameOnFocusChangeListener = View.OnFocusChangeListener { view, focused ->
              val et = view as EditText
              if (et.text.isNotEmpty() && !focused) {
                  sigin!!.isEmailValid(true,et.text.toString())
              }
          }
          passwordOnFocusChangeListener = View.OnFocusChangeListener { view, focused ->
              val et = view as EditText
              if (et.text.isNotEmpty() && !focused) {
                  sigin!!.isPasswordValid(true,et.text.toString())
              }
          }

     }*/

    fun isDataValid() {


    }


    fun onButtonClick() {
        val sigin = SigninUser(_username.value!!, _email.value!!, _password.value!!)
        _userMutableLiveData.value = sigin
    }
}