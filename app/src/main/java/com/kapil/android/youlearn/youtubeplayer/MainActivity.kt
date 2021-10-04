package com.kapil.android.youlearn.youtubeplayer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kapil.android.youlearn.R

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            bottomNavigationView = findViewById(R.id.bottomNavigationView)
            navController = findNavController(R.id.fragmentContainerView)
            val appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.fragment_playList,
                    R.id.fragment_profile,
                    R.id.fragment_readingList
                )
            )

            bottomNavigationView.setupWithNavController(navController)
    }

}