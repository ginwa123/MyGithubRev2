package com.m.ginwa.mygithubrev2.ui.detailuserfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.m.ginwa.core.utils.enforceSingleScrollDirection
import com.m.ginwa.mygithubrev2.R
import com.m.ginwa.mygithubrev2.databinding.FragmentDetailUserBinding
import com.m.ginwa.mygithubrev2.ui.ActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailUserFragment : Fragment() {
    private var login: String? = null
    private lateinit var viewPagerAdapter: UserPager
    private var _binding: FragmentDetailUserBinding? = null
    private val binding get() = _binding
    private val ViewPager2.recyclerView: RecyclerView
        get() {
            return this[0] as RecyclerView
        }
    private var startOnce = true
    private val activityVm: ActivityViewModel by activityViewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailUserBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        login = arguments?.getString("login")
        setViewPager()
    }

    private fun setViewPager() {
        binding?.apply {
            viewPagerAdapter = UserPager(childFragmentManager, lifecycle, login)
            viewPager.recyclerView.enforceSingleScrollDirection()
            viewPager.apply {
                adapter = viewPagerAdapter
                offscreenPageLimit = 3
            }
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                when (position) {
                    0 -> tab.text = getString(R.string.bio)
                    1 -> tab.text = getString(R.string.followers)
                    2 -> tab.text = getString(R.string.following)
                }
            }.attach()

            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    if (position == 0) {
                        if (startOnce) {
                            activityVm.isExpandToolbar.value =
                                mapOf("isExpand" to true, "isAnimate" to false)
                            startOnce = false
                        } else {
                            activityVm.isExpandToolbar.value =
                                mapOf("isExpand" to true, "isAnimate" to true)
                        }

                    } else {
                        activityVm.isExpandToolbar.value =
                            mapOf("isExpand" to false, "isAnimate" to true)
                    }
                }
            })
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}