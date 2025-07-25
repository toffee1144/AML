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
import com.example.aml.databinding.FragmentPhotoQuestionBinding

class PhotoQuestionFragment : Fragment() {

    private var _binding: FragmentPhotoQuestionBinding? = null
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
        _binding = FragmentPhotoQuestionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val readOnly = arguments?.getBoolean("readOnly", false) ?: false

        sharedViewModel.ingusPhotoUri.value?.let { selectedImageUri = it }

        if (readOnly) {
            binding.btnNext.visibility = View.GONE
            binding.btnBack.visibility = View.GONE
            binding.btnUploadPhoto.isEnabled = false
        }

        binding.btnUploadPhoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE_UPLOAD_PHOTO)
        }

        binding.btnBack.setOnClickListener {
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

        binding.btnNext.setOnClickListener {
            // Save photo if available (optional)
            selectedImageUri?.let {
                sharedViewModel.setIngusPhotoUri(it)
            }

            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_right, R.anim.slide_out_left,
                    R.anim.slide_in_left, R.anim.slide_out_right
                )
                .replace(R.id.homepageContainer, Question1Fragment())
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
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
