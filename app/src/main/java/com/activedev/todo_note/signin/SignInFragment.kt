package com.activedev.todo_note.signin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.activedev.todo_note.databinding.FragmentSignInBinding
import com.activedev.todo_note.network.Api
import com.activedev.todo_note.network.UserRequest
import kotlinx.android.synthetic.main.fragment_sign_in.*
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignInFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentSignInBinding.inflate(inflater, container, false)
        /*  var name :String
        name = view.findViewById<>()
        val user = view?.findViewById(R.id.edtUsername)
        val email = view?.findViewById(R.id.edtEmail)
        val pass = view?.findViewById(R.id.edtPass)
        val btn = view?.findViewById(R.id.btnSigin)*/
        /*

        binding.btnSigin.setOnClickListener {
            // Let the view model know something happened.

        }
*/
        /*


                if (t.username.isEmpty()) {
                    binding.edtUsername.error = "Enter a Name"
                    binding.edtUsername.requestFocus()
                } else if (!t.isNameValid()) {
                    binding.edtUsername.error = "Enter at least 3 Character Name"
                    binding.edtUsername.requestFocus()
                } else if (t.username.isEmpty()) {
                    binding.edtUsername.error = "Enter a Username"
                    binding.edtUsername.requestFocus()
                } else if (!t.isUsernameValid()) {
                    binding.edtUsername.error = "Enter at least 5 Character username"
                    binding.edtUsername.requestFocus()
                } else if (t.email.isEmpty()) {
                    binding.edtEmail.error = "Enter an E-Mail Address"
                    binding.edtEmail.requestFocus()
                } else if (!t.isEmailValid()) {
                    binding.edtEmail.error = "Enter a Valid E-Mail Address"
                    binding.edtEmail.requestFocus()
                } else if (t.isPassValid()) {
                    binding.edtEmail.error = "Enter at least 6 Digit password"
                    binding.edtEmail.requestFocus()
                } else
                    viewModel.isDataValid()
            }*/
        /* if (t.username.isNotEmpty()) {

             if (t.email.isNotEmpty()) {
                 if (t.isUsernameValid()) {

                 } else {
                     binding.edtUsername.error = "Enter an E-Mail Address"
                     binding.edtUsername.requestFocus()
                 }
             } else {
                 binding.edtUsername.error = "Enter an E-Mail Address"
                 binding.edtUsername.requestFocus()
             }
         } else {
             binding.edtUsername.error = "Enter a Username"
             binding.edtUsername.requestFocus()
         }*/

        binding.btnSigin.setOnClickListener {
            val name: String = edtName.text.toString().trim()
            val username: String = edtUsername.text.toString().trim()
            val email: String = edtEmail.text.toString().trim()
            val password: String = edtPass.text.toString().trim()
            val data = SigninValidat(name, username, email, password)

            when {
                username.isEmpty() -> {
                    binding.edtUsername.error = "Enter a Username"
                    binding.edtUsername.requestFocus()
                }
                !data.isUsernameValid() -> {
                    binding.edtUsername.error = "Enter at least 5 Character Username"
                    binding.edtUsername.requestFocus()
                }
                email.isEmpty() -> {
                    binding.edtEmail.error = "Enter an E-Mail Address"
                    binding.edtEmail.requestFocus()
                }
                !data.isEmailValid() -> {
                    binding.edtEmail.error = "Enter a Valid E-Mail Address"
                    binding.edtEmail.requestFocus()
                }
                password.isEmpty() -> {
                    binding.edtPass.error = "Enter a Password"
                    binding.edtPass.requestFocus()
                }
                !data.isPassValid() -> {
                    binding.edtPass.error = "Enter at least 6 Digit password"
                    binding.edtPass.requestFocus()
                }
                else -> isDataValid(name, username, email, password)
            }
        }



        return binding.root
    }

    private fun isDataValid(name: String, userName: String, email: String, password: String) {

        try {
            val response: Call<UserRequest> = Api.retrofitService.postJson(
                UserRequest(name, userName, email, password)
            )

            response.enqueue(object : Callback<UserRequest> {
                override fun onResponse(call: Call<UserRequest>, response: Response<UserRequest>) {
                    Log.i("ssssss", response.toString())
                }

                override fun onFailure(call: Call<UserRequest>, t: Throwable) {
                    Log.e("ssssss", response.toString())
                }
            })

        } catch (e: JSONException) {
            e.printStackTrace()
        }


    }

}