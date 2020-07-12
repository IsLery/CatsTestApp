package com.islery.catstestapp.presentation.adapters

import android.view.ViewParent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.islery.catstestapp.presentation.cats_list.CatsListFragment
import com.islery.catstestapp.presentation.favourites.FavouritesFragment
import java.lang.IllegalArgumentException

class ViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity){
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
     return when(position){
         0 -> CatsListFragment()
         1 -> FavouritesFragment()
         else -> throw IllegalArgumentException("Wrong fragment")
     }
    }


}