package com.example.ensuretrackapplication

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ensuretrackapplication.databinding.ActivityLoginBinding
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import java.util.Arrays

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 1001
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var callbackManager: CallbackManager   // 🔹 Facebook callbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        callbackManager = CallbackManager.Factory.create()  // ✅ Facebook init

        // 🔹 Facebook Login Button
        binding.loginButton.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(
                this,
                Arrays.asList("email", "public_profile")
            )
        }

        // 🔹 Facebook Callback Register
        LoginManager.getInstance().registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    val accessToken = result.accessToken
                    handleFacebookAccessToken(accessToken.token)   // ✅ Firebase ke sath connect
                }

                override fun onCancel() {
                    showToast("Facebook Login Cancelled!")
                }

                override fun onError(exception: FacebookException) {
                    showToast("Facebook Error: ${exception.message}")
                }
            }
        )

        // 🔹 Google SignIn Options
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) // Firebase console se milta hai
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Button listeners
        listener()

        // 🔹 Google button click
        binding.google.setOnClickListener {
            signInWithGoogle()
        }
    }

    // ✅ Ab sirf ek hi onActivityResult rakho (Facebook + Google dono handle karega)
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Facebook ka result handle karo
        callbackManager.onActivityResult(requestCode, resultCode, data)

        // Google ka result handle karo
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account: GoogleSignInAccount = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                showToast("Google Sign In Failed: ${e.message}")
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    showToast("Welcome ${user?.displayName}")
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    showToast("Authentication Failed")
                }
            }
    }

    // 🔹 Facebook Token ko Firebase ke sath connect karna
    private fun handleFacebookAccessToken(token: String) {
        val credential = FacebookAuthProvider.getCredential(token)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    showToast("Welcome ${user?.displayName}")
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    showToast("Facebook Auth Failed: ${task.exception?.message}")
                }
            }
    }

    private fun listener() {
        binding.IDoBtn.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }
        binding.SignUp.setOnClickListener {
            match()
        }
    }

    private fun match() {
        val email = binding.emaillogin.text.toString()
        val password = binding.etPass.text.toString()
        if (TextUtils.isEmpty(email)) {
            showToast("Email is required")
        } else if (TextUtils.isEmpty(password)) {
            showToast("Password is required")
        } else {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        startActivity(Intent(this, MainActivity::class.java))
                        showToast("Login Successfully")
                        finish()
                    }
                }
                .addOnFailureListener {
                    showToast("Login Failed: ${it.message}")
                }
        }
        binding.emaillogin.setText("")
        binding.etPass.setText("")
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
