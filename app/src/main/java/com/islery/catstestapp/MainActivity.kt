package com.islery.catstestapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.islery.catstestapp.databinding.ActivityMainBinding
import com.islery.catstestapp.presentation.adapters.ViewPagerAdapter

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.viewPager.adapter =
            ViewPagerAdapter(this)

        //using ViewPager 2 to slide between fragments
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.all_cats)
                1 -> tab.text = getString(R.string.fav_cats)
            }
        }.attach()
    }
}