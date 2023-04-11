package com.example.ecomfire.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class HomeViewpagerAdapter(
    private val fragments: ArrayList<Any>,
    fm: FragmentManager,
    lifecycle: Lifecycle
): FragmentStateAdapter(fm,lifecycle) {
    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

}