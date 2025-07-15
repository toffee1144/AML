package com.example.aml.homepage.checkup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.aml.R
import com.example.aml.databinding.FragmentQuestion4Binding
import kotlin.text.replace

class Question4Fragment : Fragment() {

    private var _binding: FragmentQuestion4Binding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: FormViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuestion4Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val readOnly = arguments?.getBoolean("readOnly", false) ?: false

        val radioButtons = listOf(binding.r1, binding.r2, binding.r3, binding.r4, binding.r5)
        radioButtons.forEach { rb ->
            rb.setOnClickListener {
                radioButtons.forEach { it.isChecked = false }
                rb.isChecked = true
            }
        }

        // Selalu restore jawaban jika ada
        sharedViewModel.getFever()?.let {
            setLikertChecked(it)
        }

        // Jika read-only, disable komponen input
        if (readOnly) {
            disableRadioButtons()
            binding.btnNext.visibility = View.GONE
            binding.btnPrevious.visibility = View.GONE
        }

        // Tombol Back (pojok kiri atas)
        binding.btnBack.setOnClickListener {
            val selected = getSelectedLikertValue()
            if (selected != null) {
                sharedViewModel.setFever(selected)
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

        // Tombol Previous Page (ke Question3)
        binding.btnPrevious.setOnClickListener {
            val selected = getSelectedLikertValue()
            if (selected != null) {
                sharedViewModel.setFever(selected)
            }

            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_left, R.anim.slide_out_right,
                    R.anim.slide_in_right, R.anim.slide_out_left
                )
                .replace(R.id.homepageContainer, Question3Fragment())
                .addToBackStack(null)
                .commit()
        }

        // Tombol Next Page (ke Question5)
        binding.btnNext.setOnClickListener {
            val selected = getSelectedLikertValue()
            if (selected == null) {
                binding.errorText.visibility = View.VISIBLE
                return@setOnClickListener
            }

            sharedViewModel.setFever(selected)

            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_right, R.anim.slide_out_left,
                    R.anim.slide_in_left, R.anim.slide_out_right
                )
                .replace(R.id.homepageContainer, Question5Fragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun getSelectedLikertValue(): Int? {
        return when {
            binding.r1.isChecked -> 1
            binding.r2.isChecked -> 2
            binding.r3.isChecked -> 3
            binding.r4.isChecked -> 4
            binding.r5.isChecked -> 5
            else -> null
        }
    }

    private fun setLikertChecked(value: Int) {
        when (value) {
            1 -> binding.r1.isChecked = true
            2 -> binding.r2.isChecked = true
            3 -> binding.r3.isChecked = true
            4 -> binding.r4.isChecked = true
            5 -> binding.r5.isChecked = true
        }
    }

    private fun disableRadioButtons() {
        listOf(binding.r1, binding.r2, binding.r3, binding.r4, binding.r5)
            .forEach { it.isEnabled = false }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
