package com.example.aml.homepage.checkup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.aml.R
import com.example.aml.databinding.FragmentQuestion1Binding

class Question1Fragment : Fragment() {

    private var _binding: FragmentQuestion1Binding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: FormViewModel by activityViewModels()

    private val answerMap = mapOf(
        R.id.r1 to 1,
        R.id.r2 to 2,
        R.id.r3 to 3,
        R.id.r4 to 4,
        R.id.r5 to 5
    )

    private val answerTextMap = mapOf(
        R.id.r1 to "Tidak ada ingus sama sekali",
        R.id.r2 to "Ada sedikit ingus, tapi tidak mengalir keluar",
        R.id.r3 to "Ingus terasa mengalir ke depan (hidung) secara ringan",
        R.id.r4 to "Ingus mengalir cukup banyak ke depan atau ke belakang tenggorokan (post-nasal drip)",
        R.id.r5 to "Ingus mengalir sangat banyak dan terus-menerus, terasa mengganggu (baik ke depan maupun ke belakang)"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuestion1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val readOnly = arguments?.getBoolean("readOnly", false) ?: false
        val radioButtons = listOf(binding.r1, binding.r2, binding.r3, binding.r4, binding.r5)

        // Manual radio selection
        radioButtons.forEach { rb ->
            rb.setOnClickListener {
                binding.radioGroup.check(rb.id)
                saveSelection(rb.id)
            }
        }

        // Restore previous selection if any
        sharedViewModel.ingusFlow.value?.let { restoreSelection(it) }

        // Disable if readOnly
        if (readOnly) {
            disableRadioButtons(radioButtons)
            binding.btnNext.visibility = View.GONE
            binding.btnPrevious.visibility = View.GONE
        }

        binding.btnPrevious.setOnClickListener {
            navigateTo(CUFragment(), true)
        }

        binding.btnNext.setOnClickListener {
            val selectedId = binding.radioGroup.checkedRadioButtonId
            if (selectedId == -1) {
                binding.errorText.visibility = View.VISIBLE
            } else {
                saveSelection(selectedId)
                navigateTo(Question2Fragment(), false)
            }
        }
    }

    private fun saveSelection(checkedId: Int) {
        val selectedValue = answerMap[checkedId]
        val selectedText = answerTextMap[checkedId]
        if (selectedValue != null && selectedText != null) {
            sharedViewModel.setIngusFlow(selectedValue)
            sharedViewModel.setIngusFlowText(selectedText)
            binding.errorText.visibility = View.GONE
        }
    }

    private fun restoreSelection(value: Int) {
        val id = answerMap.entries.find { it.value == value }?.key
        id?.let { binding.radioGroup.check(it) }
    }

    private fun disableRadioButtons(buttons: List<RadioButton>) {
        buttons.forEach { it.isEnabled = false }
    }

    private fun navigateTo(fragment: Fragment, toPrevious: Boolean) {
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(
                if (toPrevious) R.anim.slide_in_left else R.anim.slide_in_right,
                if (toPrevious) R.anim.slide_out_right else R.anim.slide_out_left,
                if (toPrevious) R.anim.slide_in_right else R.anim.slide_in_left,
                if (toPrevious) R.anim.slide_out_left else R.anim.slide_out_right
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
