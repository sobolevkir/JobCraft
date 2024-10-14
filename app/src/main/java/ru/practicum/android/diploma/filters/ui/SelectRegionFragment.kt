package ru.practicum.android.diploma.filters.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.ext.viewBinding
import ru.practicum.android.diploma.databinding.FragmentSelectRegionBinding

class SelectRegionFragment : Fragment(R.layout.fragment_select_region) {
    private val binding by viewBinding(FragmentSelectRegionBinding::bind)
    private var isClickAllowed = true

    private val viewModel: AreaViewModel by viewModel()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.test()
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

    }

}
