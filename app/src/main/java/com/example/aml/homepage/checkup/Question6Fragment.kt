package com.example.aml.homepage.checkup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.aml.R
import com.example.aml.databinding.FragmentQuestion6Binding

class Question6Fragment : Fragment() {

    private var _binding: FragmentQuestion6Binding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: FormViewModel by activityViewModels()

    private val answerMap = mapOf(
        R.id.r1 to 1,  // Tidak ada gejala sama sekali
        R.id.r2 to 2,  // < 3 hari
        R.id.r3 to 3,  // 3–5 hari
        R.id.r4 to 4,  // 5–10 hari
        R.id.r5 to 5   // > 10 hari
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuestion6Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val readOnly = arguments?.getBoolean("readOnly", false) ?: false
        val radioButtons = listOf(binding.r1, binding.r2, binding.r3, binding.r4, binding.r5)

        // Manual radio selection
        radioButtons.forEach { rb ->
            rb.setOnClickListener {
                binding.radioGroup.check(rb.id)
            }
        }

        // Restore jawaban jika ada
        sharedViewModel.symptomDuration.value?.let { setRadioCheckedByValue(it) }

        // Mode readOnly
        if (readOnly) {
            disableRadioButtons(radioButtons)
            binding.btnNext.visibility = View.GONE
            binding.btnPrevious.visibility = View.GONE
        }

        // Tombol BACK (pojok kiri atas) → ke CUFragment
        binding.btnBack.setOnClickListener {
            val selected = getSelectedLikertValue()
            if (selected != null) {
                sharedViewModel.setSymptomDuration(selected)
            }

            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_left, R.anim.slide_out_right,
                    R.anim.slide_in_right, R.anim.slide_out_left
                )
                .replace(R.id.homepageContainer, CUFragment())
                .addToBackStack(null)
                .commit()
        }

        // Tombol Previous Page → ke Question5Fragment
        binding.btnPrevious.setOnClickListener {
            val selected = getSelectedLikertValue()
            if (selected != null) {
                sharedViewModel.setSymptomDuration(selected)
            }

            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_left, R.anim.slide_out_right,
                    R.anim.slide_in_right, R.anim.slide_out_left
                )
                .replace(R.id.homepageContainer, Question5Fragment())
                .addToBackStack(null)
                .commit()
        }

        // Tombol NEXT → ke Question7Fragment
        binding.btnNext.setOnClickListener {
            val selected = getSelectedLikertValue()
            if (selected == null) {
                binding.errorText.visibility = View.VISIBLE
                return@setOnClickListener
            }

            sharedViewModel.setSymptomDuration(selected)
            navigateTo(Question7Fragment())
        }
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

    private fun getSelectedLikertValue(): Int? {
        val selectedId = binding.radioGroup.checkedRadioButtonId
        return answerMap[selectedId]
    }

    private fun setRadioCheckedByValue(value: Int) {
        val id = answerMap.entries.find { it.value == value }?.key
        id?.let { binding.radioGroup.check(it) }
    }

    private fun disableRadioButtons(buttons: List<RadioButton>) {
        buttons.forEach { it.isEnabled = false }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
