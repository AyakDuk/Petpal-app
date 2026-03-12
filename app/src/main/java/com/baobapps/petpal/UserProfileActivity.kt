package com.baobapps.petpal

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity

class UserProfileActivity : ComponentActivity() {
    val TAG="UserProfileActivity" // important for debugging actions
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)// Declaring the View (user)
        Log.d(TAG, "ONCREATE ACTIVITY CALLED") // Important for debugging actions

        @Suppress("DEPRECATION")
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN) // Hide the status bar


    }

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


