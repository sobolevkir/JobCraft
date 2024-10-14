package ru.practicum.android.diploma.filters.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.ext.viewBinding
import ru.practicum.android.diploma.databinding.FragmentSelectPlaceBinding

class SelectPlaceFragment : Fragment(R.layout.fragment_select_place) {
    private val binding by viewBinding(FragmentSelectPlaceBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.llCountry.setOnClickListener {
            openCountrySelection()
        }
        binding.llRegion.setOnClickListener {
            openRegionSelection()
        }

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
