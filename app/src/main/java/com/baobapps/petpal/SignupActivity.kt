package com.baobapps.petpal

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Pair
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
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

class SignupActivity : ComponentActivity() {
    //Identify this Activity
    val TAG = "SignupActivity" // Important for debugging actions

    // DATABASE CONNECTION
    // Call the root node
    val rootNode: FirebaseDatabase = FirebaseDatabase.getInstance()
    //Call a table or reference within the root node
    val reference: DatabaseReference = rootNode.getReference("petOwners")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Declaring this screen's UI
        setContentView(R.layout.activity_signup)
        Log.d(TAG, "ONCREATE ACTIVITY CALLED") // Important for debugging actions

        // Hide the status bar
        @Suppress("DEPRECATION")
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        //UI elements
        val image: ImageView = findViewById(R.id.logo_image) //Logo
        val logo: TextView = findViewById(R.id.logo_name) //Text
        val slogan: TextView = findViewById(R.id.slogan_name) //Text
        val signUpBtn: Button = findViewById(R.id.reg_btn) //Signup BTN
        val loginActivityBtn: Button = findViewById(R.id.reg_login_btn) // ToLogin Page BTN

        // Call the Login Activity
        loginActivityBtn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            val pairs = arrayOf<Pair<View, String>>(
                Pair(image, "app_image"),
                Pair(logo, "app_image_text"),
                Pair(slogan, "app_image_slogan")
            )
            val options = ActivityOptions.makeSceneTransitionAnimation(this, *pairs)
            startActivity(intent, options.toBundle())
        }

        //SignUp. Save the data in FireBase then go to specified activity
        signUpBtn.setOnClickListener {
            // Get the text from the input fields
            val firstName =
                findViewById<TextInputLayout>(R.id.input_first_name).editText?.text.toString()
            val lastName = findViewById<TextInputLayout>(R.id.input_last_name).editText?.text.toString()
            val username = findViewById<TextInputLayout>(R.id.input_username).editText?.text.toString()
            val email = findViewById<TextInputLayout>(R.id.input_email).editText?.text.toString()
            val phoneNo = findViewById<TextInputLayout>(R.id.input_phone).editText?.text.toString()
            val password = findViewById<TextInputLayout>(R.id.input_pswd).editText?.text.toString()
            val password2 = findViewById<TextInputLayout>(R.id.input_pswd2).editText?.text.toString()

            if (validateFirstName(firstName) && validateLastName(lastName) && validateUserName(username) && validateEmail(email) && validatePhoneNo(phoneNo) && validatePassword(password, password2)) {
                Log.d(TAG, "USER INPUT VALIDATED SUCCESSFULLY") // Important for debugging actions

                //Validate user only if they don't already exist
                //TODO: Report on signup
                fun signUp(){
                    GlobalScope.launch(Dispatchers.IO) {
                        if (validateUser(phoneNo)) {
                            val user = UserHelperClass(
                                firstName,
                                lastName,
                                username,
                                email,
                                phoneNo,
                                password
                            )
                            reference.child(phoneNo).setValue(user)
                            Log.d(
                                TAG,
                                "NEW USER SAVED IN DATABASE"
                            ) // Important for debugging actions
                        }
                    }
                }
                signUp()

            }

            val intent = Intent(this@SignupActivity, LoginActivity::class.java)
            startActivity(intent)
            showToast("Signup Successful!")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    //Function to validate the First Name
    private fun validateFirstName(firstName: String):Boolean {
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
        val textLayout: TextInputLayout = findViewById(R.id.input_last_name) //Input Field

        if (lastName.isEmpty()) {
            textLayout.error = "Field cannot be empty"
            return false
        } else {
            textLayout.error = null
            textLayout.isErrorEnabled = false
            return true
        }

    }

    //Function to validate the  Username
    private fun validateUserName(userName: String): Boolean {
        //UI elements
        val textLayout: TextInputLayout = findViewById(R.id.input_username)
        // No Whitespaces allowed
        val noWhiteSpace = "(?=\\S+$)"

        if (userName.isEmpty()) {
            textLayout.error = "Field cannot be empty"
            return false
        } else if (userName.length >= 15) {
            textLayout.error = "Username is too long"
            return false
        } else if (userName.matches(noWhiteSpace.toRegex())) {
            textLayout.error = "White Spaces are not allowed"
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
        // Email pattern standard
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


    //Function to validate the password
    private fun validatePassword(password1: String, password2: String): Boolean {
        //UI elements
        val textLayout1: TextInputLayout = findViewById(R.id.input_pswd)
        val textLayout2: TextInputLayout = findViewById(R.id.input_pswd2)

        // Password pattern standard
        val passwordVal = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$"

        if (password1.isEmpty()) {
            textLayout1.error = "Field cannot be empty"
            return false
        } else if (password2.isEmpty()) {
            textLayout2.error = "Field cannot be empty"
            return false
        } else if (!password1.matches(passwordVal.toRegex())) {
            textLayout1.error = "Password is too weak"
            return false
        } else if (password1 != password2) {
            textLayout1.error = "Passwords do not match"
            textLayout2.error = "Passwords do not match"
            return false
        }
        else {
            textLayout1.error = null
            textLayout2.error = null
            textLayout1.isErrorEnabled = false
            textLayout2.isErrorEnabled = false
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
                        Log.d(TAG, "USER ALREADY EXISTS")
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