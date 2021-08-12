package com.circleappsstudio.blogapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.circleappsstudio.blogapp.R
import com.circleappsstudio.blogapp.core.hide
import com.circleappsstudio.blogapp.core.show
import com.circleappsstudio.blogapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

        NavigationUI.setupActionBarWithNavController(this, navController)

        observeDestinationChange()

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    private fun observeDestinationChange() {

        navController.addOnDestinationChangedListener { controller, destination, arguments ->

            when(destination.id) {

                R.id.loginFragment -> {
                    binding.bottomNavigationView.hide()
                }

                R.id.registerFragment -> {
                    binding.bottomNavigationView.hide()
                }

                R.id.setupProfileFragment -> {
                    binding.bottomNavigationView.hide()
                }

                else -> {
                    binding.bottomNavigationView.show()
                }

            }

        }

    }

}