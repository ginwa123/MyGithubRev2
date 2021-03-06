package com.m.ginwa.favorite.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.m.ginwa.core.di.CoreModuleDependencies
import com.m.ginwa.core.domain.model.User
import com.m.ginwa.core.utils.Constants
import com.m.ginwa.core.utils.addDividerLine
import com.m.ginwa.favorite.R
import com.m.ginwa.favorite.databinding.FragmentFavoriteBinding
import com.m.ginwa.favorite.di.DaggerFavoriteComponent
import com.m.ginwa.favorite.utils.SwipeToDelete
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavoriteFragment : Fragment() {
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var snackBar: Snackbar? = null
    private var adapter: FavoriteAdapter? = null
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding

    @Inject
    lateinit var fragmentVm: FavoriteViewModel

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerFavoriteComponent.builder()
            .context(requireContext())
            .coreDependencies(
                EntryPointAccessors.fromApplication(
                    requireContext(),
                    CoreModuleDependencies::class.java
                )
            )
            .build()
            .injectFavoriteFragment(this)


    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        loadUsers()
        setRecyclerView()
    }

    private fun loadUsers() {
        fragmentVm.users.removeObservers(viewLifecycleOwner)
        fragmentVm.users.observe(viewLifecycleOwner, { result ->
            setFavoritesUsers(result)
        })
    }


    private fun setCompleted() {
        if (fragmentVm.dataSets.isEmpty()) {
            binding?.tvNoticeContainer?.visibility = View.VISIBLE
        } else {
            binding?.tvNoticeContainer?.visibility = View.GONE
            val isShowFavoriteOnBoard =
                sharedPreferences.getBoolean(Constants.FAVORITE_ONBOARD, true)
            if (isShowFavoriteOnBoard) {
                findNavController()
                    .navigate(com.m.ginwa.mygithubrev2.R.id.action_favoriteFragment_to_favoriteOnBoardFragment)
            }
        }
    }

    private fun setFavoritesUsers(data: List<User>) {
        if (data.isNotEmpty()) {
            adapter?.updateDataSets(data)
            fragmentVm.dataSets.clear()
            fragmentVm.dataSets.addAll(data)
        }
        setCompleted()
    }

    private fun setRecyclerView() {
        binding?.apply {
            val itemTouchHelper = ItemTouchHelper(SwipeToDelete(this@FavoriteFragment.adapter))
            linearLayoutManager = LinearLayoutManager(requireContext())
            recyclerView.apply {
                adapter = this@FavoriteFragment.adapter
                layoutManager = linearLayoutManager
                isNestedScrollingEnabled = false
                itemTouchHelper.attachToRecyclerView(this)
                itemAnimator = null
                addDividerLine(
                    dimenLeft = com.m.ginwa.mygithubrev2.R.dimen.divider_line_left,
                    dimenRight = com.m.ginwa.mygithubrev2.R.dimen.divider_line_right
                )
            }
        }
    }

    private fun setAdapter() {
        adapter = FavoriteAdapter(requireContext(), dataSets = fragmentVm.dataSets)
        adapter?.onClick = {
            findNavController().navigate(
                com.m.ginwa.mygithubrev2.R.id.action_favoriteFragment_to_nav_detail_graph,
                it
            )
        }
        adapter?.onDelete = { _, user ->
            lifecycleScope.launch {
                user.isFavorite = false
                fragmentVm.updateUser(user)
                showUndoSnackBar(user)
            }
        }
    }

    private fun showUndoSnackBar(user: User) {
        val message =
            StringBuilder("${getString(R.string.delete)} ${user.login} ${getString(R.string.from_list)}")
        snackBar = Snackbar.make(
            requireView(),
            message,
            Snackbar.LENGTH_LONG
        )
        snackBar?.setAction(getString(R.string.undo)) { undo(user) }
        snackBar?.show()
    }

    private fun undo(user: User) {
        lifecycleScope.launch {
            user.isFavorite = true
            fragmentVm.updateUser(user)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding?.apply {
            recyclerView.recycledViewPool.clear()
            recyclerView.adapter = null
            recyclerView.layoutManager = null
        }
        adapter = null
        _binding = null
    }


}