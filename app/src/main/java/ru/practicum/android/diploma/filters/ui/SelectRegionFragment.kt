package ru.practicum.android.diploma.filters.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.ext.viewBinding
import ru.practicum.android.diploma.databinding.FragmentSelectRegionBinding
import ru.practicum.android.diploma.filters.domain.model.Area
import ru.practicum.android.diploma.filters.presentation.AreaViewModel
import ru.practicum.android.diploma.search.presentation.SearchState

class SelectRegionFragment : Fragment(R.layout.fragment_select_region) {
    private val binding by viewBinding(FragmentSelectRegionBinding::bind)
    private var isClickAllowed = true
    private val adapter: RegionListAdapter by lazy {
        RegionListAdapter(onItemClick = { if (clickDebounce()) passArgument(it) })
    }

    private val viewModel: AreaViewModel by viewModel()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStartOptions(true)
        initClickListeners()
        initQueryChangeListener()
        binding.rvAreaList.adapter = adapter
        binding.rvAreaList.itemAnimator = null
        viewModel.getStateLiveData().observe(viewLifecycleOwner) { renderState(it) }
        //   viewModel.test()
    }

    private fun renderState(state: SearchState) {
        when (state) {
            is SearchState.InternetError -> showError(R.drawable.er_no_internet, R.string.no_internet)
            is SearchState.ServerError -> showError(R.drawable.er_server_error, R.string.server_error)
            is SearchState.NothingFound -> showError(R.drawable.er_nothing_found, R.string.no_vacancies)
            is SearchState.SearchResult -> showResults(state.vacancies, state.found)
            is SearchState.Loading -> showLoading()
            is SearchState.Updating -> showUpdating()
        }
    }

    private fun setStartOptions(isQueryEmpty: Boolean) {
        with(binding) {
            tvFragmentTitle.text = getString(R.string.region_but_sign)
            binding.flSearch.isVisible = true
        }
    }

    private fun initClickListeners() {
        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val query = binding.etSearch.text.toString()
                if (query.isNotEmpty()) {
                    viewModel.searchOnEditorAction(query)
                }
            }
            false
        }
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
                setStartOptions(s.isEmpty())
                if (s.isEmpty()) {
                    // Если текст пустой, показываем иконку поиска
                    binding.ivSearch.setImageResource(R.drawable.ic_search)
                } else {
                    // Если текст не пустой, показываем иконку очистки
                    binding.ivSearch.setImageResource(R.drawable.ic_clear)
                }
               // viewModel.search(s.toString())
            }
        })
    }

    private fun passArgument(region: Area) {
        val action = SelectRegionFragmentDirections.actionSelectRegionFragmentToSelectPlaceFragment(region)
        findNavController().navigate(action)
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
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 100L
    }
}
