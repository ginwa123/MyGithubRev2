package com.m.ginwa.mygithubrev2.ui.detailuserfragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.m.ginwa.mygithubrev2.ui.detailuserfragment.biofragment.BioFragment
import com.m.ginwa.mygithubrev2.ui.detailuserfragment.followersfragment.FollowersFragment
import com.m.ginwa.mygithubrev2.ui.detailuserfragment.followingfragment.FollowingsFragment

class UserPager(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val login: String?
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> BioFragment.newInstance(login)
            1 -> FollowersFragment.newInstance(login)
            2 -> FollowingsFragment.newInstance(login)
            else -> Fragment()
        }
    }
}