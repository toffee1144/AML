package com.mentari.sinuscan.login

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.mentari.sinuscan.R
import com.mentari.sinuscan.databinding.FragmentLoginBinding
import com.mentari.sinuscan.homepage.HomepageActivity
import com.mentari.sinuscan.model.LoginRequest
import com.mentari.sinuscan.model.LoginResponse
import com.mentari.sinuscan.network.ApiClient
import com.mentari.sinuscan.utility.DeviceIdManager
import com.mentari.sinuscan.utility.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val TAG = "LoginFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkIfLoggedIn()

        binding.buttonSubmit.setOnClickListener {
            val nickname = binding.etNickname.text.toString()
            val token = binding.etToken.text.toString()
            val password = binding.etPassword.text.toString()

            if (nickname.isNotBlank() && token.isNotBlank() && password.isNotBlank()) {
                loginUser(nickname, token, password)
            } else {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvGuest.setOnClickListener {
            val guestId = DeviceIdManager.getDeviceId(requireContext())
            SessionManager.save(requireContext(), guestId, guestId, "Guest", "guest@example.com")

            ApiClient.apiService.guestUser(mapOf("userId" to guestId))
                .enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        goToHomepage()
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(requireContext(), "Guest registration failed", Toast.LENGTH_SHORT).show()
                        goToHomepage()
                    }
                })
        }

        setupCreateAccountLink()
    }

    private fun setupCreateAccountLink() {
        val fullText = "Don’t have an account? Create Account"
        val spannable = SpannableString(fullText)

        val start = fullText.indexOf("Create Account")
        val end = start + "Create Account".length

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.loginContainer, SignUpFragment())
                    .addToBackStack(null)
                    .commit()
            }
        }

        val blue = 0xFF0000FF.toInt()

        spannable.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(ForegroundColorSpan(blue), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.tvCreateAccount.text = spannable
        binding.tvCreateAccount.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun loginUser(nickname: String, token: String, password: String) {
        val request = LoginRequest(nickname, token, password)
        val call = ApiClient.apiService.login(request)

        Log.d(TAG, "Sending login request: nickname=$nickname, token=$token")

        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val loginResponse = response.body()!!

                    Log.d(TAG, "Login success: userId=${loginResponse.userId}, username=${loginResponse.nickname}, token=${loginResponse.token}")

                    saveSession(
                        token = loginResponse.token,
                        userId = loginResponse.userId,
                        username = loginResponse.nickname,
                        profilePhotoUrl = loginResponse.profilePhotoUrl
                    )

                    goToHomepage()
                } else {
                    Log.e(TAG, "Login failed: code=${response.code()}, message=${response.message()}")
                    Toast.makeText(requireContext(), "Login failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e(TAG, "Login error: ${t.message}", t)
                Toast.makeText(requireContext(), "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveSession(token: String, userId: String, username: String, profilePhotoUrl: String?) {
        Log.d(TAG, "Saving session: token=$token, userId=$userId, username=$username")
        profilePhotoUrl?.let { Log.d(TAG, "Profile photo URL: $it") }

        SessionManager.save(requireContext(), token, userId, username, "")
        profilePhotoUrl?.let {
            SessionManager.setProfilePhotoUrl(requireContext(), it)
        }
    }

    private fun saveSession(token: String, userId: String, username: String, email: String, profilePhotoUrl: String?) {
        Log.d(TAG, "Saving session: token=$token, userId=$userId, username=$username, email=$email")
        profilePhotoUrl?.let {
            Log.d(TAG, "Saving profile photo URL: $it")
        } ?: Log.d(TAG, "No profile photo URL received")

        SessionManager.save(requireContext(), token, userId, username, email)
        profilePhotoUrl?.let {
            SessionManager.setProfilePhotoUrl(requireContext(), it)
        }
    }

    private fun checkIfLoggedIn() {
        val token = SessionManager.getToken(requireContext())
        Log.d(TAG, "Checking login status: token=$token")
        if (!token.isNullOrEmpty()) {
            goToHomepage()
        }
    }

    private fun goToHomepage() {
        Log.d(TAG, "Navigating to homepage")
        val intent = Intent(requireContext(), HomepageActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
}
