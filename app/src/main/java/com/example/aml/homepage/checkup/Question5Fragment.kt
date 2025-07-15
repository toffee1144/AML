package com.example.aml.homepage.checkup

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.aml.R
import com.example.aml.databinding.FragmentQuestion5Binding

class Question5Fragment : Fragment() {

    private var _binding: FragmentQuestion5Binding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: FormViewModel by activityViewModels()

    private var selectedImageUri: Uri? = null

    companion object {
        private const val REQUEST_CODE_UPLOAD_PHOTO = 101
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuestion5Binding.inflate(inflater, container, false)
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

        // Restore nilai dari ViewModel
        sharedViewModel.getCongestion()?.let { setLikertChecked(it) }
        sharedViewModel.ingusPhotoUri.value?.let { selectedImageUri = it }

        if (readOnly) {
            disableRadioButtons()
            binding.btnNext.visibility = View.GONE
            binding.btnPrevious.visibility = View.GONE
            binding.btnUploadPhoto.isEnabled = false
        }

        // Tombol Upload Foto
        binding.btnUploadPhoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE_UPLOAD_PHOTO)
        }

        // Tombol Back (pojok kiri atas)
        binding.btnBack.setOnClickListener {
            val selected = getSelectedLikertValue()
            if (selected != null) {
                sharedViewModel.setCongestion(selected)
            }
            selectedImageUri?.let { sharedViewModel.setIngusPhotoUri(it) }

            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_left, R.anim.slide_out_right,
                    R.anim.slide_in_right, R.anim.slide_out_left
                )
                .replace(R.id.homepageContainer, CUFragment())
                .addToBackStack(null)
                .commit()
        }

        // Tombol Previous Page ke Question4Fragment
        binding.btnPrevious.setOnClickListener {
            val selected = getSelectedLikertValue()
            if (selected != null) {
                sharedViewModel.setCongestion(selected)
            }
            selectedImageUri?.let { sharedViewModel.setIngusPhotoUri(it) }

            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_left, R.anim.slide_out_right,
                    R.anim.slide_in_right, R.anim.slide_out_left
                )
                .replace(R.id.homepageContainer, Question4Fragment())
                .addToBackStack(null)
                .commit()
        }

        // Tombol Next Page ke Question6Fragment
        binding.btnNext.setOnClickListener {
            val selected = getSelectedLikertValue()
            if (selected == null) {
                binding.errorText.visibility = View.VISIBLE
                return@setOnClickListener
            }

            if (selectedImageUri == null) {
                Toast.makeText(requireContext(), "Mohon upload foto ingus Anda!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            sharedViewModel.setCongestion(selected)
            sharedViewModel.setIngusPhotoUri(selectedImageUri!!)

            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_right, R.anim.slide_out_left,
                    R.anim.slide_in_left, R.anim.slide_out_right
                )
                .replace(R.id.homepageContainer, Question6Fragment())
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_UPLOAD_PHOTO && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data
            selectedImageUri?.let {
                Toast.makeText(requireContext(), "Foto berhasil dipilih!", Toast.LENGTH_SHORT).show()
                // TODO: Tambahkan preview image ke UI jika diperlukan
            }
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
        val radioButtons = listOf(binding.r1, binding.r2, binding.r3, binding.r4, binding.r5)
        radioButtons.forEach { it.isEnabled = false }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
