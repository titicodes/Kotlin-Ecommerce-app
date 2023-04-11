package com.example.ecomfire.utils

import androidx.fragment.app.Fragment
import com.example.ecomfire.activities.MainActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

fun Fragment.hideBottomNavigationView(){
    val bottomNavigationView =
        (activity as MainActivity).findViewById<BottomNavigationView>(
            //com.example.kelineyt.R.id.bottomNavigation
        com.example.ecomfire.R.id.bottomNavigation
        )
    bottomNavigationView.visibility = android.view.View.GONE
}

fun Fragment.showBottomNavigationView(){
    val bottomNavigationView =
        (activity as MainActivity).findViewById<BottomNavigationView>(
            com.example.ecomfire.R.id.bottomNavigation
        )
    bottomNavigationView.visibility = android.view.View.VISIBLE
}