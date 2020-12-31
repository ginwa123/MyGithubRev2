package com.m.ginwa.favorite.ui

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
import com.m.ginwa.core.data.Result
import com.m.ginwa.core.di.CoreModuleDependencies
import com.m.ginwa.core.domain.model.User
import com.m.ginwa.core.utils.showToast
import com.m.ginwa.favorite.R
import com.m.ginwa.favorite.databinding.FragmentFavoriteBinding
import com.m.ginwa.favorite.di.DaggerFavoriteComponent
import com.m.ginwa.favorite.utils.SwipeToDelete
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavoriteFragment : Fragment() {
    private var snackBar: Snackbar? = null
    private lateinit var adapter: FavoriteAdapter
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding

    @Inject
    lateinit var fragmentVm: FavoriteViewModel

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
            .inject(this)
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
    }

    private fun loadUsers() {
        lifecycleScope.launch {
            fragmentVm.getUsersFavorite(true)
            fragmentVm.users?.removeObservers(viewLifecycleOwner)
            fragmentVm.users?.observe(viewLifecycleOwner, { result ->
                when (result) {
                    is Result.Success -> setFavoritesUsers(result.data)
                    is Result.Error -> setError(result.exception.message)
                    Result.Loading -> {
                    }
                }
            })
        }
    }

    private fun setError(message: String?) {
        requireContext().showToast(message)
        setCompleted()
    }

    private fun setCompleted() {
        if (fragmentVm.dataSets.isEmpty()) {
            binding?.tvNoticeContainer?.visibility = View.VISIBLE
        } else binding?.tvNoticeContainer?.visibility = View.GONE
    }

    private fun setFavoritesUsers(data: List<User>) {
        if (data.isNotEmpty()) {
            adapter.updateDataSets(data)
            fragmentVm.dataSets.clear()
            fragmentVm.dataSets.addAll(data)
        }
        setCompleted()
    }

    private fun setAdapter() {
        binding?.apply {
            adapter = FavoriteAdapter(requireContext(), dataSets = fragmentVm.dataSets)
            val itemTouchHelper = ItemTouchHelper(SwipeToDelete(adapter))
            adapter.onClick = {
                findNavController().navigate(
                    com.m.ginwa.mygithubrev2.R.id.action_favoriteFragment_to_nav_detail_graph,
                    it
                )
            }
            adapter.onDelete = { position, user ->
                lifecycleScope.launch {
                    user.isFavorite = false
                    fragmentVm.updateUser(user)
                    showUndoSnackBar(user)
                }
            }
            recyclerView.apply {
                adapter = this@FavoriteFragment.adapter
                layoutManager = LinearLayoutManager(requireContext())
                isNestedScrollingEnabled = false
                itemTouchHelper.attachToRecyclerView(this)
            }
        }
    }

    private fun showUndoSnackBar(user: User) {
        val message =
            "${getString(R.string.delete)} ${user.login} ${getString(R.string.from_list)}"
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
        _binding = null
    }


}