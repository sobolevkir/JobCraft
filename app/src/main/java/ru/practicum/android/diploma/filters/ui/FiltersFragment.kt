package ru.practicum.android.diploma.filters.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.ext.viewBinding
import ru.practicum.android.diploma.common.presentation.FilterParametersViewModel
import ru.practicum.android.diploma.databinding.FragmentFiltersBinding

class FiltersFragment : Fragment(R.layout.fragment_filters) {
    private val binding by viewBinding(FragmentFiltersBinding::bind)

    // добавляем общую для всего графа навигации ViewModel
    private val filterParametersViewModel: FilterParametersViewModel by navGraphViewModels(R.id.root_navigation_graph)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.etSelectPlace.setOnClickListener {
            openPlaceSelection()
        }
        binding.etSelectIndustry.setOnClickListener {
            openIndustrySelection()
        }

        binding.btnApply.setOnClickListener {
            applyFilters()
        }

    }

    private fun openPlaceSelection() {
        val action = FiltersFragmentDirections.actionFiltersFragmentToSelectPlaceFragment()
        findNavController().navigate(action)
    }

    private fun openIndustrySelection() {
        val action = FiltersFragmentDirections.actionFiltersFragmentToSelectIndustryFragment()
        findNavController().navigate(action)
    }

    private fun applyFilters() {
        filterParametersViewModel.applyFilters()
        // Можно делать и вот так:
        // filterParametersViewMode.setFilterParameters(parameters)
        // это обновит лайвдату для всего графа навигации, где используется FilterParametersViewModel
        findNavController().popBackStack()
    }

}
