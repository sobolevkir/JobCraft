package ru.practicum.android.diploma.filters.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.ext.viewBinding
import ru.practicum.android.diploma.common.presentation.FilterParametersViewModel
import ru.practicum.android.diploma.databinding.FragmentFiltersBinding
import ru.practicum.android.diploma.filters.domain.model.Area
import ru.practicum.android.diploma.filters.domain.model.FilterParameters

class FiltersFragment : Fragment(R.layout.fragment_filters) {
    private val binding by viewBinding(FragmentFiltersBinding::bind)
    private val filterParametersViewModel: FilterParametersViewModel by navGraphViewModels(R.id.root_navigation_graph)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.etSalary.setText(
            filterParametersViewModel.getFilterParametersLiveData().value?.expectedSalary?.toString().orEmpty()
        )
        filterParametersViewModel.getFilterParametersLiveData().observe(viewLifecycleOwner) {
            setParameters(it)
            Log.d("FILTERS!!!", "country - ${it?.country?.id.toString()}")
            Log.d("FILTERS!!!", "region - ${it?.region?.id.toString()}")
            Log.d("FILTERS!!!", "salary - ${it?.expectedSalary.toString()}")
            Log.d("FILTERS!!!", "onlyWithSalary - ${it?.onlyWithSalary.toString()}")
        }
        initListeners()
    }

    private fun setParameters(filters: FilterParameters?) {
        with(binding) {
            bindPlace(filters?.region, filters?.country)
            etSelectIndustry.setText(filters?.industry?.name.orEmpty())
            cbSalary.isChecked = filters?.onlyWithSalary ?: false
            btnCancel.isVisible = filters?.industry != null ||
                filters?.country != null ||
                filters?.expectedSalary != null ||
                filters?.onlyWithSalary ?: false
        }
    }

    private fun bindPlace(region: Area?, country: Area?) {
        if (region != null) {
            binding.etSelectPlace.setText(getString(R.string.full_place, country?.name ?: "", region.name))
        } else if (country != null) {
            binding.etSelectPlace.setText(country.name)
        } else {
            binding.etSelectPlace.setText("")
        }
    }

    private fun initListeners() {
        binding.cbSalary.setOnCheckedChangeListener { _, isChecked ->
            filterParametersViewModel.setOnlyWithSalary(isChecked)
            binding.etSalary.clearFocus()
        }
        binding.btnCancel.setOnClickListener {
            filterParametersViewModel.clearFilters()
            binding.etSalary.setText("")
            binding.etSalary.clearFocus()
        }
        binding.btnApply.setOnClickListener {
            applyFilters()
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
        binding.etSalary.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                // Empty
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Empty
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                viewLifecycleOwner.lifecycleScope.launch {
                    delay(SET_SALARY_DELAY_MILLIS)
                    if (s.toString().isNotEmpty()) {
                        filterParametersViewModel.setExpectedSalary(s.toString().toInt())
                    } else {
                        filterParametersViewModel.setExpectedSalary(null)
                    }
                }
            }
        })
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
        findNavController().popBackStack()
    }

    companion object {
        const val SET_SALARY_DELAY_MILLIS = 300L
    }

}
