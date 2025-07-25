package com.example.aml.homepage.checkup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.aml.R
import com.example.aml.databinding.FragmentQuestion7Binding
import com.example.aml.databinding.FragmentQuestion8Binding

class Question8Fragment : Fragment() {

    private var _binding: FragmentQuestion8Binding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: FormViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuestion8Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val readOnly = arguments?.getBoolean("readOnly", false) ?: false

        val text = sharedViewModel.otherSymptoms.value ?: ""
        binding.inputText.setText(text)

        // Mode readOnly
        if (readOnly) {
            binding.inputText.isEnabled = false
            binding.btnNext.visibility = View.GONE
            binding.btnPrevious.visibility = View.GONE
        }

        // Tombol Previous → kembali ke Question6
        binding.btnPrevious.setOnClickListener {
            val answer = binding.inputText.text.toString().trim()
            sharedViewModel.setOtherSymptoms(answer)

            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_left, R.anim.slide_out_right,
                    R.anim.slide_in_right, R.anim.slide_out_left
                )
                .replace(R.id.homepageContainer, Question7Fragment())
                .addToBackStack(null)
                .commit()
        }

        // Tombol Back (ikon kiri atas) → ke CUFragment
        binding.btnBack.setOnClickListener {
            val answer = binding.inputText.text.toString().trim()
            sharedViewModel.setOtherSymptoms(answer)

            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_left, R.anim.slide_out_right,
                    R.anim.slide_in_right, R.anim.slide_out_left
                )
                .replace(R.id.homepageContainer, CUFragment()) // Kalau memang CUFragment tujuanmu
                .addToBackStack(null)
                .commit()
        }

        // Tombol Next (Submit) → ke ResultFragment
        binding.btnNext.setOnClickListener {
            val answer = binding.inputText.text.toString().trim()
            sharedViewModel.setOtherSymptoms(answer)

            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_right, R.anim.slide_out_left,
                    R.anim.slide_in_left, R.anim.slide_out_right
                )
                .replace(R.id.homepageContainer, ResultFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
