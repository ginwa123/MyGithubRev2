package com.m.ginwa.mygithubrev2.ui

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.m.ginwa.core.utils.EspressoIdlingResources
import com.m.ginwa.core.utils.GlideApp
import com.m.ginwa.mygithubrev2.R
import com.m.ginwa.mygithubrev2.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var expandJob: Job? = null
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private val activityVm: ActivityViewModel by viewModels()
    private var startOnce = true

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setSupportActionBar(binding.toolbar)
        setNav()
        setToolbarListener()
    }

    private fun setNav() {
        binding.apply {
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            navController = navHostFragment.navController
            appBarConfiguration = AppBarConfiguration(setOf(R.id.searchFragment))
            setupActionBarWithNavController(navController, appBarConfiguration)
            toolbar.setupWithNavController(navController, appBarConfiguration)
            collapsingToolbar.setupWithNavController(toolbar, navController, appBarConfiguration)
            navController.addOnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    R.id.searchFragment -> {
                        activityVm.isExpandToolbarListener.value =
                            mapOf("isExpand" to false, "isAnimate" to true)
                    }
                    R.id.detailUserFragment -> {

                    }
                    R.id.favoriteFragment -> {
                        activityVm.isExpandToolbarListener.value =
                            mapOf("isExpand" to false, "isAnimate" to true)
                        activityVm.progressbarListener.value = false
                    }
                    else -> {
                        activityVm.isExpandToolbarListener.value =
                            mapOf("isExpand" to false, "isAnimate" to true)
                        activityVm.progressbarListener.value = false
                    }
                }
            }
        }
    }

    private fun setToolbarListener() {
        binding.apply {
            // observe progressbar (loading)
            activityVm.progressbarListener.observe(this@MainActivity, { isLoading ->
                if (isLoading) {
                    EspressoIdlingResources.increment()
                    progressBar.visibility = View.VISIBLE
                } else {
                    progressBar.visibility = View.GONE
                    EspressoIdlingResources.decrement()
                }
            })
            // observe image toolbar
            activityVm.imToolbarListener.observe(this@MainActivity, { im ->
                // cancel load image
                GlideApp.with(this@MainActivity.applicationContext).clear(imUser)
                if (im != null) {
                    // load image
                    GlideApp.with(this@MainActivity.applicationContext)
                        .load(im)
                        .override(512, 512)
                        .into(imUser)
                    return@observe
                }
            })
            // observe collapsed toolbar
            activityVm.isExpandToolbarListener.observe(this@MainActivity, { map ->
                expandJob?.cancel()
                expandJob = lifecycleScope.launch {
                    val isExpand = map["isExpand"]
                    val isAnimate = map["isAnimate"]
                    val isFastAnimate = map["isFastAnimate"] ?: true
                    if (isExpand != null && isAnimate != null) {
                        if (isExpand) {
                            if (!isFastAnimate) delay(1000)
                            btFavorite.visibility = View.VISIBLE
                            appBarLayout.setExpanded(true, isAnimate)
                        } else {
                            if (startOnce) {
                                appBarLayout.setExpanded(false, false)
                                startOnce = false
                            } else {
                                btFavorite.visibility = View.GONE
                                appBarLayout.setExpanded(false, isAnimate)
                            }
                        }
                    }
                }
            })

            activityVm.btFavoriteListener.observe(this@MainActivity, { user ->
                user?.apply {
                    btFavorite.visibility = View.VISIBLE
                    if (isFavorite) {
                        btFavorite.setImageResource(R.drawable.ic_baseline_favorite_pink_24)
                    } else btFavorite.setImageResource(R.drawable.ic_baseline_favorite_border_48)
                    btFavorite.setOnClickListener { activityVm.updateUser(user) }
                    return@observe
                }
                btFavorite.visibility = View.GONE
            })

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}