package com.example.ensuretrackapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavHost
import androidx.navigation.ui.setupWithNavController
import com.example.ensuretrackapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val navController by lazy {
        val navHost = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHost
        navHost.navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bottomNavigation()
    }

    private fun bottomNavigation() {
        binding.nav.setupWithNavController(navController)
    }
}