package ru.practicum.android.diploma.filters.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.ext.viewBinding
import ru.practicum.android.diploma.databinding.FragmentFiltersBinding

class FiltersFragment : Fragment(R.layout.fragment_vacancy) {
    private val binding by viewBinding(FragmentFiltersBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}
