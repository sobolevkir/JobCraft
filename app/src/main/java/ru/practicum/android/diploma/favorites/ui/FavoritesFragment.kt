package ru.practicum.android.diploma.favorites.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.domain.model.VacancyFromList
import ru.practicum.android.diploma.common.ext.viewBinding
import ru.practicum.android.diploma.common.ui.VacancyListAdapter
import ru.practicum.android.diploma.common.util.debounce
import ru.practicum.android.diploma.databinding.FragmentFavoritesBinding
import ru.practicum.android.diploma.favorites.presentation.FavoritesViewModel

class FavoritesFragment : Fragment(R.layout.fragment_favorites) {
    private val viewModel: FavoritesViewModel by viewModel()
    private val binding by viewBinding(FragmentFavoritesBinding::bind)
    private var favoritesAdapter: VacancyListAdapter? = null
    private lateinit var onVacancyClickDebounce: (VacancyFromList) -> Unit

    companion object {
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 100L
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.testDb()
        initVacancyDebounce()
        favoritesAdapter = VacancyListAdapter(onItemClick = { onVacancyClickDebounce(it) })
        binding.rvFoundVacanciesList.adapter = favoritesAdapter
    }

    private fun openVacancy(vacancy: VacancyFromList) {
        val action = FavoritesFragmentDirections.actionFavoritesFragmentToVacancyFragment(vacancy.id)
        findNavController().navigate(action)
    }
    private fun initVacancyDebounce() {
        onVacancyClickDebounce = debounce(
            CLICK_DEBOUNCE_DELAY_MILLIS,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { vacancy -> openVacancy(vacancy) }
    }

}
