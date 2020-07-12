package com.islery.catstestapp.presentation

import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope

abstract class CatsFragment: Fragment() {

    val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if(it.containsValue(false)){
                Toast.makeText(requireContext(), "Please grant needed permissions to download images", Toast.LENGTH_LONG).show()
            }
        }


    protected fun downloadImageToStorage(url: String){
       if (requireContext().checkWriteAndReadPermissions()){
           downloadImage(requireContext(),url,lifecycleScope)
       }else{
           requestPermissionLauncher.launch(arrayOf(
               android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
               android.Manifest.permission.READ_EXTERNAL_STORAGE
           ))
       }
    }
}