package ru.practicum.android.diploma.filters.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.ext.viewBinding
import ru.practicum.android.diploma.common.presentation.FilterParametersViewModel
import ru.practicum.android.diploma.databinding.FragmentSelectRegionBinding
import ru.practicum.android.diploma.filters.domain.model.Area
import ru.practicum.android.diploma.filters.presentation.RegionViewModel
import ru.practicum.android.diploma.filters.presentation.models.AreaState
import ru.practicum.android.diploma.filters.ui.adapters.RegionListAdapter

class SelectRegionFragment : Fragment(R.layout.fragment_select_region) {
    private val binding by viewBinding(FragmentSelectRegionBinding::bind)
    private var isClickAllowed = true
    private val adapter: RegionListAdapter by lazy {
        RegionListAdapter(onItemClick = { if (clickDebounce()) applyChanges(it) })
    }

    private val viewModel: RegionViewModel by viewModel()
    private val filterParametersViewModel: FilterParametersViewModel by navGraphViewModels(R.id.root_navigation_graph)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        filterParametersViewModel.getPlaceTemporaryLiveData().observe(viewLifecycleOwner) { placeParameters ->
            viewModel.getRegions(placeParameters.countryTemp?.id, placeParameters.countryTemp?.name)
        }
        viewModel.getStateLiveData().observe(viewLifecycleOwner) { renderState(it) }
        setStartOptions()
        initClickListeners()
        initQueryChangeListener()
        binding.rvAreaList.adapter = adapter
        binding.rvAreaList.itemAnimator = null
    }

    private fun renderState(state: AreaState) {
        when (state) {
            is AreaState.InternetError -> showError(R.drawable.er_no_internet, R.string.no_internet, true)
            is AreaState.ServerError -> showError(R.drawable.er_server_error, R.string.server_error, true)
            is AreaState.NothingFound -> showError(R.drawable.er_nothing_found, R.string.no_regions, true)
            is AreaState.NoList -> showError(R.drawable.er_failed_to_get_list, R.string.failed_to_get_list, true)
            is AreaState.Success -> showResults(state.regions)
            is AreaState.Loading -> showLoading()
        }
    }

    private fun showLoading() {
        with(binding) {
            llError.isVisible = false
            progressBar.isVisible = true
            rvAreaList.isVisible = false
        }
    }

    private fun showResults(regions: List<Area>) {
        adapter.submitList(regions) {
            with(binding) {
                llError.isVisible = false
                progressBar.isVisible = false
                rvAreaList.isVisible = true
            }
        }
    }

    private fun showError(image: Int, text: Int? = null, messageState: Boolean = false) {
        with(binding) {
            llError.isVisible = true
            progressBar.isVisible = false
            rvAreaList.isVisible = false
            ivError.setImageResource(image)
            tvError.isVisible = messageState
            if (text == null) {
                tvError.text = ""
            } else {
                tvError.setText(text)
            }
        }
    }

    private fun setStartOptions() {
        with(binding) {
            tvFragmentTitle.text = getString(R.string.region_but_sign)
            flSearch.isVisible = true
            progressBar.isVisible = false
        }
    }

    private fun initClickListeners() {
        binding.ivSearch.setOnClickListener {
            binding.etSearch.setText("")
        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initQueryChangeListener() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                // Empty
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Empty
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                viewModel.searchRequest(s.toString())
                if (s.isEmpty()) {
                    binding.ivSearch.setImageResource(R.drawable.ic_search)
                } else {
                    binding.ivSearch.setImageResource(R.drawable.ic_clear)
                }
            }
        })
    }

    private fun applyChanges(region: Area) {
        filterParametersViewModel.setRegionTemporary(region)
        val country = viewModel.getCountryByParentId(region.parentId.toString())
        filterParametersViewModel.setCountryTemporary(country)
        findNavController().popBackStack()
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY_MILLIS)
                isClickAllowed = true
            }
        }
        return current
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 500L
    }
}
