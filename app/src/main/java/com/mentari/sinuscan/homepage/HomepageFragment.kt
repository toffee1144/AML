package com.mentari.sinuscan.homepage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.mentari.sinuscan.databinding.FragmentHomepageBinding
import com.mentari.sinuscan.R
import com.mentari.sinuscan.homepage.checkup.CheckupFragment
import com.mentari.sinuscan.homepage.report.ActivityFragment
import com.mentari.sinuscan.model.LatestDataResponse
import com.mentari.sinuscan.network.ApiClient
import com.mentari.sinuscan.utility.SessionManager
import com.mentari.sinuscan.profile.ProfilIdentityActivity

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
        userId = SessionManager.getUserId(requireContext()) // Pakai userId dari session manager
        Log.d("HomepageFragment", "UserId: $userId")  // Cek userId di log
        registerUser(userId)

        updateUsername()

        val photoUrl = SessionManager.getProfilePhotoUrl(requireContext())
        Log.d("HomepageFragment", "Photo URL: $photoUrl")

        Glide.with(requireContext())
            .load(photoUrl)
            .placeholder(R.drawable.ic_guest)
            .into(binding.imgProfileIcon)

        if (isFirstTime()) {
            binding.tvPercentage.text = "No Data Found!"
            binding.tvLevel.text = ""
            binding.tvNote.text = ""
            markFirstTimeDone()
        } else {
            fetchLatestDataFromApi()
        }

        binding.imgProfileIcon.setOnClickListener {
            val intent = Intent(requireContext(), ProfilIdentityActivity::class.java)
            startActivity(intent)
        }

        binding.layoutCheckup.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left,
                    R.anim.slide_in_right,
                    R.anim.slide_out_left
                )
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

    private fun updateUsername() {
        val username = SessionManager.getUsername(requireContext()) ?: "Guest"
        binding.tvUsername.text = username
    }

    override fun onResume() {
        super.onResume()
        updateUsername()

        val photoUrl = SessionManager.getProfilePhotoUrl(requireContext())
        Glide.with(requireContext())
            .load(photoUrl)
            .placeholder(R.drawable.ic_guest)
            .into(binding.imgProfileIcon)
    }


    private fun registerUser(userId: String) {
        val body = mapOf("userId" to userId)
        ApiClient.apiService.guestUser(body).enqueue(object : retrofit2.Callback<Void> {
            override fun onResponse(call: retrofit2.Call<Void>, response: retrofit2.Response<Void>) {
                // Optional: log
                Log.d("HomepageFragment", "guestUser registered: ${response.isSuccessful}")
            }

            override fun onFailure(call: retrofit2.Call<Void>, t: Throwable) {
                Log.e("HomepageFragment", "guestUser failed: ${t.message}")
            }
        })
    }

    private fun isFirstTime(): Boolean {
        val prefs = requireContext().getSharedPreferences("user_prefs", android.content.Context.MODE_PRIVATE)
        return prefs.getBoolean("is_first_time", true)
    }

    private fun markFirstTimeDone() {
        val prefs = requireContext().getSharedPreferences("user_prefs", android.content.Context.MODE_PRIVATE)
        prefs.edit().putBoolean("is_first_time", false).apply()
    }

    private fun fetchLatestDataFromApi() {
        Log.d("HomepageFragment", "Fetching latest data for userId: $userId")
        ApiClient.apiService.getLatestData(userId).enqueue(object : retrofit2.Callback<LatestDataResponse> {
            override fun onResponse(call: retrofit2.Call<LatestDataResponse>, response: retrofit2.Response<LatestDataResponse>) {
                if (!isAdded || _binding == null) return

                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        Log.d("HomepageFragment", "Data received: $data")

                        binding.tvPercentage.text = "${data.percentage}%"
                        binding.tvLevel.text = when (data.percentage) {
                            in 0..30 -> "Low Risk"
                            in 31..70 -> "Medium Risk"
                            else -> "High Risk"
                        }
                        binding.tvNote.text = data.recommendedAction
                    } else {
                        Log.e("HomepageFragment", "Response body null")
                        showNoDataFound()
                    }
                } else {
                    Log.e("HomepageFragment", "Response failed: ${response.code()} - ${response.message()}")
                    showNoDataFound()
                }
            }

            override fun onFailure(call: retrofit2.Call<LatestDataResponse>, t: Throwable) {
                if (!isAdded || _binding == null) return

                Log.e("HomepageFragment", "API call failed: ${t.message}")
                showNoDataFound()
            }
        })
    }

    private fun showNoDataFound() {
        binding.tvPercentage.text = ""
        binding.tvLevel.text = "No Data Found!"
        binding.tvNote.text = ""
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
