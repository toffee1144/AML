package com.example.aml.homepage.report

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.aml.R
import com.example.aml.model.FormData
import com.example.aml.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReportFragment : Fragment() {

    private var reportUid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            reportUid = it.getString(ARG_REPORT_UID)
            Log.d("ReportFragment", "Received reportUid = $reportUid")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_report, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tabResult = view.findViewById<FrameLayout>(R.id.tabResult)
        val tabChecklist = view.findViewById<FrameLayout>(R.id.tabChecklist)
        tabResult.isEnabled = false
        tabChecklist.isEnabled = false

        val backButton: ImageView = view.findViewById(R.id.imgBackButton)
        backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        tabResult.setOnClickListener {
            updateTabs(selectedTab = "result")
            loadResultFragment()
        }

        tabChecklist.setOnClickListener {
            updateTabs(selectedTab = "checklist")
            loadChecklistFragment()
        }

        reportUid?.let { uid ->
            fetchReportData(uid)
        } ?: run {
            Log.e("ReportFragment", "reportUid is null! Cannot fetch data.")
        }
    }

    private fun updateTabs(selectedTab: String) {
        val tabResult = requireView().findViewById<FrameLayout>(R.id.tabResult)
        val tabChecklist = requireView().findViewById<FrameLayout>(R.id.tabChecklist)
        val tvResult = requireView().findViewById<TextView>(R.id.tvTabResult)
        val tvChecklist = requireView().findViewById<TextView>(R.id.tvTabChecklist)

        if (selectedTab == "result") {
            tabResult.setBackgroundResource(R.drawable.shape_white_cu)
            tvResult.setTextColor(ContextCompat.getColor(requireContext(), R.color.teal))

            tabChecklist.setBackgroundResource(R.drawable.shape_orange_cu)
            tvChecklist.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        } else {
            tabChecklist.setBackgroundResource(R.drawable.shape_white_cu)
            tvChecklist.setTextColor(ContextCompat.getColor(requireContext(), R.color.teal))

            tabResult.setBackgroundResource(R.drawable.shape_orange_cu)
            tvResult.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        }
    }

    private fun fetchReportData(uid: String) {
        Log.d("ReportFragment", "Fetching report data for UID: $uid")
        val apiService = ApiClient.apiService
        apiService.getReportByUid(uid).enqueue(object : Callback<FormData> {
            override fun onResponse(call: Call<FormData>, response: Response<FormData>) {
                if (response.isSuccessful && response.body() != null) {
                    Log.d("ReportFragment", "Report data fetched successfully")
                    requireView().findViewById<View>(R.id.tabResult).isEnabled = true
                    requireView().findViewById<View>(R.id.tabChecklist).isEnabled = true

                    updateTabs("result")
                    loadResultFragment()
                } else {
                    Log.e("ReportFragment", "Failed to fetch report data or data is null")
                }
            }

            override fun onFailure(call: Call<FormData>, t: Throwable) {
                Log.e("ReportFragment", "API call failed: ${t.localizedMessage}")
            }
        })
    }

    private fun loadResultFragment() {
        Log.d("ReportFragment", "Loading ResultFragment with reportUid: $reportUid")
        val resultFragment = RcFragment.newInstance(reportUid ?: "")
        childFragmentManager.beginTransaction()
            .replace(R.id.reportContainer, resultFragment)
            .commit()
    }

    private fun loadChecklistFragment() {
        Log.d("ReportFragment", "Loading ChecklistFragment with reportUid: $reportUid")
        val checklistFragment = ChecklistFragment.newInstance(reportUid ?: "")
        childFragmentManager.beginTransaction()
            .replace(R.id.reportContainer, checklistFragment)
            .commit()
    }

    companion object {
        private const val ARG_REPORT_UID = "report_uid"

        fun newInstance(uid: String) = ReportFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_REPORT_UID, uid)
            }
        }
    }
}
