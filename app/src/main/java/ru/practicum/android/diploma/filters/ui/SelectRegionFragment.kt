package ru.practicum.android.diploma.filters.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.ext.viewBinding
import ru.practicum.android.diploma.databinding.FragmentSelectRegionBinding

class SelectRegionFragment : Fragment(R.layout.fragment_select_region) {
    private val binding by viewBinding(FragmentSelectRegionBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}
