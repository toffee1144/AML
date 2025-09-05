package com.mentari.sinuscan.homepage.checkup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.mentari.sinuscan.R
import com.mentari.sinuscan.databinding.FragmentPIBinding

class PIFragment : Fragment() {

    private var _binding: FragmentPIBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: FormViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPIBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDropdowns()

        val readOnly = arguments?.getBoolean("readOnly", false) ?: false

        if (readOnly) {
            // Baca data dari arguments, bukan dari ViewModel
            val reportName = arguments?.getString("reportName") ?: ""
            val age = arguments?.getInt("age", 0) ?: 0
            val familyHistory = arguments?.getString("familyHistory") ?: ""
            val sex = arguments?.getString("sex") ?: ""

            binding.evReportName.setText(reportName)
            binding.editTextAge.setText(if (age == 0) "" else age.toString())
            binding.editTextFamilyHistory.setText(familyHistory)

            val sexIndex = when (sex) {
                "Male" -> 1
                "Female" -> 2
                "Other" -> 3
                else -> 0
            }
            binding.spinnerSex.setSelection(sexIndex)

            // Kalau perlu set juga spinnerCheckup dan doctorHospital
            val checkupStatus = arguments?.getString("checkupStatus") ?: ""
            val doctorHospital = arguments?.getString("doctorHospital") ?: ""

            val checkupOptions = listOf("Select an option", "Yes", "No")
            val checkupIndex = checkupOptions.indexOf(checkupStatus)
            if (checkupIndex >= 0) {
                binding.spinnerCheckup.setSelection(checkupIndex)
            }
            binding.editTextDoctorHospital.setText(doctorHospital)

            // Disable semua input agar readOnly
            binding.evReportName.isEnabled = false
            binding.editTextAge.isEnabled = false
            binding.editTextFamilyHistory.isEnabled = false
            binding.spinnerSex.isEnabled = false
            binding.spinnerCheckup.isEnabled = false
            binding.editTextDoctorHospital.isEnabled = false
            binding.buttonNext.visibility = View.GONE
        } else {
            // Kalau bukan readOnly, bisa setup biasa atau kosong
        }

        binding.buttonNext.setOnClickListener {
            validateAndGoNext()
        }
    }



    private fun setupDropdowns() {
        val sexOptions = listOf("Select Sex", "Male", "Female", "Other")
        val sexAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, sexOptions)
        binding.spinnerSex.adapter = sexAdapter

        val checkupOptions = listOf("Select an option", "Yes", "No")
        val checkupAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, checkupOptions)
        binding.spinnerCheckup.adapter = checkupAdapter
    }

    private fun validateAndGoNext() {
        val reportName = binding.evReportName.text.toString().trim()
        val ageText = binding.editTextAge.text.toString().trim()
        val sex = binding.spinnerSex.selectedItem.toString()
        val checkupStatus = binding.spinnerCheckup.selectedItem.toString()
        val doctorHospital = binding.editTextDoctorHospital.text.toString().trim()
        val familyHistory = binding.editTextFamilyHistory.text.toString().trim()

        if (reportName.isEmpty()) {
            binding.evReportName.error = "Report name is required"
            return
        }

        if (ageText.isEmpty()) {
            binding.editTextAge.error = "Age is required"
            return
        }

        if (sex == "Select Sex") {
            Toast.makeText(requireContext(), "Please select your sex", Toast.LENGTH_SHORT).show()
            return
        }

        if (checkupStatus == "Select an option") {
            Toast.makeText(requireContext(), "Please select a checkup status", Toast.LENGTH_SHORT).show()
            return
        }

        if (doctorHospital.isEmpty()) {
            binding.editTextDoctorHospital.error = "Please enter doctor and hospital name"
            return
        }

        if (familyHistory.isEmpty()) {
            binding.editTextFamilyHistory.error = "Family history is required"
            return
        }

        // Store in ViewModel
        sharedViewModel.setPersonalInfo(reportName, ageText.toInt(), sex, checkupStatus, doctorHospital, familyHistory)

        // Proceed
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.homepageContainer, PhotoQuestionFragment())
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
