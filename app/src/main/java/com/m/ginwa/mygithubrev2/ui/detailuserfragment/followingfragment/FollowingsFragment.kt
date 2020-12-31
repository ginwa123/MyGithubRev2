package com.m.ginwa.mygithubrev2.ui.detailuserfragment.followingfragment

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
import com.m.ginwa.core.domain.model.Following
import com.m.ginwa.core.utils.showToast
import com.m.ginwa.mygithubrev2.R
import com.m.ginwa.mygithubrev2.databinding.FragmentFollowingsBinding
import com.m.ginwa.mygithubrev2.ui.ActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FollowingsFragment : Fragment() {
    private var login: String? = null
    private lateinit var adapter: FollowingsAdapter
    private var _binding: FragmentFollowingsBinding? = null
    private val binding get() = _binding
    private val fragmentVm: FollowingsViewModel by viewModels()
    private val activityVm: ActivityViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        login = arguments?.getString("login")
        lifecycleScope.launch {
            fragmentVm.getFollowings(login ?: "ginwa")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFollowingsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        setFollowingsDataListener()
        binding?.swipeRefreshLayout?.setOnRefreshListener {
            lifecycleScope.launch {
                fragmentVm.getFollowings(login ?: "ginwa")
                setFollowingsDataListener()
            }
        }
    }

    private fun setFollowingsDataListener() {
        lifecycleScope.launch {
            fragmentVm.followings?.removeObservers(viewLifecycleOwner)
            fragmentVm.followings?.observe(viewLifecycleOwner, { result ->
                when (result) {
                    is Result.Success -> setUserFollowings(result.data)
                    is Result.Error -> setError(result.exception.message)
                    Result.Loading -> setLoading()
                }
            })
        }
    }

    private fun setError(message: String?) {
        requireContext().showToast(message)
    }

    private fun setCompleted() {
        activityVm.progressbarListener.value = false
        binding?.swipeRefreshLayout?.isRefreshing = false
        if (fragmentVm.followingsDataSets.isEmpty()) {
            binding?.tvNoticeContainer?.visibility = View.VISIBLE
        } else {
            binding?.tvNoticeContainer?.visibility = View.GONE
        }
    }

    private fun setLoading() {
        binding?.swipeRefreshLayout?.isRefreshing = true
        activityVm.progressbarListener.value = true
    }

    private fun setUserFollowings(dataSets: List<Following>) {
        adapter.updateDataSets(dataSets)
        fragmentVm.followingsDataSets.clear()
        fragmentVm.followingsDataSets.addAll(dataSets)
        setCompleted()
    }


    private fun setAdapter() {
        binding?.apply {
            adapter = FollowingsAdapter(requireContext(), fragmentVm.followingsDataSets)
            adapter.onClick = {
                findNavController().navigate(R.id.action_detailUserFragment_self, it)
            }
            recyclerView.apply {
                adapter = this@FollowingsFragment.adapter
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
            val fragment = FollowingsFragment()
            val bundle = bundleOf("login" to login)
            fragment.arguments = bundle
            return fragment
        }
    }
}