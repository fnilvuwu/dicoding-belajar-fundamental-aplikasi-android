package com.example.bfaa.ui.detail

import android.content.Context
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.bfaa.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class SectionPagerAdapter(
    private val mCtx: Context,
    fragmentActivity: FragmentActivity,
    private val tabLayout: TabLayout,
    private val viewPager: ViewPager2,
    data: Bundle
) : FragmentStateAdapter(fragmentActivity) {

    private var fragmentBundle: Bundle

    init {
        fragmentBundle = data
    }

    @StringRes
    private val TAB_TITLES = intArrayOf(R.string.tab_1, R.string.tab_2)

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                val followersFragment = FollowersFragment()
                followersFragment.arguments = fragmentBundle // Pass the bundle to the fragment
                followersFragment
            }
            1 -> {
                val followingFragment = FollowingFragment()
                followingFragment.arguments = fragmentBundle // Pass the bundle to the fragment
                followingFragment
            }
            else -> throw IllegalArgumentException("Invalid position $position")
        }
    }

    fun setupTabs() {
        viewPager.adapter = this
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = mCtx.resources.getString(TAB_TITLES[position])
        }.attach()
    }
}
