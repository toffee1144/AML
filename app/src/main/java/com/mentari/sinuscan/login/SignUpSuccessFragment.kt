package com.mentari.sinuscan.login

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.mentari.sinuscan.R

class SignUpSuccessFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_sign_up_success, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val layout = view.findViewById<View>(R.id.layout_success)
        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.clink)
        layout?.startAnimation(animation)

        // Handle button click immediately
        view.findViewById<View>(R.id.buttonSubmit).setOnClickListener {
            navigateToLogin()
        }

        // Or auto navigate after delay
        Handler(Looper.getMainLooper()).postDelayed({
            navigateToLogin()
        }, 25000)
    }

    private fun navigateToLogin() {
        requireActivity()
            .supportFragmentManager
            .beginTransaction()
            .replace(R.id.loginRoot, LoginFragment())
            .commit()
    }
}
