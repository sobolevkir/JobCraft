package ru.practicum.android.diploma.common.ui

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ActivityRootBinding

class RootActivity : AppCompatActivity() {

    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                handleCustomBackPressed()
            }
        }

    private val bottomNavigationTabs = listOf(
        R.id.searchFragment,
        R.id.teamFragment,
        R.id.favoritesFragment
    )

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.rootFragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.bottomNavigationView.isVisible = destination.id in bottomNavigationTabs
        }

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    private fun handleCustomBackPressed() {
        if (bottomNavigationTabs.contains(navController.currentDestination?.id)) {
            moveTaskToBack(true)
        } else {
            if (!navController.popBackStack()) {
                super.onBackPressedDispatcher.onBackPressed()
            }
        }
    }
}

