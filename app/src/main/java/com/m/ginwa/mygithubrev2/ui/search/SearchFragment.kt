package com.m.ginwa.mygithubrev2.ui.search

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import androidx.recyclerview.widget.LinearLayoutManager
import com.m.ginwa.core.data.Result
import com.m.ginwa.core.domain.model.User
import com.m.ginwa.core.utils.addDividerLine
import com.m.ginwa.core.utils.showToastError
import com.m.ginwa.mygithubrev2.R
import com.m.ginwa.mygithubrev2.databinding.FragmentSearchBinding
import com.m.ginwa.mygithubrev2.ui.ActivityViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchFragment : Fragment() {
    private lateinit var searchView: androidx.appcompat.widget.SearchView
    private var searchAdapter: SearchAdapter? = null
    private lateinit var navController: NavController
    private var login: String? = null
    private val fragmentVm: SearchUsersViewModel by viewModels()
    private val activityVm: ActivityViewModel by activityViewModels()
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        navController = findNavController()
        login = arguments?.getString("login")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        loadUsers(login ?: "ginwa")
    }

    private fun setAdapter() {
        binding?.apply {
            activityVm.btFavoriteListener.value = null
            searchAdapter = SearchAdapter(requireContext(), fragmentVm.dataSets)
            searchAdapter?.onClick = {
                navController.navigate(R.id.action_searchFragment_to_nav_detail_graph, it)

            }

            recyclerView.apply {
                adapter = searchAdapter
                layoutManager = LinearLayoutManager(requireContext())
                isNestedScrollingEnabled = false
                addDividerLine(
                    dimenLeft = R.dimen.divider_line_left,
                    dimenRight = R.dimen.divider_line_right
                )
            }
            swipeRefreshLayout.setOnRefreshListener {
                searchAdapter?.clearDataSets()
                loadUsers(login ?: "ginwa")
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)
        searchView =
            menu.findItem(R.id.action_search).actionView as androidx.appcompat.widget.SearchView
        searchView.setQuery(login, false)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null && query.isNotEmpty()) {
                    val bundle = bundleOf("login" to query)
                    navController.navigate(R.id.action_searchFragment_self, bundle)
                    return false
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return onNavDestinationSelected(
            item,
            requireView().findNavController()
        ) || super.onOptionsItemSelected(item)
    }

    private fun loadUsers(login: String) {
        if (searchAdapter?.dataSets?.isEmpty() == true) {
            fragmentVm.getUsers(login)
            fragmentVm.users.removeObservers(viewLifecycleOwner)
            fragmentVm.users.observe(viewLifecycleOwner, { result ->
                when (result) {
                    is Result.Success -> setUsers(result.data)
                    is Result.Error -> setError(result.exception)
                    Result.Loading -> setLoading()
                }
            })
        }
    }

    private fun setLoading() {
        binding?.swipeRefreshLayout?.isRefreshing = true
        activityVm.progressbarListener.value = true
    }

    private fun setUsers(dataSets: List<User>) {
        if (dataSets.isNotEmpty()) {
            searchAdapter?.updateDataSets(dataSets)
            fragmentVm.dataSets.clear()
            fragmentVm.dataSets.addAll(dataSets)
        }
        setCompleted()
    }

    private fun setError(exception: Exception) {
        requireActivity().showToastError(exception)
        setCompleted()
    }

    private fun setCompleted() {
        activityVm.progressbarListener.value = false
        binding?.swipeRefreshLayout?.isRefreshing = false
        if (fragmentVm.dataSets.isEmpty()) {
            binding?.tvNoticeContainer?.visibility = View.VISIBLE
        } else {
            binding?.tvNoticeContainer?.visibility = View.GONE
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
        searchAdapter = null
        _binding = null
    }
}