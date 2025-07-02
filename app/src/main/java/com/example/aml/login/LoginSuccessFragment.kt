package com.example.aml.login

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.example.aml.R
import androidx.core.graphics.toColorInt
import androidx.navigation.fragment.findNavController

class LoginSuccessFragment : Fragment() {

    companion object {
        private const val ARG_IS_SUCCESS = "isLoginSuccess"
        private const val ARG_USERNAME = "username"

        fun newInstance(isLoginSuccess: Boolean, username: String = ""): LoginSuccessFragment {
            val fragment = LoginSuccessFragment()
            val args = Bundle()
            args.putBoolean(ARG_IS_SUCCESS, isLoginSuccess)
            args.putString(ARG_USERNAME, username)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login_success, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val isLoginSuccess = arguments?.getBoolean(ARG_IS_SUCCESS) ?: false
        val username = arguments?.getString(ARG_USERNAME) ?: ""

        updateLoginResultUI(view, isLoginSuccess, username)
    }

    private fun updateLoginResultUI(view: View, isLoginSuccess: Boolean, username: String = "") {
        val flChange = view.findViewById<FrameLayout>(R.id.flChange)
        val ivChange = view.findViewById<ImageView>(R.id.ivChange)
        val flChange2 = view.findViewById<FrameLayout>(R.id.flChange2)
        val tvWelcome2 = view.findViewById<TextView>(R.id.tvWelcome2)
        val imageView6 = view.findViewById<ImageView>(R.id.imageView6)
        val buttonSubmit = view.findViewById<AppCompatButton>(R.id.buttonSubmit)

        if (isLoginSuccess) {
            // Success UI
            flChange.setBackgroundColor(Color.parseColor("#6E89C1C6")) // Blue 43%
            flChange2.setBackgroundResource(R.drawable.shape_bblue_card_43)
            ivChange.setImageResource(R.drawable.ic_success)
            imageView6.setImageResource(R.drawable.ic_succkey)
            tvWelcome2.text = "Login Success"
            tvWelcome2.setTextColor("#008080".toColorInt()) // Teal
            buttonSubmit.text = "Hello /$username/"
            buttonSubmit.setBackgroundResource(R.drawable.shape_teal_card)

            buttonSubmit.setOnClickListener {
                // Navigate to homepage or main screen
                Toast.makeText(requireContext(), "Welcome $username", Toast.LENGTH_SHORT).show()
                // Example:
                findNavController().navigate(R.id.btnNavigation)
            }

        } else {
            // Failure UI
            flChange.setBackgroundColor(Color.parseColor("#6EFF4545")) // Red 43%
            flChange2.setBackgroundResource(R.drawable.shape_red_card_43)
            ivChange.setImageResource(R.drawable.ic_failed)
            imageView6.setImageResource(R.drawable.ic_failkey)
            tvWelcome2.text = "Login Failed"
            tvWelcome2.setTextColor(Color.RED)
            buttonSubmit.text = "Back to Login"
            buttonSubmit.setBackgroundResource(R.drawable.shape_red_card)

            buttonSubmit.setOnClickListener {
                parentFragmentManager.popBackStack()
            }
        }
    }
}
