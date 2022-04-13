package com.example.citiesoftheworld.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.navigateUp
import com.example.citiesoftheworld.R
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Timber.d("inside onCreate o")

        Log.d("TIMBAA","inside onCreate o")

//        val navController: NavController = findNavController(R.id.nav_host_fragment)


    }

//    override fun onSupportNavigateUp(): Boolean {
//        return findNavController(R.id.nav_host_fragment).navigateUp(appBarConfiguration)
//                || super.onSupportNavigateUp()
//    }

}