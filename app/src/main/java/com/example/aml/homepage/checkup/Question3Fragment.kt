package com.example.aml.homepage.checkup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.aml.R
import com.example.aml.databinding.FragmentQuestion3Binding

class Question3Fragment : Fragment() {

    private var _binding: FragmentQuestion3Binding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: FormViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuestion3Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val readOnly = arguments?.getBoolean("readOnly", false) ?: false

        val likertButtons = listOf(
            binding.likert1,
            binding.likert2,
            binding.likert3,
            binding.likert4,
            binding.likert5
        )

        fun updateLikertSelection(selectedIndex: Int) {
            likertButtons.forEachIndexed { index, imageView ->
                val bgRes = if (index == selectedIndex) R.drawable.bg_likert_selected else R.drawable.bg_likert_unselected
                (imageView.parent as View).setBackgroundResource(bgRes)
            }
        }

        likertButtons.forEachIndexed { index, imageView ->
            imageView.setOnClickListener {
                updateLikertSelection(index)
                sharedViewModel.setFacialPain(index + 1)
                binding.errorText.visibility = View.GONE
            }
        }

        sharedViewModel.getFacialPain()?.let {
            if (it in 1..5) updateLikertSelection(it - 1)
        }

        if (readOnly) {
            likertButtons.forEach { it.isEnabled = false }
            binding.btnNext.visibility = View.GONE
            binding.btnPrevious.visibility = View.GONE
        }

        binding.btnBack.setOnClickListener {
            sharedViewModel.getFacialPain()?.let { sharedViewModel.setFacialPain(it) }
            navigateToFragment(CUFragment(), R.anim.slide_in_left, R.anim.slide_out_right)
        }

        binding.btnPrevious.setOnClickListener {
            sharedViewModel.getFacialPain()?.let { sharedViewModel.setFacialPain(it) }
            navigateToFragment(Question2Fragment(), R.anim.slide_in_left, R.anim.slide_out_right)
        }

        binding.btnNext.setOnClickListener {
            val selected = sharedViewModel.getFacialPain()
            if (selected == null) {
                binding.errorText.visibility = View.VISIBLE
                return@setOnClickListener
            }
            navigateToFragment(Question4Fragment(), R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    private fun navigateToFragment(fragment: Fragment, enterAnim: Int, exitAnim: Int) {
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(
                enterAnim, exitAnim,
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
