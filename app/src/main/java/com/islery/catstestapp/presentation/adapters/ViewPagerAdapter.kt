package com.islery.catstestapp.presentation.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.islery.catstestapp.presentation.cats_list.CatsListFragment
import com.islery.catstestapp.presentation.favourites.FavouritesFragment

class ViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CatsListFragment()
            1 -> FavouritesFragment()
            else -> throw IllegalArgumentException("Wrong fragment")
        }
    }


}