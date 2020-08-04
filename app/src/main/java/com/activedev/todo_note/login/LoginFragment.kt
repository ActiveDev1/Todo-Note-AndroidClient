package com.activedev.todo_note.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.activedev.todo_note.R
import com.activedev.todo_note.SessionManager
import com.activedev.todo_note.databinding.FragmentLoginBinding
import com.activedev.todo_note.network.Api
import com.activedev.todo_note.network.LoginUserRequest
import com.shashank.sony.fancytoastlib.FancyToast
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginFragment : Fragment() {

    private lateinit var session: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentLoginBinding.inflate(inflater, container, false)

        session = SessionManager(context)
        binding.btnLogin.setOnClickListener {
            val usernameOrEmail: String = binding.edtUsername.text.toString().trim()
            val password: String = binding.edtPass.text.toString().trim()
            val data = LoginValidat(usernameOrEmail, password)
            when {
                usernameOrEmail.isEmpty() -> {
                    binding.edtUsername.error = "Enter a Username or E-Mail"
                    binding.edtUsername.requestFocus()
                }
                !data.isUsernameValid() -> {
                    binding.edtUsername.error =
                        "Enter at least 5 Character for Username or valid E-Mail"
                    binding.edtUsername.requestFocus()
                }
                password.isEmpty() -> {
                    binding.edtPass.error = "Enter a Password"
                    binding.edtPass.requestFocus()
                }
                !data.isPassValid() -> {
                    binding.edtPass.error = "Enter at least 6 Digit password"
                    binding.edtPass.requestFocus()
                }
                else -> isDataValid(usernameOrEmail, password)
            }
        }

        binding.NoAcc.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signInFragment)
        }

        return binding.root
    }

    private fun isDataValid(usernameOrEmail: String, password: String) {

        try {
            val call: Call<ResponseBody> = Api.retrofitService.loginUser(
                LoginUserRequest(usernameOrEmail, password)
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
                                val name: String = data.getString("name")
                                FancyToast.makeText(
                                    context,
                                    "Welcome dear $name",
                                    FancyToast.LENGTH_SHORT,
                                    FancyToast.SUCCESS, false
                                ).show()
                                val sharedPref = activity?.getSharedPreferences(
                                    R.string.preference_file_key.toString()
                                    , Context.MODE_PRIVATE
                                ) ?: return
                                with(sharedPref.edit()) {
                                    putString(R.string.username.toString(), username)
                                    putString(R.string.name.toString(), name)
                                    putString(R.string.token.toString(), token)
                                    apply()
                                }
                                session.setLogin(true)

                                findNavController().navigate(R.id.action_loginFragment_to_nav_graph1)

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
                        "Error on connection !!",
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