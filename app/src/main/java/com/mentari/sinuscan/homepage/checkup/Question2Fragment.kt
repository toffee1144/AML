package com.mentari.sinuscan.homepage.checkup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.mentari.sinuscan.R
import com.mentari.sinuscan.databinding.FragmentQuestion2Binding

class Question2Fragment : Fragment() {

    private var _binding: FragmentQuestion2Binding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: FormViewModel by activityViewModels()
    private var selectedLikert: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuestion2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val readOnly = arguments?.getBoolean("readOnly", false) ?: false

        val likertViews = listOf(
            binding.likert1,
            binding.likert2,
            binding.likert3,
            binding.likert4,
            binding.likert5
        )

        // Manual single-select image logic
        likertViews.forEachIndexed { index, imageView ->
            imageView.setOnClickListener {
                if (!readOnly) {
                    updateLikertSelection(index + 1)
                }
            }
        }

        // Restore previous answer
        sharedViewModel.getAnosmia()?.let {
            updateLikertSelection(it)
        }

        if (readOnly) {
            likertViews.forEach { it.isEnabled = false }
            binding.btnNext.visibility = View.GONE
            binding.btnPrevious.visibility = View.GONE
        }

        binding.btnNext.setOnClickListener {
            val selected = getSelectedLikertValue()
            if (selected == null) {
                binding.errorText.visibility = View.VISIBLE
                return@setOnClickListener
            }

            sharedViewModel.setAnosmia(selected)

            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_right, R.anim.slide_out_left,
                    R.anim.slide_in_left, R.anim.slide_out_right
                )
                .replace(R.id.homepageContainer, Question3Fragment())
                .addToBackStack(null)
                .commit()
        }

        binding.btnPrevious.setOnClickListener {
            getSelectedLikertValue()?.let { sharedViewModel.setAnosmia(it) }

            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_left, R.anim.slide_out_right,
                    R.anim.slide_in_right, R.anim.slide_out_left
                )
                .replace(R.id.homepageContainer, Question1Fragment())
                .addToBackStack(null)
                .commit()
        }

        binding.btnBack.setOnClickListener {
            getSelectedLikertValue()?.let { sharedViewModel.setAnosmia(it) }

            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_left, R.anim.slide_out_right,
                    R.anim.slide_in_right, R.anim.slide_out_left
                )
                .replace(R.id.homepageContainer, CUFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun updateLikertSelection(value: Int) {
        val backgrounds = listOf(
            binding.likert1.parent as View,
            binding.likert2.parent as View,
            binding.likert3.parent as View,
            binding.likert4.parent as View,
            binding.likert5.parent as View
        )

        backgrounds.forEachIndexed { index, view ->
            view.setBackgroundResource(
                if (index + 1 == value) R.drawable.bg_likert_selected
                else R.drawable.bg_likert_unselected
            )
        }

        selectedLikert = value
        binding.errorText.visibility = View.GONE
    }

    private fun getSelectedLikertValue(): Int? {
        return selectedLikert
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
