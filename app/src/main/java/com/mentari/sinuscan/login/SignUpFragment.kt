package com.mentari.sinuscan.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.mentari.sinuscan.R
import com.mentari.sinuscan.model.SignUpRequest
import com.mentari.sinuscan.model.SignUpResponse
import com.mentari.sinuscan.network.ApiClient
import com.mentari.sinuscan.utility.DeviceIdManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpFragment : Fragment() {

    private lateinit var nickname: EditText
    private lateinit var token: EditText
    private lateinit var password: EditText
    private lateinit var confirmPassword: EditText

    private lateinit var checkBoxMinLength: CheckBox
    private lateinit var checkBoxUppercase: CheckBox
    private lateinit var checkBoxNumber: CheckBox

    private lateinit var checkBoxPrivacyPolicy: CheckBox

    private lateinit var registerButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nickname = view.findViewById(R.id.editTextNickname)
        password = view.findViewById(R.id.editTextPassword)
        confirmPassword = view.findViewById(R.id.editTextConfirmPassword)
        token = view.findViewById(R.id.editTextToken)   // <-- NEW
        checkBoxMinLength = view.findViewById(R.id.checkBoxMinLength)
        checkBoxUppercase = view.findViewById(R.id.checkBoxUppercase)
        checkBoxNumber = view.findViewById(R.id.checkBoxNumber)
        registerButton = view.findViewById(R.id.Btn_Register)

        password.addTextChangedListener(passwordWatcher)
        registerButton.setOnClickListener { onRegisterClick() }

        checkBoxPrivacyPolicy = view.findViewById(R.id.checkBoxPrivacyPolicy)

// Open Privacy Policy link when clicked
        checkBoxPrivacyPolicy.setOnClickListener {
            val url = "https://docs.google.com/document/d/1V-x4VgMYMJ21GWIIydAFxBaxQqSIsUG9yenvvJSiSLM/edit?tab=t.0"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = android.net.Uri.parse(url)
            startActivity(intent)
        }

    }

    private fun onRegisterClick() {
        val name = nickname.text.toString().trim()
        val pwd = password.text.toString()
        val confirmPwd = confirmPassword.text.toString()
        val tokenInput = token.text.toString().trim()

        if (name.isEmpty() || pwd.isEmpty() || confirmPwd.isEmpty() || tokenInput.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (pwd != confirmPwd) {
            Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        if (!checkBoxMinLength.isChecked || !checkBoxUppercase.isChecked || !checkBoxNumber.isChecked) {
            Toast.makeText(requireContext(), "Password doesn't meet requirements", Toast.LENGTH_SHORT).show()
            return
        }

        if (tokenInput.length != 5 || !tokenInput.all { it.isDigit() }) {
            Toast.makeText(requireContext(), "Token must be exactly 5 digits", Toast.LENGTH_SHORT).show()
            return
        }

        if (!checkBoxPrivacyPolicy.isChecked) {
            Toast.makeText(requireContext(), "You must agree to the Privacy Policy", Toast.LENGTH_SHORT).show()
            return
        }


        val userId = DeviceIdManager.getDeviceId(requireContext())
        val signUpRequest = SignUpRequest(
            userId = userId,
            nickname = name,
            token = tokenInput.toInt(),
            password = pwd
        )

        ApiClient.apiService.registerUser(signUpRequest).enqueue(object : Callback<SignUpResponse> {
            override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                Log.d("SignUpFragment", "HTTP Status: ${response.code()}")
                Log.d("SignUpFragment", "Raw request: ${call.request()}")

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.d("SignUpFragment", "Response body: $responseBody")

                    if (responseBody != null) {
                        Toast.makeText(requireContext(), "Registered successfully", Toast.LENGTH_SHORT).show()
                        parentFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.loginContainer, SignUpSuccessFragment())
                            .commit()
                    }
                } else {
                    // Log error body
                    val errorBody = response.errorBody()?.string()
                    Log.e("SignUpFragment", "Error body: $errorBody")

                    Toast.makeText(
                        requireContext(),
                        "Registration failed: ${errorBody ?: "Unknown error"}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                Log.e("SignUpFragment", "API call failed", t)
            }
        })
    }

    private val passwordWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            val pwd = s.toString()
            checkBoxMinLength.isChecked = pwd.length >= 8
            checkBoxUppercase.isChecked = pwd.any { it.isUpperCase() }
            checkBoxNumber.isChecked = pwd.any { it.isDigit() }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

}
