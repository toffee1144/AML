package com.mentari.sinuscan.homepage.report

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.mentari.sinuscan.R
import com.mentari.sinuscan.homepage.checkup.CUFragment
import com.mentari.sinuscan.homepage.checkup.PIFragment
import com.mentari.sinuscan.model.FormData
import com.mentari.sinuscan.network.ApiClient
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
        Log.d("ChecklistFragment", "[onCreate] Received reportUid: $reportUid")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnIdentity = view.findViewById(R.id.btnIdentity)
        btnScreening = view.findViewById(R.id.btnScreening)

        btnIdentity.setOnClickListener {
            Log.d("ChecklistFragment", "[btnIdentity] Clicked")
            formData?.let {
                Log.d("ChecklistFragment", "[btnIdentity] Loading PIFragment with data: $it")
                loadPIFragment(it)
                highlightTab(isIdentity = true)
            } ?: Log.w("ChecklistFragment", "[btnIdentity] formData is null, cannot load PIFragment")
        }

        btnScreening.setOnClickListener {
            Log.d("ChecklistFragment", "[btnScreening] Clicked")
            formData?.let {
                Log.d("ChecklistFragment", "[btnScreening] Loading CUFragment with data: $it")
                loadCUFragment(it)
                highlightTab(isIdentity = false)
            } ?: Log.w("ChecklistFragment", "[btnScreening] formData is null, cannot load CUFragment")
        }

        if (reportUid != null) {
            Log.d("ChecklistFragment", "[onViewCreated] Starting fetchFormData for UID: $reportUid")
            fetchFormData(reportUid!!)
        } else {
            Log.e("ChecklistFragment", "[onViewCreated] reportUid is null, cannot fetch data")
        }
    }

    private fun fetchFormData(uid: String) {
        Log.d("ChecklistFragment", "[fetchFormData] Fetching form data for UID: $uid")
        ApiClient.apiService.getReportByUid(uid).enqueue(object : Callback<FormData> {
            override fun onResponse(call: Call<FormData>, response: Response<FormData>) {
                Log.d("ChecklistFragment", "[fetchFormData.onResponse] response.isSuccessful=${response.isSuccessful}")
                if (response.isSuccessful) {
                    formData = response.body()
                    if (formData != null) {
                        Log.d("ChecklistFragment", "[fetchFormData.onResponse] Data fetched successfully for UID: $uid -> $formData")
                        loadPIFragment(formData!!)
                        highlightTab(isIdentity = true)
                    } else {
                        Log.e("ChecklistFragment", "[fetchFormData.onResponse] Data is null for UID: $uid")
                    }
                } else {
                    Log.e("ChecklistFragment", "[fetchFormData.onResponse] Failed to fetch data for UID: $uid, code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<FormData>, t: Throwable) {
                Log.e("ChecklistFragment", "[fetchFormData.onFailure] API call failed: ${t.localizedMessage}")
            }
        })
    }

    private fun loadPIFragment(data: FormData) {
        Log.d("ChecklistFragment", "[loadPIFragment] Preparing PIFragment with data: $data")
        val fragment = PIFragment().apply {
            arguments = Bundle().apply {
                putBoolean("readOnly", true)
                putString("reportName", data.reportName)
                putInt("age", data.age)
                putString("sex", data.sex)
                putString("familyHistory", data.familyHistory)
                putString("checkupStatus", data.checkupStatus)
                putString("doctorHospital", data.doctorHospital)
            }
        }

        childFragmentManager.beginTransaction()
            .replace(R.id.child_fragment_container, fragment)
            .commit()
        Log.d("ChecklistFragment", "[loadPIFragment] PIFragment committed")
    }

    private fun loadCUFragment(data: FormData) {
        Log.d("ChecklistFragment", "[loadCUFragment] Preparing CUFragment with data: $data")
        val fragment = CUFragment().apply {
            arguments = Bundle().apply {
                putBoolean("readOnly", true)
                putInt("symptomDuration", data.symptomDuration)
                putInt("nasalDischarge", data.nasalDischarge)
                putInt("anosmia", data.anosmia)
                putInt("facialPain", data.facialPain)
                putInt("fever", data.fever)
                putInt("congestion", data.congestion)
                putString("coughYesNo", data.coughYesNo)
            }
        }

        childFragmentManager.beginTransaction()
            .replace(R.id.child_fragment_container, fragment)
            .commit()
        Log.d("ChecklistFragment", "[loadCUFragment] CUFragment committed")
    }

    private fun highlightTab(isIdentity: Boolean) {
        val orangeBg = R.drawable.shape_orange_bg
        val whiteColor = ContextCompat.getColor(requireContext(), R.color.white)

        Log.d("ChecklistFragment", "[highlightTab] isIdentity=$isIdentity")

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
