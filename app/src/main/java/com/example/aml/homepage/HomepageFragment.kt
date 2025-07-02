package com.example.aml.homepage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.aml.databinding.FragmentHomepageBinding
import com.example.aml.R
import com.example.aml.homepage.checkup.CheckupFragment
import com.example.aml.homepage.report.ActivityAdapter
import com.example.aml.homepage.report.ActivityFragment
import com.example.aml.model.LatestDataResponse
import com.example.aml.network.ApiClient
import com.example.aml.utility.DeviceIdManager
import androidx.core.content.edit

class HomepageFragment : Fragment() {
    private var _binding: FragmentHomepageBinding? = null
    private val binding get() = _binding!!
    private lateinit var userId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomepageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        userId = DeviceIdManager.getDeviceId(requireContext())
        registerUser(userId)

        if (isFirstTime()) {
            binding.tvPercentage.text = "No Data Found!"
            binding.tvLevel.text = ""
            binding.tvNote.text = ""
            markFirstTimeDone()
        } else {
            fetchLatestDataFromApi()
        }

        binding.layoutCheckup.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.homepageContainer, CheckupFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.layoutReport.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.homepageContainer, ActivityFragment())
                .addToBackStack(null)
                .commit()
        }

    }

    private fun registerUser(userId: String) {
        val body = mapOf("userId" to userId)
        ApiClient.apiService.guestUser(body).enqueue(object : retrofit2.Callback<Void> {
            override fun onResponse(call: retrofit2.Call<Void>, response: retrofit2.Response<Void>) {
                // Optional: Log or show something if needed
            }

            override fun onFailure(call: retrofit2.Call<Void>, t: Throwable) {
                // Optional: Handle error
            }
        })
    }

    private fun isFirstTime(): Boolean {
        val prefs = requireContext().getSharedPreferences("user_prefs", android.content.Context.MODE_PRIVATE)
        return prefs.getBoolean("is_first_time", true)
    }

    private fun markFirstTimeDone() {
        val prefs = requireContext().getSharedPreferences("user_prefs", android.content.Context.MODE_PRIVATE)
        prefs.edit() { putBoolean("is_first_time", false) }
    }

    private fun fetchLatestDataFromApi() {
        ApiClient.apiService.getLatestData(userId).enqueue(object : retrofit2.Callback<LatestDataResponse> {
            override fun onResponse(call: retrofit2.Call<LatestDataResponse>, response: retrofit2.Response<LatestDataResponse>) {
                if (!isAdded || _binding == null) return

                val data = response.body()
                if (response.isSuccessful && data != null) {
                    binding.tvPercentage.text = "${data.percentage}%"
                    binding.tvLevel.text = when (data.percentage) {
                        in 0..30 -> "Low Risk"
                        in 31..70 -> "Medium Risk"
                        else -> "High Risk"
                    }
                    binding.tvNote.text = "Please seek a doctor for\ndetailed information"
                } else {
                    binding.tvPercentage.text = ""
                    binding.tvLevel.text = "No Data Found!"
                    binding.tvNote.text = ""
                }
            }

            override fun onFailure(call: retrofit2.Call<LatestDataResponse>, t: Throwable) {
                if (!isAdded || _binding == null) return

                binding.tvPercentage.text = "No Data Found!"
                binding.tvLevel.text = ""
                binding.tvNote.text = ""
            }
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
