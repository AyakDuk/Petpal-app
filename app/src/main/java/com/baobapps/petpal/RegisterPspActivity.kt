package com.baobapps.petpal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class RegisterPspActivity : AppCompatActivity() {
    //Identify this Activity
    val TAG = "RegisterPspActivity" // Important for debugging actions

    //DATABASE CONNECTION
    // Call the root node
    val rootNode: FirebaseDatabase = FirebaseDatabase.getInstance()
    //Reference the table within the root node
    val reference: DatabaseReference = rootNode.getReference("pspApplications")

    override fun onCreate(savedInstanceState: Bundle?) {

        Log.d(TAG, "ONCREATE ACTIVITY CALLED") // Important for debugging actions

        super.onCreate(savedInstanceState)
        // Set this screen's UI
        setContentView(R.layout.activity_register_psp)

        //UI elements
        val signUpBtn: Button = findViewById(R.id.reg_btn) //Register BTN
        val locationSpinner: Spinner = findViewById(R.id.input_location) //Location Spinner
        val locationSpinnerErrorView: TextView =
            findViewById(R.id.locationSpinnerErrorTextView) // Location Spinner Error TextView
        val servicesSpinner: Spinner = findViewById(R.id.input_service) //Services Spinner
        val serviceSpinnerErrorView: TextView =
            findViewById(R.id.serviceSpinnerErrorTextView) // Service Spinner Error TextView

        // Hide the status bar
        @Suppress("DEPRECATION")
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )


        //Create the Array Adapters
        val locationsArray =
            resources.getStringArray(R.array.location_options) //Dropdown Menu: Locations
        val servicesArray =
            resources.getStringArray(R.array.service_options) //Dropdown menu: Services
        //Initialize the adapters
        val locationAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, locationsArray)
        val servicesAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, servicesArray)

        //Initialize the Dropdown Views
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        servicesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        locationSpinner.adapter = locationAdapter
        servicesSpinner.adapter = servicesAdapter

        // Set the initial selections
        locationSpinner.setSelection(0)
        servicesSpinner.setSelection(0)

        // Save the data into FireBase Database
        signUpBtn.setOnClickListener {
            // Retrieve the  values from the input elements
            val firstName =
                findViewById<TextInputLayout>(R.id.input_first_name).editText?.text.toString()
            val lastName =
                findViewById<TextInputLayout>(R.id.input_last_name).editText?.text.toString()
            val emailAddress =
                findViewById<TextInputLayout>(R.id.input_email).editText?.text.toString()
            val phoneNumber =
                findViewById<TextInputLayout>(R.id.input_phone).editText?.text.toString()
            val locationName = locationSpinner.selectedItem.toString()
            val serviceName = servicesSpinner.selectedItem.toString()

            //Validate the user input
            if (validateFirstName(firstName) && validateLastName(lastName) && validateEmail(emailAddress) && validatePhoneNo(phoneNumber) && validateSpinner(
                    locationSpinner,
                    locationSpinnerErrorView
                ) && validateSpinner(servicesSpinner, serviceSpinnerErrorView)
            ) {
                Log.d(TAG, "USER INPUT VALIDATED SUCCESSFULLY") // Important for debugging actions

                // Store the user input into the database using the phoneNo as the primary key only if they don't already exist
                fun storeUser(){
                    GlobalScope.launch(Dispatchers.IO) {
                        if (validateUser(phoneNumber)) {
                            // Store it as a PSP object
                            val psp = ServiceProviderHelperClass(
                                firstName,
                                lastName,
                                emailAddress,
                                phoneNumber,
                                locationName,
                                serviceName
                            )
                            reference.child(phoneNumber).setValue(psp)
                            Log.d(
                                TAG,
                                "NEW USER SAVED IN DATABASE"
                            ) // Important for debugging actions
                            //TODO: Report on signup
                            Log.d(
                                TAG,
                                "NEW SP APPLICANT SAVED IN DATABASE"
                            ) // Important for debugging actions
                            clearInputs()
                        }
                    }
                    showToast("Application submitted! Check email")
                }
                storeUser()


            }

        }

    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    //Function to clear fields
    private fun clearInputs() {
        val inputLayouts = listOf(
            R.id.input_first_name,
            R.id.input_last_name,
            R.id.input_email,
            R.id.input_phone
        )
        for (layoutId in inputLayouts) {
            findViewById<TextInputLayout>(layoutId).editText?.setText("")
        }
    }

    //Function to validate the First Name
    private fun validateFirstName(firstName: String): Boolean {
        //UI elements
        val textLayout: TextInputLayout = findViewById(R.id.input_first_name)
        if (firstName.isEmpty()) {
            textLayout.error = "Field cannot be empty"
            return false
        } else {
            textLayout.error = null
            textLayout.isErrorEnabled = false
            return true
        }
    }

    //Function to validate the Last Name
    private fun validateLastName(lastName: String): Boolean {
        //UI elements
        val textLayout: TextInputLayout = findViewById(R.id.input_last_name)
        if (lastName.isEmpty()) {
            textLayout.error = "Field cannot be empty"
            return false
        } else {
            textLayout.error = null
            textLayout.isErrorEnabled = false
            return true
        }
    }

    //Function to validate the email
    private fun validateEmail(email: String): Boolean {
        //UI elements
        val textLayout: TextInputLayout = findViewById(R.id.input_email)

        // Email standard
        val emailPattern = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}"
        if (email.isEmpty()) {
            textLayout.error = "Field cannot be empty"
            return false
        } else if (!email.matches(emailPattern.toRegex())) {
            textLayout.error = "Invalid email address"
            return false
        } else {
            textLayout.error = null
            textLayout.isErrorEnabled = false
            return true
        }
    }

    //Function to validate the phone number
    private fun validatePhoneNo(phone: String): Boolean {
        val textLayout: TextInputLayout = findViewById(R.id.input_phone)
        if (phone.isEmpty()) {
            textLayout.error = "Field cannot be empty"
            return false
        } else {
            textLayout.error = null
            textLayout.isErrorEnabled = false
            return true
        }
    }

    //Function to validate Spinner
    private fun validateSpinner(spinner: Spinner, errorTextView: TextView): Boolean {
        //Get the values from the input fields
        val selectedPosition = spinner.selectedItemPosition
        if (selectedPosition <= 0) {
            // No item selected or default item selected, show an error
            errorTextView.visibility = View.VISIBLE
            return false
        } else {
            // Valid selection, hide the error
            errorTextView.visibility = View.GONE
            return true
        }
    }

    private suspend fun validateUser(phone: String): Boolean {
        val checkUser: Query = reference.orderByChild("phoneNumber").equalTo(phone)

        return suspendCoroutine { continuation ->
            checkUser.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val errorView: TextView = findViewById(R.id.errorTextView)
                    if (dataSnapshot.exists()) {
                        Log.d(TAG, "APPLICANT ALREADY EXISTS")
                        errorView.visibility = View.VISIBLE
                        continuation.resume(false)
                    } else {
                        errorView.visibility = View.GONE
                        continuation.resume(true)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e(TAG, "Database query cancelled: ${databaseError.message}")
                    continuation.resume(false)
                }
            })
        }
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


