package ru.practicum.android.diploma.filters.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import androidx.navigation.navGraphViewModels
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.ext.viewBinding
import ru.practicum.android.diploma.common.presentation.FilterParametersViewModel
import ru.practicum.android.diploma.databinding.FragmentFiltersBinding
import ru.practicum.android.diploma.filters.presentation.states.FiltersState
import ru.practicum.android.diploma.filters.presentation.FiltersViewModel

class FiltersFragment : Fragment(R.layout.fragment_filters) {
    private val binding by viewBinding(FragmentFiltersBinding::bind)
    private val viewModel: FiltersViewModel by viewModel()

    init {
        viewModel.getFilters()
    }

    // добавляем общую для всего графа навигации ViewModel
    private val filterParametersViewModel: FilterParametersViewModel by navGraphViewModels(R.id.root_navigation_graph)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        viewModel.getStateLiveData().observe(viewLifecycleOwner) { renderState(it) }
    }

    private fun renderState(state: FiltersState){
        if (!state.isEmpty){
            with(binding){
                etSelectPlace.setText("${state.country}, ${state.region}")
                etSelectIndustry.setText(state.industry)
                etSalary.setText(state.minSalary)
                cbSalary.isChecked = state.isOnlyWithSalary
                binding.btnCancel.isVisible = false
                btnCancel.isVisible = true
            }
        }
    }

    private fun setFilters(place: String, industry: String, salary: String){

    }

    private fun initListeners(){
        binding.cbSalary.setOnCheckedChangeListener{_, isChecked ->
            viewModel.setCheckbox(isChecked)
        }
        binding.btnCancel.setOnClickListener{
            setEmptyFilters()
            viewModel.clear()
        }
        binding.btnApply.setOnClickListener{
            viewModel.saveFilters()
        }
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.etSelectPlace.setOnClickListener {
            openPlaceSelection()
        }
        binding.etSelectIndustry.setOnClickListener {
            openIndustrySelection()
        }
    }

    private fun setEmptyFilters(){
        setFilters("","", "")
        binding.cbSalary.isChecked = false
        binding.btnCancel.isVisible = false
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
