package com.example.week1project

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.week1project.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

// Set up the NavController with the BottomNavigationView
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.navigation_tab1_1, R.id.navigation_tab2_1, R.id.navigation_tab3)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.mobile_navigation, true) // 기존 BackStack 제거
            .build()

        navView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_tab1_1 -> {
                    navController.navigate(R.id.navigation_tab1_1, null, navOptions)
                    true
                }
                R.id.navigation_tab2_1 -> {
                    navController.navigate(R.id.navigation_tab2_1, null, navOptions)
                    true
                }
                R.id.navigation_tab3 -> {
                    navController.navigate(R.id.navigation_tab3, null, navOptions)
                    true
                }
                else -> false
            }
        }

    }
}