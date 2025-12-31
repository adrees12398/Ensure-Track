package com.example.ensuretrackapplication

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ensuretrackapplication.databinding.ActivityRegistrationBinding
import com.google.firebase.auth.FirebaseAuth

class RegistrationActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegistrationBinding

    lateinit var firebaseAuth: FirebaseAuth
    lateinit var sessionClass: SessionClass


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        sessionClass = SessionClass(this)
        listener()
    }

    private fun AuthenticationSignUp() {
        val name = binding.name1.text.toString()
        val email = binding.email1.text.toString()
        val mobile = binding.mobile1.text.toString()
        val password = binding.password1.text.toString()
        if (TextUtils.isEmpty(name)){
            showToast("name is required")
        } else if(TextUtils.isEmpty(email)){
            showToast("email is required")
        }
        else if(TextUtils.isEmpty(mobile)){
            showToast("Mobile no is required")
        }
        else if (TextUtils.isEmpty(password)){
            showToast("password is required")
        }
        else if(TextUtils.isEmpty(name) && TextUtils.isEmpty(email) && TextUtils.isEmpty(mobile) && TextUtils.isEmpty(password)) {
            showToast("All filed are required ")
        }
        else{
            if (email.isNotEmpty() && password.isNotEmpty() && mobile.isNotEmpty() && name.isNotEmpty()){
                firebaseAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this) {task ->
                        if (task.isSuccessful){
                            startActivity(Intent(this,LoginActivity::class.java))
                            sessionClass.setUser("User")
                            sessionClass.setTheme(true)
                            showToast("Successfully Registration")
                            finish()

                        }
                    }
                    .addOnFailureListener {

                    }
            }

        }
        binding.name1.setText("")
        binding.email1.setText("")
        binding.mobile1.setText("")
        binding.password1.setText("")
    }

    fun listener(){
        binding.sign.setOnClickListener {
            AuthenticationSignUp()
        }
        binding.arrow.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
    fun showToast(message:String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}