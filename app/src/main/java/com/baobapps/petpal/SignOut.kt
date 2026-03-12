package com.baobapps.petpal

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.auth.FirebaseAuth

class SignOutActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance() // Initialize FirebaseAuth instance

        setContent {
            SignOutScreen()
        }
    }

    @Composable
    fun SignOutScreen() {
        val context = LocalContext.current
        val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

        // Sign out the user
        auth.signOut()

        // Navigate to the login activity
        val intent = Intent(context, LoginActivity::class.java)
        startActivity(intent)
        finish() // Close the current activity to prevent going back to it

        // Display a message indicating successful sign out
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = "You have been signed out.")
        }

        // Handle back button press to prevent going back to the signed-out state
        backDispatcher?.addCallback {
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
            finish() // Close the current activity to prevent going back to it
        }
    }
}
