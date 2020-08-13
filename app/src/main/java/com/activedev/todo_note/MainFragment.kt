package com.activedev.todo_note

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController


class MainFragment : Fragment() {

    private lateinit var session: SessionManager

//    lateinit var binding: FragmentMainBinding
    private var navController: NavController? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        session = SessionManager(context)
        if (!session.isLoggedIn) {
            logoutUser()
        }

        val nestedNavHostFragment =
            childFragmentManager.findFragmentById(R.id.nav_host_fragment1) as? NavHostFragment
        navController = nestedNavHostFragment?.navController
        navController?.navigate(R.id.todoFragment)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.fragment_main, container, false)
    }


    private fun logoutUser() {
        session.setLogin(false)
        findNavController().navigate(R.id.action_mainFragment_to_signInFragment)

    }

}

