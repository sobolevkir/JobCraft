package ru.practicum.android.diploma.filters.ui

import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.ext.viewBinding
import ru.practicum.android.diploma.common.presentation.FilterParametersViewModel
import ru.practicum.android.diploma.databinding.FragmentSelectPlaceBinding
import ru.practicum.android.diploma.filters.presentation.PlaceViewModel

class SelectPlaceFragment : Fragment(R.layout.fragment_select_place) {
    private val viewModel by viewModel<PlaceViewModel>()
    private val binding by viewBinding(FragmentSelectPlaceBinding::bind)
    private val filterParametersViewModel: FilterParametersViewModel by navGraphViewModels(R.id.root_navigation_graph)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvCountryFilled.addTextChangedListener(
            afterTextChanged = { s: Editable? ->
                if (s.isNullOrEmpty()) {
                    binding.ivCountry.setImageResource(R.drawable.ic_arrow_forward)
                    binding.llCountryFilled.isVisible = false
                    binding.tvCountryEmpty.isVisible = true
                } else {
                    binding.ivCountry.setImageResource(R.drawable.ic_clear)
                    binding.llCountryFilled.isVisible = true
                    binding.tvCountryEmpty.isVisible = false
                }
                visibilityBtnSelect()
            }
        )

        binding.tvRegionFilled.addTextChangedListener(
            afterTextChanged = { s: Editable? ->
                if (s.isNullOrEmpty()) {
                    binding.ivRegion.setImageResource(R.drawable.ic_arrow_forward)
                    binding.llRegionFilled.isVisible = false
                    binding.tvRegionEmpty.isVisible = true
                } else {
                    binding.ivRegion.setImageResource(R.drawable.ic_clear)
                    binding.llRegionFilled.isVisible = true
                    binding.tvRegionEmpty.isVisible = false
                }
                visibilityBtnSelect()
            }
        )
        // Нужен общий debounse для btnBack, llCountry, llRegion, btnSelect после клика по любой из них  запретить
        // реакцию на клик по всем вышеперечисленным и еще по ivCountry и ivRegion (по всем сенсорным объектам)
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.llCountry.setOnClickListener {
            openCountrySelection()
        }
        binding.llRegion.setOnClickListener {
            openRegionSelection()
        }
        binding.ivCountry.setOnClickListener {
            if (binding.tvCountryFilled.text.isNullOrEmpty()) resetCountry()
        }
        binding.ivRegion.setOnClickListener {
            if (binding.tvRegionFilled.text.isNullOrEmpty()) resetRegion()
        }
        binding.btnSelect.setOnClickListener {
            processingBtnSelect()
        }
        filterParametersViewModel.getFilterParametersLiveData().observe(viewLifecycleOwner) { filterParameters ->
            viewModel.passNewParameters(filterParameters.country, filterParameters.region)
        }
        viewModel.getAreaLiveData().observe(viewLifecycleOwner) { newState ->
            if (newState.country.isNullOrEmpty()) {
                binding.tvCountryFilled.text = ""
            } else {
                binding.tvCountryFilled.text = newState.country
            }
            if (newState.region.isNullOrEmpty()) {
                binding.tvRegionFilled.text = ""
            } else {
                binding.tvRegionFilled.text = newState.region
            }
        }
    }

    private fun visibilityBtnSelect() {
        binding.btnSelect.isVisible =
            !(binding.tvCountryFilled.text.isNullOrEmpty() && binding.tvRegionFilled.text.isNullOrEmpty())
    }

    private fun processingBtnSelect() {
        // Обработка нажатия BtnSelect
        findNavController().popBackStack()
    }

    private fun resetCountry() {
        viewModel.resetCountry()
        filterParametersViewModel.setCountry(null)
    }

    private fun resetRegion() {
        viewModel.resetRegion()
        filterParametersViewModel.setRegion(null)
    }

    private fun openCountrySelection() {
        val action = SelectPlaceFragmentDirections.actionSelectPlaceFragmentToSelectRegionFragment()
        findNavController().navigate(action)
    }

    private fun openRegionSelection() {
        val action = SelectPlaceFragmentDirections.actionSelectPlaceFragmentToSelectRegionFragment()
        findNavController().navigate(action)
    }
}
