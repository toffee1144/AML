package com.example.aml.homepage.report

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.aml.R
import com.example.aml.homepage.checkup.PIFragment
import com.example.aml.homepage.checkup.CUFragment
import com.example.aml.model.FormData
import com.example.aml.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChecklistFragment : Fragment(R.layout.fragment_checklist) {

    private lateinit var btnIdentity: TextView
    private lateinit var btnScreening: TextView
    private var reportUid: String? = null

    private var formData: FormData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        reportUid = arguments?.getString(ARG_REPORT_UID)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnIdentity = view.findViewById(R.id.btnIdentity)
        btnScreening = view.findViewById(R.id.btnScreening)

        btnIdentity.setOnClickListener {
            formData?.let {
                loadPIFragment(it)
                highlightTab(isIdentity = true)
            }
        }

        btnScreening.setOnClickListener {
            formData?.let {
                loadCUFragment(it)
                highlightTab(isIdentity = false)
            }
        }

        reportUid?.let { fetchFormData(it) }
    }

    private fun fetchFormData(uid: String) {
        ApiClient.apiService.getReportByUid(uid).enqueue(object : Callback<FormData> {
            override fun onResponse(call: Call<FormData>, response: Response<FormData>) {
                if (response.isSuccessful) {
                    formData = response.body()
                    formData?.let {
                        loadPIFragment(it)
                        highlightTab(isIdentity = true)
                    }
                }
            }

            override fun onFailure(call: Call<FormData>, t: Throwable) {
                // Optionally show an error or log
            }
        })
    }

    private fun loadPIFragment(data: FormData) {
        val fragment = PIFragment().apply {
            arguments = Bundle().apply {
                putBoolean("readOnly", true)
                putString("reportName", data.reportName)
                putInt("age", data.age)
                putString("sex", data.sex)
                putString("familyHistory", data.familyHistory)
                putString("checkupStatus", data.checkupStatus) // ✅ Added
                putString("doctorHospital", data.doctorHospital) // ✅ Added
            }
        }

        childFragmentManager.beginTransaction()
            .replace(R.id.child_fragment_container, fragment)
            .commit()
    }

    private fun loadCUFragment(data: FormData) {
        val fragment = CUFragment().apply {
            arguments = Bundle().apply {
                putBoolean("readOnly", true)
                putInt("symptomDuration", data.symptomDuration)
                putInt("nasalDischarge", data.nasalDischarge)
                putInt("anosmia", data.anosmia)
                putInt("facialPain", data.facialPain)
                putInt("fever", data.fever)
                putInt("congestion", data.congestion)
                putString("painFluctuate", data.painFluctuate)
                putString("coughYesNo", data.coughYesNo)
            }
        }

        childFragmentManager.beginTransaction()
            .replace(R.id.child_fragment_container, fragment)
            .commit()
    }

    private fun highlightTab(isIdentity: Boolean) {
        val orangeBg = R.drawable.shape_orange_bg
        val whiteColor = ContextCompat.getColor(requireContext(), R.color.white)

        if (isIdentity) {
            btnIdentity.setBackgroundResource(orangeBg)
            btnIdentity.setTextColor(whiteColor)

            btnScreening.setBackgroundColor(Color.TRANSPARENT)
            btnScreening.setTextColor(whiteColor)
        } else {
            btnScreening.setBackgroundResource(orangeBg)
            btnScreening.setTextColor(whiteColor)

            btnIdentity.setBackgroundColor(Color.TRANSPARENT)
            btnIdentity.setTextColor(whiteColor)
        }
    }

    companion object {
        private const val ARG_REPORT_UID = "report_uid"

        fun newInstance(reportUid: String): ChecklistFragment {
            return ChecklistFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_REPORT_UID, reportUid)
                }
            }
        }
    }
}
