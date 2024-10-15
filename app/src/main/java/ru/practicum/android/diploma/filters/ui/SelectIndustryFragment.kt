package ru.practicum.android.diploma.filters.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.ext.viewBinding
import ru.practicum.android.diploma.common.presentation.FilterParametersViewModel
import ru.practicum.android.diploma.databinding.FragmentSelectIndustryBinding
import ru.practicum.android.diploma.filters.domain.model.Industry
import ru.practicum.android.diploma.filters.presentation.IndustryViewModel
import ru.practicum.android.diploma.filters.presentation.states.FilterIndustryState
import ru.practicum.android.diploma.filters.ui.adapters.IndustriesAdapter

class SelectIndustryFragment : Fragment(R.layout.fragment_select_industry) {
    private val binding by viewBinding(FragmentSelectIndustryBinding::bind)
    private val viewModel by viewModel<IndustryViewModel>()
    private val filterParametersViewModel: FilterParametersViewModel by navGraphViewModels(R.id.root_navigation_graph)

    private var isClickAllowed = true

    private val adapter: IndustriesAdapter by lazy {
        IndustriesAdapter(onItemSelect = { if (clickDebounce()) saveSelect(it) })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchIndustries()
        initListeners()
    }

    private fun initListeners() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun searchIndustries() {
        viewModel.getIndustries()

        viewModel.getStateLiveData().observe(viewLifecycleOwner) {
            renderState(it)
        }
    }

    private fun renderState(state: FilterIndustryState) {
        when (state) {
            is FilterIndustryState.NothingFound -> showNothingFound()
            is FilterIndustryState.UnknownError -> showUnknownError()
            is FilterIndustryState.IndustryFound -> showResults(state.industries)
        }
    }

    private fun showNothingFound(){
        with(binding) {
            llPlaceholderEmpty.isVisible = true
            llPlaceholderUnknown.isVisible = false
            recyclerview.isVisible = false
        }
    }

    private fun showUnknownError() {
        with(binding) {
            llPlaceholderEmpty.isVisible = false
            llPlaceholderUnknown.isVisible = true
            recyclerview.isVisible = false
        }
    }

    private fun showResults(list: List<Industry>) {
        with(binding) {
            llPlaceholderEmpty.isVisible = false
            llPlaceholderUnknown.isVisible = false
            recyclerview.isVisible = true
        }
        adapter.submitList(list)
    }

    private fun saveSelect(select: Industry) {
        binding.selectBtn.isVisible = true
        binding.selectBtn.setOnClickListener {
            filterParametersViewModel.setIndustry(select)
            findNavController().popBackStack()
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY_MILLIS)
                isClickAllowed = true
            }
        }
        return current
    }

    companion object {
        const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
    }
}
