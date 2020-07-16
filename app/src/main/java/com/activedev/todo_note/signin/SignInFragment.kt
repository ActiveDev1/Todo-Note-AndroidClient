package com.activedev.todo_note.signin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.activedev.todo_note.databinding.FragmentSignInBinding


class SignInFragment : Fragment() {

    private val viewModel: SigninViewModel by lazy {
        ViewModelProvider(this).get(SigninViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentSignInBinding.inflate(inflater)

        binding.lifecycleOwner = this
        binding.viewModel

        viewModel.userMutableLiveData.observe(viewLifecycleOwner, Observer<SigninUser?> { t ->

            if (t != null) {

                if (t.username.isEmpty()) {
                    binding.edtUsername.error = "Enter a Username"
                    binding.edtUsername.requestFocus()
                } else if (!t.isUsernameValid()) {
                    binding.edtUsername.error = "Enter at least 6 Character username"
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
            }

        })

        /* if (t.username.isNotEmpty()) {

             if (t.email.isNotEmpty()) {
                 if (t.isUsernameValid()) {
                     //TODO Create a requist()
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

        return binding.root
    }

}