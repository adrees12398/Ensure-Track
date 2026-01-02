package com.example.ensuretrackapplication
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.ensuretrackapplication.Fragments.AdminPaymentDashboardFragment
import com.example.ensuretrackapplication.Fragments.ClientFragment2
import com.example.ensuretrackapplication.Fragments.MyTaskFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlin.jvm.java
class ManageScreenActivity : AppCompatActivity() {
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var currentFragment: Fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_screen)
        bottomNav = findViewById(R.id.bottom_navigation)

        // Default fragment
        currentFragment = ClientFragment2()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, currentFragment)
            .commit()

        bottomNav.selectedItemId = R.id.clientFragment2

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.clientFragment2 -> switchFragment(ClientFragment2())
                R.id.myTaskFragment -> switchFragment(MyTaskFragment())
                R.id.adminPaymentDashboardFragment -> switchFragment(AdminPaymentDashboardFragment())
            // R.id.tab_home -> switchFragment(HomeFragment())
                // etc...
            }
            true
        }
    }
    private fun switchFragment(fragment: Fragment) {
        if (fragment::class.java != currentFragment::class.java) {
            currentFragment = fragment
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
        }
    }
}