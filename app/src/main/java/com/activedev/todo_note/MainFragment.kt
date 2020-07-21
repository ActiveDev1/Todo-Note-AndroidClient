package com.activedev.todo_note

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.activedev.todo_note.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        session = SessionManager(context)
        if (!session.isLoggedIn) {
            logoutUser()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
//        val defaultValue = resources.getInteger(R.integer.saved_high_score_default_key)
//        val highScore = sharedPref.getInt(getString(R.string.saved_high_score_key), defaultValue)

        val binding = FragmentMainBinding.inflate(inflater, container, false)

        return binding.root
    }

    private fun logoutUser() {
        session.setLogin(false)
        findNavController().navigate(R.id.action_mainFragment_to_signInFragment)

    }

}