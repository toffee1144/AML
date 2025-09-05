package com.mentari.sinuscan.homepage.checkup

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.mentari.sinuscan.R
import com.mentari.sinuscan.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.mentari.sinuscan.homepage.HomepageFragment

class ResultFragment : Fragment() {

    private val formViewModel: FormViewModel by activityViewModels()
    private var isDataSent = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_result, container, false)

        // Auto-submit when fragment is opened
        if (!isDataSent) {
            sendFormData()
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

        isDataSent = true

        ApiClient.apiService.submitForm(formData).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Form berhasil dikirim!", Toast.LENGTH_SHORT).show()

                    // ✅ Navigate back to homepage
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.homepageContainer, HomepageFragment())
                        .addToBackStack(null)
                        .commit()
                } else {
                    Toast.makeText(requireContext(), "Gagal submit: ${response.code()}", Toast.LENGTH_SHORT).show()
                    resetState()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(requireContext(), "Error jaringan: ${t.message}", Toast.LENGTH_LONG).show()
                resetState()
            }
        })
    }

    private fun resetState() {
        isDataSent = false
    }
}
