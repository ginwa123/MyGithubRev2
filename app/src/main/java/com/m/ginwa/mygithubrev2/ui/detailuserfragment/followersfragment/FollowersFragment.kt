package com.m.ginwa.mygithubrev2.ui.detailuserfragment.followersfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.m.ginwa.core.data.Result
import com.m.ginwa.core.domain.model.Follower
import com.m.ginwa.core.utils.showToast
import com.m.ginwa.mygithubrev2.R
import com.m.ginwa.mygithubrev2.databinding.FragmentFollowersBinding
import com.m.ginwa.mygithubrev2.ui.ActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FollowersFragment : Fragment() {
    private var login: String? = null
    private lateinit var adapter: FollowersAdapter
    private var _binding: FragmentFollowersBinding? = null
    private val binding get() = _binding
    private val fragmentVm: FollowersViewModel by viewModels()
    private val activityVm: ActivityViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        login = arguments?.getString("login")
        lifecycleScope.launch {
            fragmentVm.getFollowers(login ?: "ginwa")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFollowersBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        setFollowersDatListener()
        binding?.swipeRefreshLayout?.setOnRefreshListener {
            lifecycleScope.launch {
                fragmentVm.getFollowers(login ?: "ginwa")
                setFollowersDatListener()
            }
        }
    }

    private fun setFollowersDatListener() {
        lifecycleScope.launch {
            fragmentVm.followers?.removeObservers(viewLifecycleOwner)
            fragmentVm.followers?.observe(viewLifecycleOwner, { result ->
                when (result) {
                    is Result.Success -> setUserFollowers(result.data)
                    is Result.Error -> setError(result.exception.message)
                    Result.Loading -> setLoading()
                }
            })
        }
    }

    private fun setError(message: String?) {
        requireContext().showToast(message)
        setCompleted()
    }

    private fun setCompleted() {
        activityVm.progressbarListener.value = false
        binding?.swipeRefreshLayout?.isRefreshing = false
        if (fragmentVm.followersDataSets.isEmpty()) {
            binding?.tvNoticeContainer?.visibility = View.VISIBLE
        } else {
            binding?.tvNoticeContainer?.visibility = View.GONE
        }
    }

    private fun setLoading() {
        binding?.swipeRefreshLayout?.isRefreshing = true
        activityVm.progressbarListener.value = true
    }

    private fun setUserFollowers(dataSets: List<Follower>) {
        adapter.updateDataSets(dataSets)
        fragmentVm.followersDataSets.clear()
        fragmentVm.followersDataSets.addAll(dataSets)
        setCompleted()
    }


    private fun setAdapter() {
        binding?.apply {
            adapter = FollowersAdapter(requireContext(), fragmentVm.followersDataSets)
            adapter.onClick = {
                findNavController().navigate(R.id.action_detailUserFragment_self, it)
            }
            recyclerView.apply {
                adapter = this@FollowersFragment.adapter
                layoutManager = LinearLayoutManager(requireContext())
                isNestedScrollingEnabled = false
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        fun newInstance(login: String?): Fragment {
            val fragment = FollowersFragment()
            val bundle = bundleOf("login" to login)
            fragment.arguments = bundle
            return fragment
        }
    }
}