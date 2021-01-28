package com.m.ginwa.mygithubrev2.ui.detailuser

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
import timber.log.Timber

@AndroidEntryPoint
class DetailUserFragment : Fragment() {

    private var isFastAnimate: Boolean = false
    private var isStartOnce: Boolean = true
    private var mediator: TabLayoutMediator? = null
    private var login: String? = null
    private lateinit var viewPagerAdapter: UserPager
    private var _binding: FragmentDetailUserBinding? = null
    private val binding get() = _binding
    private val ViewPager2.recyclerView: RecyclerView
        get() {
            return this[0] as RecyclerView
        }
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
            viewPagerAdapter = UserPager(childFragmentManager, viewLifecycleOwner.lifecycle, login)

            viewPager.apply {
                recyclerView.enforceSingleScrollDirection()
                adapter = viewPagerAdapter
                offscreenPageLimit = 3
                registerOnPageChangeCallback(viewpagerCallback)
            }
            mediator = TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                when (position) {
                    0 -> tab.text = getString(R.string.bio)
                    1 -> tab.text = getString(R.string.followers)
                    2 -> tab.text = getString(R.string.following)
                }
            }
            mediator?.attach()


        }
    }


    private val viewpagerCallback: ViewPager2.OnPageChangeCallback =
        object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (isStartOnce) {
                    isStartOnce = false
                } else {
                    Timber.d("viewpager at $position")
                    if (position != 0) {
                        activityVm.isExpandToolbarListener.value =
                            mapOf("isExpand" to false, "isAnimate" to true)
                    } else {
                        if (isFastAnimate) {
                            activityVm.isExpandToolbarListener.value =
                                mapOf("isExpand" to true, "isAnimate" to true)
                        } else {
                            activityVm.isExpandToolbarListener.value =
                                mapOf(
                                    "isExpand" to true,
                                    "isAnimate" to true,
                                    "isFastAnimate" to false
                                )
                            isFastAnimate = true
                        }
                    }
                }
            }

        }


    override fun onDestroyView() {
        super.onDestroyView()
        binding?.apply {
            viewPager.adapter = null
            viewPager.unregisterOnPageChangeCallback(viewpagerCallback)
        }
        mediator?.detach()
        mediator = null
        _binding = null
    }

}