package com.example.aml.homepage.checkup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.aml.R
import com.example.aml.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.Button
import com.example.aml.homepage.HomepageFragment

class ResultFragment : Fragment() {

    private val formViewModel: FormViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sendFormData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_result, container, false)

        // Set button listener here
        val buttonNext = view.findViewById<Button>(R.id.buttonNext)
        buttonNext.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.homepageContainer, HomepageFragment())
                .addToBackStack(null)
                .commit()
        }

        return view
    }

    private fun sendFormData() {
        val formData = formViewModel.toFormData(requireContext())

        ApiClient.apiService.submitForm(formData).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Form submitted successfully!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Failed to submit form: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(requireContext(), "Network error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}
