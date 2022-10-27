package com.tiorisnanto.storyapp_risnanto73.activity.addstroies

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.tiorisnanto.storyapp_risnanto73.R
import com.tiorisnanto.storyapp_risnanto73.activity.viewmodel.ViewModelFactory
import com.tiorisnanto.storyapp_risnanto73.data.model.UserModel
import com.tiorisnanto.storyapp_risnanto73.data.resultresponse.ResultResponse
import com.tiorisnanto.storyapp_risnanto73.databinding.ActivityAddStroiesBinding
import com.tiorisnanto.storyapp_risnanto73.helper.Helper
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddStroiesActivity : AppCompatActivity() {

    companion object {
        const val CAMERA_X_RESULT = 200
        private const val TAG = "AddStoryActivity"
        const val EXTRA_USER = "user"

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    private lateinit var binding: ActivityAddStroiesBinding
    private lateinit var user: UserModel
    private var getFile: File? = null
    private var result: Bitmap? = null
    private var lokasi: Location? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val viewModel: AddStroiesViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStroiesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Add Stories"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        user = intent.getParcelableExtra(EXTRA_USER)!!
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        getPermission()

        binding.btnCameraX.setOnClickListener { startCameraX() }
        binding.btnGallery.setOnClickListener { startGallery() }
        binding.btnUpload.setOnClickListener { uploadImage() }
        binding.switchCompact.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                getMyLocation()
            } else {
                lokasi = null
            }
        }


    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        Log.d(TAG, "$it")
        if (it[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
            getMyLocation()
        } else binding.switchCompact.isChecked = false
    }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) { // Location permission granted, then set lokasi
            fusedLocationClient.lastLocation.addOnSuccessListener {
                if (it != null) {
                    lokasi = it
                    Log.d(TAG, "Lat : ${it.latitude}, Lon : ${it.longitude}")
                } else {
                    Helper.showToastLong(this, getString(R.string.enable_gps_permission))
                    binding.switchCompact.isChecked = false
                }
            }
        } else { // Location permission denied, then request permission
            requestPermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION))
        }
    }

    private fun uploadImage() {
        when {
            binding.etDescription.text.toString().isEmpty() -> {
                binding.etDescription.error = getString(R.string.error)
            }
            getFile != null -> {
                val file = Helper.reduceFileImage(getFile as File)
                val description = binding.etDescription.text.toString()
                    .toRequestBody("application/json;charset=utf-8".toMediaType())
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart = MultipartBody.Part.createFormData(
                    "photo",
                    file.name,
                    requestImageFile
                )

                var lat: RequestBody? = null
                var lon: RequestBody? = null
                if (lokasi != null) {
                    lat = lokasi?.latitude.toString().toRequestBody("text/plain".toMediaType())
                    lon = lokasi?.longitude.toString().toRequestBody("text/plain".toMediaType())
                }

                // upload story
                viewModel.postStories(user.token, description, imageMultipart, lat, lon).observe(this) {
                    if (it != null) {
                        when (it) {
                            is ResultResponse.Loading -> {
                                binding.progressBar.visibility = View.VISIBLE
                            }
                            is ResultResponse.Success -> {
                                binding.progressBar.visibility = View.GONE
                                Helper.showToastLong(this, getString(R.string.upload_success))
                                finish()
                            }
                            is ResultResponse.Error -> {
                                binding.progressBar.visibility = View.GONE
                                AlertDialog.Builder(this).apply {
                                    setTitle(getString(R.string.information))
                                    setMessage(getString(R.string.upload_failed) + ", ${it.error}")
                                    setPositiveButton(getString(R.string.continue_reading)) { _, _ ->
                                        binding.progressBar.visibility = View.GONE
                                    }
                                    create()
                                    show()
                                }
                            }
                        }
                    }
                }
            }
            else -> {
                Helper.showToastShort(this@AddStroiesActivity, getString(R.string.no_attach_file))
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
