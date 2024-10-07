package ru.practicum.android.diploma.vacancy.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
 Epic3_44_Favorites_screen
import androidx.navigation.fragment.navArgs

import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.ext.viewBinding
import ru.practicum.android.diploma.databinding.FragmentVacancyBinding
import ru.practicum.android.diploma.vacancy.presentation.VacancyViewModel

class VacancyFragment : Fragment(R.layout.fragment_vacancy) {
    private val binding by viewBinding(FragmentVacancyBinding::bind)
 Epic3_44_Favorites_screen
    private val args: VacancyFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.txHeader.text = "id = $args"
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.getVacancyDetailsTest()

    }

}
