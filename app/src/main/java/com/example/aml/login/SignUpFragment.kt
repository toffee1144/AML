package com.example.aml.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.aml.homepage.HomepageActivity
import com.example.aml.R
import com.example.aml.model.SignUpRequest
import com.example.aml.model.SignUpResponse
import com.example.aml.network.ApiClient
import com.example.aml.utility.DeviceIdManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpFragment : Fragment() {

    private lateinit var fullName: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var confirmPassword: EditText

    private lateinit var checkBoxMinLength: CheckBox
    private lateinit var checkBoxUppercase: CheckBox
    private lateinit var checkBoxNumber: CheckBox

    private lateinit var registerButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fullName = view.findViewById(R.id.editTextFullName)
        email = view.findViewById(R.id.editTextEmail)
        password = view.findViewById(R.id.editTextPassword)
        confirmPassword = view.findViewById(R.id.editTextConfirmPassword)

        checkBoxMinLength = view.findViewById(R.id.checkBoxMinLength)
        checkBoxUppercase = view.findViewById(R.id.checkBoxUppercase)
        checkBoxNumber = view.findViewById(R.id.checkBoxNumber)

        registerButton = view.findViewById(R.id.Btn_Register)

        password.addTextChangedListener(passwordWatcher)
        registerButton.setOnClickListener { onRegisterClick() }
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

    private fun onRegisterClick() {
        val name = fullName.text.toString().trim()
        val emailInput = email.text.toString().trim()
        val pwd = password.text.toString()
        val confirmPwd = confirmPassword.text.toString()

        if (name.isEmpty() || emailInput.isEmpty() || pwd.isEmpty() || confirmPwd.isEmpty()) {
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

        val userId = DeviceIdManager.getDeviceId(requireContext())
        val signUpRequest = SignUpRequest(
            userId = userId,
            username = name,
            email = emailInput,
            password = pwd
        )

        ApiClient.apiService.registerUser(signUpRequest).enqueue(object : Callback<SignUpResponse> {
            override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null && responseBody.success) {
                    Toast.makeText(requireContext(), "Registered successfully", Toast.LENGTH_SHORT).show()

                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, SignUpSuccessFragment())
                        .addToBackStack(null)
                        .commit()
                } else {
                    val message = responseBody?.message ?: "Registration failed"
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
