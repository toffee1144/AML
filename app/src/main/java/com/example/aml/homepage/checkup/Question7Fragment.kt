package com.example.aml.homepage.checkup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.aml.R
import com.example.aml.databinding.FragmentQuestion7Binding

class Question7Fragment : Fragment() {

    private var _binding: FragmentQuestion7Binding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: FormViewModel by activityViewModels()

    private val answerMap = mapOf(
        R.id.r1 to "Yes",
        R.id.r2 to "No"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuestion7Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val readOnly = arguments?.getBoolean("readOnly", false) ?: false
        val radioButtons = listOf(binding.r1, binding.r2)

        // Handle manual radio logic to make selection exclusive
        radioButtons.forEach { rb ->
            rb.setOnClickListener {
                binding.radioGroup.check(rb.id)
            }
        }

        // Restore previous value if available
        sharedViewModel.coughYesNo.value?.let { setRadioCheckedByValue(it) }

        if (readOnly) {
            disableRadioButtons(radioButtons)
            binding.btnNext2.visibility = View.GONE
            binding.btnPrevious.visibility = View.GONE
        }

        binding.btnPrevious.setOnClickListener {
            getSelectedValue()?.let { sharedViewModel.setCoughYesNo(it) }
            navigateTo(Question6Fragment())
        }

        binding.btnBack.setOnClickListener {
            getSelectedValue()?.let { sharedViewModel.setCoughYesNo(it) }
            navigateTo(CUFragment())
        }

        binding.btnNext2.setOnClickListener {
            getSelectedValue()?.let {
                sharedViewModel.setCoughYesNo(it)
                navigateTo(Question8Fragment())
            }
        }
    }

    private fun getSelectedValue(): String? {
        val selectedId = binding.radioGroup.checkedRadioButtonId
        return answerMap[selectedId]
    }

    private fun setRadioCheckedByValue(value: String) {
        val id = answerMap.entries.find { it.value == value }?.key
        id?.let { binding.radioGroup.check(it) }
    }

    private fun disableRadioButtons(buttons: List<RadioButton>) {
        buttons.forEach { it.isEnabled = false }
    }

    private fun navigateTo(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right, R.anim.slide_out_left,
                R.anim.slide_in_left, R.anim.slide_out_right
            )
            .replace(R.id.homepageContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
