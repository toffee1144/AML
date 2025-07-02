package com.example.aml.homepage.report

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.aml.R
import com.example.aml.model.ReportSummary
import com.example.aml.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RcFragment : Fragment(R.layout.fragment_rc) {

    private var reportUid: String? = null

    private lateinit var tvSymptom: TextView
    private lateinit var tvRiskLevel: TextView
    private lateinit var tvReportContent: TextView
    private lateinit var tvReportContent3: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            reportUid = it.getString(ARG_REPORT_UID)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvSymptom = view.findViewById(R.id.tvSymptom)
        tvRiskLevel = view.findViewById(R.id.tvRiskLevel)
        tvReportContent = view.findViewById(R.id.tvReportContent)
        tvReportContent3 = view.findViewById(R.id.tvReportContent3)

        if (reportUid != null) {
            fetchSummary(reportUid!!)
        }
    }

    private fun fetchSummary(uid: String) {
        ApiClient.apiService.getReportSummary(uid).enqueue(object : Callback<ReportSummary> {
            override fun onResponse(call: Call<ReportSummary>, response: Response<ReportSummary>) {
                if (response.isSuccessful && response.body() != null) {
                    val summary = response.body()!!
                    tvSymptom.text = "${summary.percentage}%"
                    tvRiskLevel.text = "${summary.matchCount}/${summary.totalCount}"
                    tvReportContent.text = summary.explanation
                    tvReportContent3.text = summary.recommendation
                } else {
                    tvReportContent.text = "Failed to load report data."
                }
            }

            override fun onFailure(call: Call<ReportSummary>, t: Throwable) {
                tvReportContent.text = "Error: ${t.localizedMessage}"
            }
        })
    }

    companion object {
        private const val ARG_REPORT_UID = "report_uid"

        fun newInstance(reportUid: String): RcFragment {
            val fragment = RcFragment()
            val args = Bundle()
            args.putString(ARG_REPORT_UID, reportUid)
            fragment.arguments = args
            return fragment
        }
    }
}
