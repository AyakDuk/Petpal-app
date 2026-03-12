package com.baobapps.petpal

import android.app.ActivityOptions
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Pair
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class LoginActivity : ComponentActivity() {
    //Identify this Activity
    val TAG="LoginActivity" // Important for debugging actions

    //DATABASE CONNECTION
    // Call the root node
    val rootNode: FirebaseDatabase = FirebaseDatabase.getInstance()
    // Call a table or reference within the root node
    val reference: DatabaseReference = rootNode.getReference("petOwners")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Set this screen's UI
        setContentView(R.layout.activity_login)
        Log.d(TAG, "ONCREATE ACTIVITY CALLED") // Important for debugging actions

        // Hide the status bar
        @Suppress("DEPRECATION")
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)

        // UI Elements
        val image :ImageView = findViewById(R.id.logo_image) //Logo
        val logo : TextView = findViewById(R.id.logo_name) //Text 1
        val slogan : TextView = findViewById(R.id.slogan_name) //Text 2
        val callSignUpBtn : Button = findViewById(R.id.signup_screen) //Signup BTN
        val loginBtn : Button = findViewById(R.id.login_btn) //Login BTN
        val pspSignupBtn : Button = findViewById(R.id.signup_psp_screen) // PSP signup BTN
        val forgotPassBtn : Button = findViewById(R.id.forgot_pass_btn) // Forgot Password  BTN
        val usernameLayout : TextInputLayout = findViewById(R.id.input_userUsername) //InputField
        val passwordLayout : TextInputLayout = findViewById(R.id.input_pswd) //InputField


        // Call the signup page
        callSignUpBtn.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)

            val pairs = arrayOf<Pair<View, String>>(
                Pair(image, "app_image"),
                Pair(logo, "app_image_text"),
                Pair(slogan, "app_image_slogan")
            )

            val options = ActivityOptions.makeSceneTransitionAnimation(this, *pairs)
            startActivity(intent, options.toBundle())
        }

        // Call the PSP signup page
        pspSignupBtn.setOnClickListener {
            val intent = Intent(this, RegisterPspActivity::class.java)

            val pairs = arrayOf<Pair<View, String>>(
                Pair(image, "app_image"),
                Pair(logo, "app_image_text"),
                Pair(slogan, "app_image_slogan")
            )

            val options = ActivityOptions.makeSceneTransitionAnimation(this, *pairs)
            startActivity(intent, options.toBundle())
        }

        //Login action
        loginBtn.setOnClickListener {
            val username = usernameLayout.editText?.text.toString()
            val pswd = passwordLayout.editText?.text.toString()
            if(validateUserName(username) && validatePassword(pswd)) {
                //TODO: Track this login event
                Log.d(TAG, "USER INPUT VALIDATION SUCCESSFUL") // Important for debugging actions
                isUser(username, pswd)
            }
            //Proceed to Home page.
            Log.d(TAG, "USER AUTHENTICATION SUCCESS") // Important for debugging actions
            val intent = Intent(this@LoginActivity, SearchActivity::class.java)
            startActivity(intent)
        }



    }
    override fun onBackPressed() {
        showExitDialog()
    }
    private fun showExitDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Exit or Sign Out")
            .setMessage("Do you want to exit the app or sign out?")
            .setPositiveButton("Exit", DialogInterface.OnClickListener { dialog, which ->
                finishAffinity() // Exit the app
            })
            .setNegativeButton("Sign Out", DialogInterface.OnClickListener { dialog, which ->
                // TODO: Implement sign-out logic here
            })
            .setCancelable(true)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
    //Function to validate the username
    private fun validateUserName(username: String): Boolean {
        //UI elements
        val textLayout : TextInputLayout = findViewById(R.id.input_userUsername) //InputField
        if (username.isEmpty()) {
            textLayout.error = "Field cannot be empty"
            return false
        } else {
            textLayout.error = null
            textLayout.isErrorEnabled = false
            return true
        }
    }

    //Function to validate the password
    private fun validatePassword(password: String): Boolean {
        //UI elements
        val errorView: TextView = findViewById(R.id.errorTextView) //TextView
        if (password.isEmpty()) {
            errorView.visibility = View.VISIBLE
            return false
        } else {
            errorView.visibility = View.GONE
            return true
        }
    }

    //Function to validate the user
    private fun isUser(username: String, password: String) {
        //UI elements
        val usernameTextLayout : TextInputLayout = findViewById(R.id.input_userUsername) //InputField
        val errorView: TextView = findViewById(R.id.errorTextView) //TextView

        //Query to find user
        val checkUser: Query = reference.orderByChild("userName").equalTo(username)
        //Function Login()
        checkUser.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.d(TAG, "USER EXISTS") // Important for debugging actions
                    usernameTextLayout.error = null
                    usernameTextLayout.isErrorEnabled = false

                    //Check if password is correct
                    //Query
                    val checkPassword: Query = reference.orderByChild("password").equalTo(password)

                    checkPassword.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.exists()) {
                                errorView.visibility = View.GONE
                            }
                            // Wrong password
                            else {
                                errorView.visibility = View.VISIBLE

                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            // TODO: Handle cancellation
                        }
                    })
                    //Username does not exist
                } else {
                    usernameTextLayout.error = "No such User exist"
                    usernameTextLayout.requestFocus()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // TODO: Handle cancellation
            }
        })
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