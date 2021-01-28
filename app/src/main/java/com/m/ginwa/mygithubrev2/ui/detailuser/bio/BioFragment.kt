package com.m.ginwa.mygithubrev2.ui.detailuser.bio

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
import com.m.ginwa.core.data.Result
import com.m.ginwa.core.domain.model.User
import com.m.ginwa.core.utils.showToastError
import com.m.ginwa.mygithubrev2.R
import com.m.ginwa.mygithubrev2.databinding.FragmentBioBinding
import com.m.ginwa.mygithubrev2.ui.ActivityViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class BioFragment : Fragment() {

    private var login: String? = null
    private var _binding: FragmentBioBinding? = null
    private val binding get() = _binding
    private val fragmentVm: BioViewModel by viewModels()
    private val activityVm: ActivityViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        login = arguments?.getString("login")
        fragmentVm.login = login ?: "ginwa"
        fragmentVm.getUser(login ?: "ginwa")
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
            fragmentVm.getUser(fragmentVm.login)
            setUserDataListener()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding?.apply {
            swipeRefreshLayout.setOnRefreshListener(null)
        }
        _binding = null
    }

    private fun setUserDataListener() {
        fragmentVm.user.removeObservers(viewLifecycleOwner)
        fragmentVm.user.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Result.Success -> setUserProfile(result.data)
                is Result.Error -> setError(result.exception)
                Result.Loading -> setLoading()
            }
        })
    }


    private fun setError(exception: Exception) {
        requireActivity().showToastError(exception)
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
                    StringBuilder("${getString(R.string.repositories)} : $publicRepos")
                if (followers != null) tvFollowers.text =
                    StringBuilder("${getString(R.string.followers)} : ${followers?.size}")
                if (following != null) tvFollowings.text =
                    StringBuilder("${getString(R.string.following)} : ${following?.size}")

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
                activityVm.btFavoriteListener.value = user
                fragmentVm.userData = user
                setCompleted()
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