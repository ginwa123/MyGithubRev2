package com.m.ginwa.mygithubrev2.ui.detailuserfragment.biofragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.m.ginwa.core.data.Result
import com.m.ginwa.core.domain.model.User
import com.m.ginwa.core.utils.showToast
import com.m.ginwa.mygithubrev2.R
import com.m.ginwa.mygithubrev2.databinding.FragmentBioBinding
import com.m.ginwa.mygithubrev2.ui.ActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class BioFragment : Fragment() {

    private var login: String? = null
    private var btFavorite: FloatingActionButton? = null
    private var _binding: FragmentBioBinding? = null
    private val binding get() = _binding
    private val fragmentVm: BioViewModel by viewModels()
    private val activityVm: ActivityViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        login = arguments?.getString("login")
        lifecycleScope.launch {
            fragmentVm.getUser(login ?: "ginwa")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBioBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUserProfile(fragmentVm.userData)
        setUserDataListener()
        binding?.swipeRefreshLayout?.setOnRefreshListener {
            lifecycleScope.launch {
                fragmentVm.getUser(login ?: "ginwa")
                setUserDataListener()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUserDataListener() {
        lifecycleScope.launch {
            fragmentVm.user?.removeObservers(viewLifecycleOwner)
            fragmentVm.user?.observe(viewLifecycleOwner, { result ->
                when (result) {
                    is Result.Success -> setUserProfile(result.data)
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

    private fun setLoading() {
        binding?.swipeRefreshLayout?.isRefreshing = true
        activityVm.progressbarListener.value = true
    }

    private fun setCompleted() {
        activityVm.progressbarListener.value = false
        binding?.swipeRefreshLayout?.isRefreshing = false
    }


    private fun setUserProfile(user: User?) {
        binding?.apply {
            user?.apply {
                tvName.text = name
                tvCompany.text = company
                tvLocation.text = location
                if (publicRepos != null) tvRepository.text =
                    String.format("${getString(R.string.repositories)} : $publicRepos")
                if (followers != null) tvFollowers.text =
                    String.format("${getString(R.string.followers)} : ${followers?.size}")
                if (following != null) tvFollowings.text =
                    String.format("${getString(R.string.following)} : ${following?.size}")

                if (!tvName.text.isNullOrEmpty()) tvNameContainer.visibility = VISIBLE
                else tvNameContainer.visibility = GONE

                if (!tvCompany.text.isNullOrEmpty()) tvCompanyContainer.visibility = VISIBLE
                else tvCompanyContainer.visibility = GONE

                if (!tvLocation.text.isNullOrEmpty()) tvLocationContainer.visibility = VISIBLE
                else tvLocationContainer.visibility = GONE

                if (!tvRepository.text.isNullOrEmpty()) tvRepositoryContainer.visibility =
                    VISIBLE
                else tvRepositoryContainer.visibility = GONE

                if (!tvFollowers.text.isNullOrEmpty()) tvFollowersContainer.visibility = VISIBLE
                else tvFollowersContainer.visibility = GONE

                if (!tvFollowings.text.isNullOrEmpty()) tvFollowingContainer.visibility =
                    VISIBLE
                else tvFollowingContainer.visibility = GONE

                activityVm.imToolbarListener.value = avatarUrl
                btFavorite = requireActivity().findViewById(R.id.bt_favorite)
                btFavorite?.visibility = VISIBLE
                if (isFavorite) btFavorite?.setImageResource(R.drawable.ic_baseline_favorite_24)
                else btFavorite?.setImageResource(R.drawable.ic_baseline_favorite_border_48)
                fragmentVm.userData = user
                setBtFavoriteListener(user)
                setCompleted()
            }
        }
    }

    private fun setBtFavoriteListener(user: User) {
        binding?.apply {
            btFavorite?.setOnClickListener {
                lifecycleScope.launch {
                    fragmentVm.updateUser(user)
                }
            }
        }
    }


    companion object {
        fun newInstance(login: String?): Fragment {
            val fragment = BioFragment()
            val bundle = bundleOf("login" to login)
            fragment.arguments = bundle
            return fragment
        }
    }
}