package com.example.aml.homepage.report

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aml.R
import com.example.aml.model.ReportItem
import com.example.aml.network.ApiClient
import com.example.aml.utility.DeviceIdManager
import com.example.aml.utility.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ActivityAdapter
    private val reportList = mutableListOf<ReportItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_activity, container, false)
        recyclerView = view.findViewById(R.id.recyclerReports)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val backButton: ImageView = view.findViewById(R.id.imgBackButton)
        backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // ✅ Setup adapter with click listener here
        adapter = ActivityAdapter(reportList) { reportItem ->
            val uid = reportItem.reportUid
            if (uid.isNotEmpty()) {
                val fragment = ReportFragment.newInstance(uid)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.homepageContainer, fragment)
                    .addToBackStack(null)
                    .commit()
            } else {
                Toast.makeText(context, "Invalid report data", Toast.LENGTH_SHORT).show()
            }
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        fetchReportsFromApi()
    }

    private fun fetchReportsFromApi() {
        val userId = SessionManager.getUserId(requireContext())  // Ganti dari DeviceIdManager ke SessionManager

        ApiClient.apiService.getReports(userId).enqueue(object : Callback<List<ReportItem>> {
            override fun onResponse(
                call: Call<List<ReportItem>>,
                response: Response<List<ReportItem>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    reportList.clear()
                    reportList.addAll(response.body()!!)
                    adapter.notifyDataSetChanged()
                    Log.d("ActivityFragment", "Fetched reports: ${response.body()}")
                } else {
                    Toast.makeText(requireContext(), "No reports found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<ReportItem>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
