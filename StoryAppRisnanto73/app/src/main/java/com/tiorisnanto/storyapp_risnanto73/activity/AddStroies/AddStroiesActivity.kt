package com.tiorisnanto.storyapp_risnanto73.activity.AddStroies

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.tiorisnanto.storyapp_risnanto73.R
import com.tiorisnanto.storyapp_risnanto73.data.model.UserModel
import com.tiorisnanto.storyapp_risnanto73.databinding.ActivityAddStroiesBinding
import com.tiorisnanto.storyapp_risnanto73.helper.Helper
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class AddStroiesActivity : AppCompatActivity() {

    companion object {
        const val CAMERA_X_RESULT = 200

        const val EXTRA_USER = "user"

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    private lateinit var binding: ActivityAddStroiesBinding
    private lateinit var user: UserModel
    private var getFile: File? = null
    private var result: Bitmap? = null
    private val viewModel = AddStroiesViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStroiesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Add Stories"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        user = intent.getParcelableExtra(EXTRA_USER)!!

        getPermission()

        binding.btnCameraX.setOnClickListener { startCameraX() }
        binding.btnGallery.setOnClickListener { startGallery() }
        binding.btnUpload.setOnClickListener { uploadImage() }

    }

    private fun uploadImage() {
        val description = binding.etDescription.text.toString().trim()
        var isEmptyField = false
        if(description.isEmpty()){
            isEmptyField = true
            binding.etDescription.error = getString(R.string.error)
        }
        if (!isEmptyField){
            if (getFile != null) {
                val file = Helper.reduceFileImage(getFile as File)



                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart = MultipartBody.Part.createFormData(
                    "photo",
                    file.name,
                    requestImageFile
                )

                // upload image
                viewModel.uploadImage(user, description, imageMultipart, object : Helper.ApiCallbackString {
                    override fun onResponse(success: Boolean, message: String) {
                        showAlertDialog(success, message)
                    }
                })

            } else {
                Helper.showToast(this@AddStroiesActivity, getString(R.string.no_attach_file))
            }
        }


    }

    private fun showAlertDialog(param: Boolean, message: String) {
        if (param) {
            AlertDialog.Builder(this).apply {
                setTitle(getString(R.string.information))
                setMessage(getString(R.string.upload_success))
                setPositiveButton(getString(R.string.continue_reading)) { _, _ ->
                    // Do nothing
                }
                finish()
                create()
                show()
            }
        } else {
            AlertDialog.Builder(this).apply {
                setTitle(getString(R.string.information))
                setMessage(getString(R.string.upload_failed) + ", $message")
                setPositiveButton(getString(R.string.continue_reading)) { _, _ ->
                    binding.progressBar.visibility = View.GONE
                }
                create()
                show()
            }
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val selectedImg: Uri = it.data?.data as Uri
            val myFile = Helper.uriToFile(selectedImg, this@AddStroiesActivity)
            getFile = myFile
            binding.ivPreview.setImageURI(selectedImg)
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            getFile = myFile
            result =
                BitmapFactory.decodeFile(getFile?.path)
        }
        binding.ivPreview.setImageBitmap(result)
    }

    private fun startCameraX() {
        launcherIntentCameraX.launch(Intent(this, CameraXActivity::class.java))
    }

    private fun getPermission() {
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private fun allPermissionsGranted()= REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}