package ru.practicum.android.diploma.filters.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.ext.viewBinding
import ru.practicum.android.diploma.databinding.FragmentSelectIndustryBinding
import ru.practicum.android.diploma.filters.presentation.IndustryViewModel

class SelectIndustryFragment : Fragment(R.layout.fragment_select_industry) {
    private val binding by viewBinding(FragmentSelectIndustryBinding::bind)
    private val viewModel: IndustryViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.test()
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

    }

}
