package com.example.ensuretrackapplication

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.facebook.AccessToken
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
    private lateinit var sessionClass: SessionClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        sessionClass = SessionClass(this)

        Handler(Looper.getMainLooper()).postDelayed({
            checkLoginStatus()
        }, 2000)
    }

    private fun checkLoginStatus() {
        // 🔹 Firebase user check
        val firebaseUser = FirebaseAuth.getInstance().currentUser

        // 🔹 Facebook user check
        val fbAccessToken = AccessToken.getCurrentAccessToken()
        val fbLoggedIn = fbAccessToken != null && !fbAccessToken.isExpired

        when {
            firebaseUser != null -> {
                // Agar Google/Email se login hai
                startActivity(Intent(this, ManageScreenActivity::class.java))
            }
            fbLoggedIn -> {
                // Agar Facebook se login hai
                startActivity(Intent(this, ManageScreenActivity::class.java))
            }
            sessionClass.getTheme() && sessionClass.getUser().equals("User", true) -> {
                // Agar tumhari SessionClass ke hisaab se login mila
                startActivity(Intent(this, ManageScreenActivity::class.java))
            }
            else -> {
                // Agar koi bhi login nahi hai
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }
        finish()
    }
}
