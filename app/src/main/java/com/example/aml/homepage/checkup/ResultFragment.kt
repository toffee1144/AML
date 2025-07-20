package com.example.aml.homepage.checkup

import android.os.Bundle
import android.util.Log
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
    private var isDataSent = false
    private lateinit var buttonNext: Button  // ⬅️ Make it accessible throughout the class

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_result, container, false)

        buttonNext = view.findViewById(R.id.buttonNext)

        buttonNext.setOnClickListener {
            if (!isDataSent) {
                sendFormData()
            }
        }

        return view
    }

    private fun sendFormData() {
        val formData = formViewModel.toFormData(requireContext())

        if (formData.reportName.isBlank()) {
            Toast.makeText(requireContext(), "Data tidak lengkap. Harap isi semua pertanyaan.", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("ResultFragment", "Sending form: $formData")

        // Disable the button to prevent multiple clicks
        isDataSent = true
        buttonNext.isEnabled = false

// Submit form in background
        ApiClient.apiService.submitForm(formData).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    requireActivity().runOnUiThread {
                        Toast.makeText(requireContext(), "Form berhasil dikirim!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    requireActivity().runOnUiThread {
                        Toast.makeText(requireContext(), "Gagal submit: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                requireActivity().runOnUiThread {
                    Toast.makeText(requireContext(), "Error jaringan: ${t.message}", Toast.LENGTH_LONG).show()
                }
            }
        })

// 🔄 Immediately continue to next screen (no waiting)
        parentFragmentManager.beginTransaction()
            .replace(R.id.homepageContainer, HomepageFragment())
            .addToBackStack(null)
            .commit()
    }
}
