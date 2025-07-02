package com.example.aml.homepage.checkup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.aml.R
import com.example.aml.databinding.FragmentCUBinding
import com.example.aml.utility.DeviceIdManager

class CUFragment : Fragment() {

    private lateinit var binding: FragmentCUBinding
    private val sharedViewModel: FormViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCUBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val readOnly = arguments?.getBoolean("readOnly", false) ?: false

        if (readOnly) {
            // Set Likert values from ViewModel
            sharedViewModel.symptomDuration.value?.let {
                setLikertChecked(binding.radioDuration, it)
            }
            sharedViewModel.nasalDischarge.value?.let {
                setLikertChecked(binding.radioDischarge, it)
            }
            sharedViewModel.anosmia.value?.let {
                setLikertChecked(binding.radioAnosmia, it)
            }
            sharedViewModel.facialPain.value?.let {
                setLikertChecked(binding.radioFacepain, it)
            }
            sharedViewModel.fever.value?.let {
                setLikertChecked(binding.radioFever, it)
            }
            sharedViewModel.congestion.value?.let {
                setLikertChecked(binding.radioCongestion, it)
            }

            // Set Yes/No values
            sharedViewModel.painFluctuate.value?.let {
                setYesNoChecked(binding.radioFluctuating, it)
            }
            sharedViewModel.coughYesNo.value?.let {
                setYesNoChecked(binding.radioCoughYesno, it)
            }

            // Disable all RadioGroups (readonly mode)
            val radioGroups = listOf(
                binding.radioDuration,
                binding.radioDischarge,
                binding.radioAnosmia,
                binding.radioFacepain,
                binding.radioFever,
                binding.radioCongestion,
                binding.radioFluctuating,
                binding.radioCoughYesno
            )

            radioGroups.forEach { group ->
                for (i in 0 until group.childCount) {
                    group.getChildAt(i).isEnabled = false
                }
            }

            // Hide Submit button
            binding.buttonSubmit.visibility = View.GONE
        }

        binding.buttonSubmit.setOnClickListener {
            validateAndSubmit()
        }
    }

    private fun validateAndSubmit() {
        val duration = getSelectedLikertValue(binding.radioDuration)
        val discharge = getSelectedLikertValue(binding.radioDischarge)
        val anosmia = getSelectedLikertValue(binding.radioAnosmia)
        val facePain = getSelectedLikertValue(binding.radioFacepain)
        val fever = getSelectedLikertValue(binding.radioFever)
        val congestion = getSelectedLikertValue(binding.radioCongestion)

        val painFluctuate = getSelectedValue(binding.radioFluctuating)
        val coughYesNo = getSelectedValue(binding.radioCoughYesno)

        if (duration == null || discharge == null || anosmia == null ||
            facePain == null || fever == null || congestion == null ||
            painFluctuate == null || coughYesNo == null
        ) {
            Toast.makeText(requireContext(), "Please answer all questions", Toast.LENGTH_SHORT).show()
            return
        }

        sharedViewModel.setScreeningInfo(
            symptomDuration = duration,
            nasalDischarge = discharge,
            anosmia = anosmia,
            facialPain = facePain,
            fever = fever,
            congestion = congestion
        )

        sharedViewModel.setExtraSymptoms(
            painFluctuate = painFluctuate,
            coughYesNo = coughYesNo
        )

        Toast.makeText(requireContext(), "Form submitted!", Toast.LENGTH_SHORT).show()

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.homepageContainer, ResultFragment())
            .addToBackStack(null)
            .commit()
    }

    // ===== Helper Functions =====

    private fun setLikertChecked(radioGroup: RadioGroup, value: Int) {
        for (i in 0 until radioGroup.childCount) {
            val radio = radioGroup.getChildAt(i) as? RadioButton
            if (radio?.text?.toString() == value.toString()) {
                radio.isChecked = true
                break
            }
        }
    }

    private fun setYesNoChecked(radioGroup: RadioGroup, value: String) {
        for (i in 0 until radioGroup.childCount) {
            val radio = radioGroup.getChildAt(i) as? RadioButton
            if (radio?.text?.toString()?.equals(value, ignoreCase = true) == true) {
                radio.isChecked = true
                break
            }
        }
    }

    private fun getSelectedLikertValue(radioGroup: RadioGroup): Int? {
        val selectedId = radioGroup.checkedRadioButtonId
        return if (selectedId != -1) {
            val radioButton = radioGroup.findViewById<RadioButton>(selectedId)
            radioButton.text.toString().toIntOrNull()
        } else null
    }

    private fun getSelectedValue(radioGroup: RadioGroup): String? {
        val selectedId = radioGroup.checkedRadioButtonId
        return if (selectedId != -1) {
            val radioButton = radioGroup.findViewById<RadioButton>(selectedId)
            radioButton.text.toString()
        } else null
    }
}
