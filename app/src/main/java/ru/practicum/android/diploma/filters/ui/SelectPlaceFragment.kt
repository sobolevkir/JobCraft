package ru.practicum.android.diploma.filters.ui

import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.ext.viewBinding
import ru.practicum.android.diploma.common.presentation.FilterParametersViewModel
import ru.practicum.android.diploma.databinding.FragmentSelectPlaceBinding

class SelectPlaceFragment : Fragment(R.layout.fragment_select_place) {
    private var isClickAllowed = true
    private val binding by viewBinding(FragmentSelectPlaceBinding::bind)
    private val filterParametersViewModel: FilterParametersViewModel by navGraphViewModels(R.id.root_navigation_graph)
    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                filterParametersViewModel.resetPlaceTemporaryLiveData()
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )
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
        binding.btnBack.setOnClickListener {
            filterParametersViewModel.resetPlaceTemporaryLiveData()
            findNavController().popBackStack()
        }
        binding.llCountry.setOnClickListener {
            if (clickDebounce()) openCountrySelection()
        }
        binding.llRegion.setOnClickListener {
            if (clickDebounce()) openRegionSelection()
        }
        binding.ivCountry.setOnClickListener {
            if (clickDebounce()) {
                if (!binding.tvCountryFilled.text.isNullOrEmpty()) {
                    resetCountry()
                } else {
                    openCountrySelection()
                }
            }
        }
        binding.ivRegion.setOnClickListener {
            if (clickDebounce()) {
                if (!binding.tvRegionFilled.text.isNullOrEmpty()) {
                    resetRegion()
                } else {
                    openRegionSelection()
                }
            }
        }
        binding.btnSelect.setOnClickListener {
            processingBtnSelect()
        }
        filterParametersViewModel.getPlaceTemporaryLiveData().observe(viewLifecycleOwner) { filterParameters ->
            binding.tvCountryFilled.text = filterParameters.countryTemp?.name.orEmpty()
            binding.tvRegionFilled.text = filterParameters.regionTemp?.name.orEmpty()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onBackPressedCallback.remove()
    }

    private fun visibilityBtnSelect() {
        binding.btnSelect.isVisible =
            !(binding.tvCountryFilled.text.isNullOrEmpty() && binding.tvRegionFilled.text.isNullOrEmpty())
    }

    private fun processingBtnSelect() {
        filterParametersViewModel.applyPlaceTemporaryLiveData()
        findNavController().popBackStack()
    }

    private fun resetCountry() {
        filterParametersViewModel.setCountryTemporary(null)
        filterParametersViewModel.setRegionTemporary(null)
    }

    private fun resetRegion() {
        filterParametersViewModel.setRegionTemporary(null)
    }

    private fun openCountrySelection() {
        val action = SelectPlaceFragmentDirections.actionSelectPlaceFragmentToSelectCountryFragment()
        findNavController().navigate(action)
    }

    private fun openRegionSelection() {
        val action = SelectPlaceFragmentDirections.actionSelectPlaceFragmentToSelectRegionFragment()
        findNavController().navigate(action)
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            CoroutineScope(Dispatchers.Main).launch {
                delay(CLICK_DEBOUNCE_DELAY_MILLIS)
                isClickAllowed = true
            }
        }
        return current
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
    }
}
