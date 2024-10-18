package ru.practicum.android.diploma.filters.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import ru.practicum.android.diploma.filters.presentation.models.FilterIndustryState
import ru.practicum.android.diploma.filters.ui.adapters.IndustriesAdapter

class SelectIndustryFragment : Fragment(R.layout.fragment_select_industry) {
    private val binding by viewBinding(FragmentSelectIndustryBinding::bind)
    private val viewModel by viewModel<IndustryViewModel>()
    private val filterParametersViewModel: FilterParametersViewModel by navGraphViewModels(R.id.root_navigation_graph)

    private var isClickAllowed = true
    private var industries = listOf<Industry>()

    private val adapter = IndustriesAdapter(onItemSelect = { if (clickDebounce()) saveSelect(it) })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        filterParametersViewModel.getFilterParametersLiveData().observe(viewLifecycleOwner) { parametrs ->
            adapter.setSelectedPosition(if (parametrs.industry != null) {
                parametrs.industry.id
            } else {
                "-1"
            })
        }
        binding.recyclerview.adapter = adapter
        super.onViewCreated(view, savedInstanceState)
        searchIndustries()
        initListeners()
    }

    private fun initListeners() {
        with(binding) {
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }

            etSearch.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                    // empty
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    // empty
                }

                override fun onTextChanged(s: CharSequence, p1: Int, p2: Int, p3: Int) {
                    viewModel.searchRequest(s.toString())
                    if (s.isEmpty()) {
                        ivSearch.setImageResource(R.drawable.ic_search)
                        ivSearch.isClickable = false
                    } else {
                        ivSearch.setImageResource(R.drawable.ic_clear)
                        ivSearch.isClickable = true
                    }
                }
            })

            ivSearch.setOnClickListener {
                etSearch.setText("")
                viewModel.getIndustries()
            }
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
            is FilterIndustryState.InternetError -> showError(
                R.drawable.er_no_internet,
                getString(R.string.no_internet)
            )

            is FilterIndustryState.NothingFound -> showError(
                R.drawable.er_nothing_found,
                getString(R.string.no_industry)
            )

            is FilterIndustryState.UnknownError -> showError(
                R.drawable.er_server_error,
                getString(R.string.server_error)
            )

            is FilterIndustryState.IndustryFound -> showResults(state.industries)

            is FilterIndustryState.Loading -> showLoading()
        }
    }

    private fun showError(image: Int, message: String) {
        with(binding) {
            llPlaceholder.isVisible = true
            progressBar.isVisible = false
            recyclerview.isVisible = false
            ivError.setImageResource(image)
            tvError.text = message
        }
    }

    private fun showLoading() {
        with(binding) {
            progressBar.isVisible = true
            recyclerview.isVisible = false
            llPlaceholder.isVisible = false
        }
    }

    private fun showResults(list: List<Industry>) {
        with(binding) {
            progressBar.isVisible = false
            recyclerview.isVisible = true
        }
        industries = list
        adapter.submitList(list)
    }

    private fun saveSelect(select: Industry) {
        val selectedPosition = industries.indexOfFirst { it.id == select.id }
        if (selectedPosition != -1){
            adapter.setSelectedPosition(selectedPosition.toString())
        }
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
