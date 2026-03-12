package com.baobapps.petpal

import android.app.AlertDialog
import android.content.Intent
import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener

class SearchActivity : ComponentActivity() {

    val TAG = "Search" // Important for debugging actions

    // Declare a private property to hold an instance of FusedLocationProviderClient.
    // FusedLocationProviderClient is used to interact with the fused location provider
    // and obtain the user's last known location or request location updates.
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var providersListView: ListView
    private lateinit var filterSpinner: Spinner

    private val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    private val locationPermissionCode = 42

    override fun onCreate(savedInstanceState: Bundle?) {

        Log.d(TAG, "ONCREATE ACTIVITY CALLED") // Important for debugging actions
        super.onCreate(savedInstanceState)
        //Set this screen's UI
        setContentView(R.layout.activity_search)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        providersListView = findViewById(R.id.providersListView)
        filterSpinner = findViewById(R.id.filterSpinner)


        val filterOptions = resources.getStringArray(R.array.filter_options)
        val filterAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, filterOptions)
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        filterSpinner.adapter = filterAdapter

        val providers = resources.getStringArray(R.array.provider_options)
        val providerAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, providers)
        providersListView.adapter = providerAdapter

        // Hide the status bar
        @Suppress("DEPRECATION")
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        requestPermission()

        val findProvidersButton: Button = findViewById(R.id.findProvidersButton)
        findProvidersButton.setOnClickListener {
            getLastKnownLocation()
        }


    }

    //Request permission using Dexter library
    private fun requestPermission() {
        val permissionListener = object : PermissionListener {
            //Permision granted
            override fun onPermissionGranted(response: PermissionGrantedResponse) {
                Log.d(TAG, "PERMISSION GRANTED") // Important for debugging actions
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(this@SearchActivity)
                Toast.makeText(this@SearchActivity, "Permission granted", Toast.LENGTH_SHORT).show()
            }

            // Permission denied. Go to settings
            override fun onPermissionDenied(response: PermissionDeniedResponse) {
                Log.d(TAG, "PERMISSION DENIED") // Important for debugging actions
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val packageName = packageName
                val uri = Uri.fromParts("package", packageName, "")
                intent.data = uri
                startActivity(intent)
            }

            // Show the permission explanation dialog when required
            override fun onPermissionRationaleShouldBeShown(
                permission: PermissionRequest,
                token: PermissionToken
            ) {
                showPermissionExplanationDialog(token)
                Log.d(TAG, "PERMISSION EXPLANATION DIALOGUE ACTIVE") // Important for debugging actions
            }

        }

        Dexter.withContext(this)
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(permissionListener)
            .check()
    }
    // Permission Explanation Dialogue Box
    private fun showPermissionExplanationDialog(token: PermissionToken) {
    val dialogBuilder = AlertDialog.Builder(this)
    dialogBuilder.setTitle("Location Required")
    dialogBuilder.setMessage("We need this permission to perform the search in the app.")
    dialogBuilder.setPositiveButton("OK") { dialog, _ ->
        token.continuePermissionRequest()
        dialog.dismiss()
    }
        // Handle the case when the user clicks on Cancel, you may choose to close the app or show some message
    dialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
        dialog.dismiss()
    }

    val dialog = dialogBuilder.create()
    dialog.show()
}

    //Get the user's current location
    private fun getLastKnownLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
        {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    location?.let {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    // TODO: Use latitude and longitude to fetch nearby providers and update the list
                    updateProvidersList( filterSpinner.selectedItem.toString())
                }
            }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
        }

    private fun updateProvidersList(filter: String) {
        // Update the list of providers based on the filter and location
    }

    /*
     *
    Important for Debugging */

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "ONSTART ACTIVITY CALLED") // Important for debugging actions

    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "ONRESUME ACTIVITY CALLED") // Important for debugging actions

    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "ONSTOP ACTIVITY CALLED") // Important for debugging actions

    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "ONRESTART ACTIVITY CALLED") // Important for debugging actions

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "ONDESTROY ACTIVITY CALLED") // Important for debugging actions
    }
}



