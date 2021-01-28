package com.m.ginwa.favorite.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.m.ginwa.core.di.CoreModuleDependencies
import com.m.ginwa.core.utils.Constants
import com.m.ginwa.favorite.di.DaggerFavoriteComponent
import com.m.ginwa.mygithubrev2.databinding.FragmentOnboardFavoriteBinding
import dagger.hilt.android.EntryPointAccessors
import javax.inject.Inject

class FavoriteOnBoardFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentOnboardFavoriteBinding? = null
    private val binding get() = _binding

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOnboardFavoriteBinding.inflate(inflater, container, false)
        return binding?.root
    }

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
            .injectFavoriteOnBoardFragment(this)

        sharedPreferences
            .edit()
            .putBoolean(Constants.FAVORITE_ONBOARD, false)
            .apply()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.btOk?.setOnClickListener { dialog?.dismiss() }
    }


}