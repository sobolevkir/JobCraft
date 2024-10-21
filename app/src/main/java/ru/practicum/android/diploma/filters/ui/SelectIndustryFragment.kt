package ru.practicum.android.diploma.filters.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
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

    private val adapter = IndustriesAdapter(onItemSelect = { saveSelect(it) })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getSavedValue()
        searchIndustries()
        initListeners()
        binding.recyclerview.adapter = adapter
        binding.recyclerview.itemAnimator = null
    }

    private fun getSavedValue() {
        val value = filterParametersViewModel.getFilterParametersLiveData().value
        if (value?.industry != null) {
            viewModel.setSelectedID(value.industry.id)
        }
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
                    viewModel.saveSearchText(s.toString())
                    viewModel.getIndustriesWithSelected()
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
                etSearch.setText(EMPTY_TEXT)
                viewModel.saveSearchText(EMPTY_TEXT)
                viewModel.getIndustriesWithSelected()
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

            is FilterIndustryState.NoList -> showError(
                R.drawable.er_failed_to_get_list,
                getString(R.string.failed_to_get_list)
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

    private fun showResults(list: List<IndustryForUi>) {
        with(binding) {
            llPlaceholder.isVisible = false
            progressBar.isVisible = false
            recyclerview.isVisible = true
            adapter.submitList(list)
        }
    }

    private fun saveSelect(select: IndustryForUi) {
        viewModel.setSelectedID(select.id)
        viewModel.getIndustriesWithSelected()
        binding.selectBtn.isVisible = true
        binding.selectBtn.setOnClickListener {
            filterParametersViewModel.setIndustry(Industry(select.id, select.name))
            findNavController().popBackStack()
        }
    }

    companion object {
        const val EMPTY_TEXT = ""
    }
}
