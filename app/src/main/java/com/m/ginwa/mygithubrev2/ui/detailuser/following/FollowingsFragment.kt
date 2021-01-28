package com.m.ginwa.mygithubrev2.ui.detailuser.following

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.m.ginwa.core.data.Result
import com.m.ginwa.core.domain.model.Following
import com.m.ginwa.core.utils.addDividerLine
import com.m.ginwa.core.utils.showToast
import com.m.ginwa.mygithubrev2.R
import com.m.ginwa.mygithubrev2.databinding.FragmentFollowingsBinding
import com.m.ginwa.mygithubrev2.ui.ActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FollowingsFragment : Fragment() {
    private var login: String? = null
    private var adapter: FollowingsAdapter? = null
    private var _binding: FragmentFollowingsBinding? = null
    private val binding get() = _binding
    private val fragmentVm: FollowingsViewModel by viewModels()
    private val activityVm: ActivityViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        login = arguments?.getString("login")
        fragmentVm.login = login ?: "ginwa"
        fragmentVm.getFollowings(login ?: "ginwa")
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
        setRecyclerView()
        setFollowingsDataListener()
        binding?.swipeRefreshLayout?.setOnRefreshListener {
            fragmentVm.getFollowings(login ?: "ginwa")
            setFollowingsDataListener()
        }
    }

    private fun setFollowingsDataListener() {
        fragmentVm.followings.removeObservers(viewLifecycleOwner)
        fragmentVm.followings.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Result.Success -> setUserFollowings(result.data)
                is Result.Error -> setError(result.exception.message)
                Result.Loading -> setLoading()
            }
        })
    }

    private fun setError(message: String?) {
        requireActivity().showToast(message)
    }

    private fun setCompleted() {
        activityVm.progressbarListener.value = false
        binding?.swipeRefreshLayout?.isRefreshing = false
        if (adapter?.dataSets?.isEmpty() == true) binding?.tvNoticeContainer?.visibility =
            View.VISIBLE
        else binding?.tvNoticeContainer?.visibility = View.GONE
    }

    private fun setLoading() {
        binding?.swipeRefreshLayout?.isRefreshing = true
        activityVm.progressbarListener.value = true
    }

    private fun setUserFollowings(dataSets: List<Following>) {
        adapter?.updateDataSets(dataSets)
        fragmentVm.followingsDataSets.clear()
        fragmentVm.followingsDataSets.addAll(dataSets)
        setCompleted()
    }

    private fun setAdapter() {
        adapter = FollowingsAdapter(requireContext(), fragmentVm.followingsDataSets)
        adapter?.onClick = {
            findNavController().navigate(R.id.action_nav_detail_graph_self, it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding?.apply {
            recyclerView.recycledViewPool.clear()
            recyclerView.adapter = null
            recyclerView.layoutManager = null
            swipeRefreshLayout.setOnRefreshListener(null)
        }
        adapter = null
        _binding = null
    }


    private fun setRecyclerView() {
        binding?.apply {
            recyclerView.apply {
                adapter = this@FollowingsFragment.adapter
                layoutManager = LinearLayoutManager(requireContext())
                isNestedScrollingEnabled = false
                addDividerLine(
                    dimenLeft = R.dimen.divider_line_left,
                    dimenRight = R.dimen.divider_line_right
                )
            }
        }
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