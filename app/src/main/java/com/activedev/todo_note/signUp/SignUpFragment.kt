package com.activedev.todo_note.signUp

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.activedev.todo_note.R
import com.activedev.todo_note.SessionManager
import com.activedev.todo_note.databinding.FragmentSignInBinding
import com.activedev.todo_note.network.Api
import com.activedev.todo_note.network.CreateUserRequest
import com.shashank.sony.fancytoastlib.FancyToast
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignUpFragment : Fragment() {

    private lateinit var session: SessionManager
    lateinit var sharedPref: SharedPreferences


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        session = SessionManager(context)
        if (session.isLoggedIn) {
            findNavController().navigate(R.id.action_signInFragment_to_mainFragment)
        }

        sharedPref = activity?.getSharedPreferences(
            R.string.preference_file_key.toString(),
            Context.MODE_PRIVATE
        ) ?: return
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentSignInBinding.inflate(inflater, container, false)

//        val sharedPref = activity?.getSharedPreferences(
//            getString(R.string.preference_file_key), Context.MODE_PRIVATE)


        binding.btnSigin.setOnClickListener {
            val name: String = binding.edtName.text.toString().trim()
            val username: String = binding.edtUsername.text.toString().trim()
            val email: String = binding.edtEmail.text.toString().trim()
            val password: String = binding.edtPass.text.toString().trim()
            val data = SignUpValidat(name, username, email, password)

            when {
                name.isEmpty() -> {
                    binding.edtName.error = "Enter a Name"
                    binding.edtName.requestFocus()
                }
                !data.isNameValid() -> {
                    binding.edtName.error = "Enter at least 3 Character Name"
                    binding.edtName.requestFocus()
                }
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

        binding.haveAcc.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_loginFragment)
        }

        return binding.root
    }

    private fun isDataValid(name: String, userName: String, email: String, password: String) {

        try {
            val call: Call<ResponseBody> = Api.retrofitService.createUser(
                CreateUserRequest(name, userName, email, password)
            )

            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        try {
                            val result = JSONObject(response.body()!!.string())
                            val success = result.getBoolean("success")
                            if (success) {
                                val data = result.getJSONObject("data")
                                val token: String = data.getString("token")
                                val username: String = data.getString("username")

                                FancyToast.makeText(
                                    context,
                                    "Successfully created account",
                                    FancyToast.LENGTH_SHORT,
                                    FancyToast.SUCCESS, false
                                ).show()

                                with(sharedPref.edit()) {
                                    putString(R.string.username.toString(), username)
                                    putString(R.string.token.toString(), token)
                                    apply()
                                }
                                session.setLogin(true)
                                findNavController().navigate(R.id.action_signInFragment_to_mainFragment)


                            } else {
                                val error = result.getString("error")
                                FancyToast.makeText(
                                    context,
                                    error,
                                    FancyToast.LENGTH_SHORT,
                                    FancyToast.WARNING, false
                                ).show()
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    FancyToast.makeText(
                        context,
                        "Error on conection !!",
                        FancyToast.LENGTH_SHORT,
                        FancyToast.ERROR, false
                    ).show()
                }
            })

        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}