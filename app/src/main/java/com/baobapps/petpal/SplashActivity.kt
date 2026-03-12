package com.baobapps.petpal

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Pair
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.google.firebase.analytics.FirebaseAnalytics


class SplashActivity : ComponentActivity() {
    //Identify this Activity
    val TAG="SplashScreen" // Important for debugging actions
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Declare this screen's UI
        setContentView(R.layout.activity_splash)
        Log.d(TAG, "ONCREATE ACTIVITY CALLED") // Important for debugging actions

        // Hide the status bar
        @Suppress("DEPRECATION")
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)


        // Add the animations using variables
        val topAnim=AnimationUtils.loadAnimation(this, R.anim.top_animation)
        val bottomAnim=AnimationUtils.loadAnimation(this, R.anim.bottom_animation)
        val image :ImageView = findViewById(R.id.imageView)
        val logo : TextView = findViewById(R.id.textView)
        val slogan :TextView = findViewById(R.id.textView2)

        // Apply the animations
        image.startAnimation(topAnim)
        logo.startAnimation(bottomAnim)
        slogan.startAnimation(bottomAnim)

        // Move to the specified activity after this
        @Suppress("DEPRECATION")
        android.os.Handler().postDelayed(
            {
                val intent = Intent(this, LoginActivity::class.java)

                val pairs = arrayOf<Pair<View, String>>(
                    Pair(image, "app_image"),
                    Pair(logo, "app_image_text"),
                    Pair(slogan, "app_image_slogan")
                )

                val options = ActivityOptions.makeSceneTransitionAnimation(this, *pairs)
                startActivity(intent, options.toBundle())

            },
            5000
        )

    }

    /*
     *
    Important for Debugging */

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "ONSTART ACTIVITY CALLED") // important for debugging actions

    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "ONRESUME ACTIVITY CALLED") // important for debugging actions

    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "ONSTOP ACTIVITY CALLED") // important for debugging actions

    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "ONRESTART ACTIVITY CALLED") // important for debugging actions

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "ONDESTROY ACTIVITY CALLED") // important for debugging actions

    }
}


