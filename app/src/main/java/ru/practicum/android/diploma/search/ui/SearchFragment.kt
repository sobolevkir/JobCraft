package ru.practicum.android.diploma.search.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.domain.model.VacancyFromList
import ru.practicum.android.diploma.common.ext.hideKeyboard
import ru.practicum.android.diploma.common.ext.viewBinding
import ru.practicum.android.diploma.common.presentation.FilterParametersViewModel
import ru.practicum.android.diploma.common.ui.VacancyListAdapter
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.search.presentation.SearchState
import ru.practicum.android.diploma.search.presentation.SearchViewModel

class SearchFragment : Fragment(R.layout.fragment_search) {
    private val binding by viewBinding(FragmentSearchBinding::bind)
    private val searchViewModel by viewModel<SearchViewModel>()
    private val filterParametersViewModel: FilterParametersViewModel by navGraphViewModels(R.id.root_navigation_graph)
    private var isClickAllowed = true
    private val adapter: VacancyListAdapter by lazy {
        VacancyListAdapter(onItemClick = { if (clickDebounce()) openVacancy(it.id) })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        filterParametersViewModel.getFiltersAppliedLiveEvent().observe(viewLifecycleOwner) { isFiltersApplied ->
            searchViewModel.applyFilters()
        }
        setStartOptions(true)
        initClickListeners()
        initQueryChangeListener()
        initScrollListener()
        binding.rvFoundVacanciesList.adapter = adapter
        binding.rvFoundVacanciesList.itemAnimator = null
        searchViewModel.getStateLiveData().observe(viewLifecycleOwner) { renderState(it) }

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

    private fun initScrollListener() {
        binding.rvFoundVacanciesList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > ZERO) {
                    activity?.hideKeyboard()
                    binding.etSearchRequest.clearFocus()
                    val pos =
                        (binding.rvFoundVacanciesList.layoutManager as LinearLayoutManager)
                            .findLastVisibleItemPosition()
                    val itemsCount = adapter.itemCount
                    if (pos >= itemsCount - 1) {
                        searchViewModel.onLastItemReached()
                    }
                }
            }
        })
    }

    private fun initQueryChangeListener() {
        binding.etSearchRequest.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                // Empty
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Empty
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setStartOptions(s.isEmpty())
                searchViewModel.search(s.toString())
            }
        })
    }

    private fun initClickListeners() {
        binding.etSearchRequest.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val query = binding.etSearchRequest.text.toString()
                if (query.isNotEmpty()) {
                    searchViewModel.newSearch(query)
                }
            }
            false
        }
        binding.ivClearRequest.setOnClickListener {
            binding.etSearchRequest.setText("")
            setStartOptions(true)
        }
        binding.btnFilters.setOnClickListener {
            openFilters()
        }
    }

    private fun showLoading() {
        with(binding) {
            llError.isVisible = false
            progressBar.isVisible = true
            clSearchResult.isVisible = false
        }
    }

    private fun showUpdating() {
        with(binding) {
            llError.isVisible = false
            progressBar.isVisible = false
            clSearchResult.isVisible = true
            rvFoundVacanciesList.isVisible = true
            tvSearchResultMessage.isVisible = true
            loadMoreProgressBar.isVisible = true
        }
    }

    private fun showResults(vacancies: List<VacancyFromList>, foundNumber: Int) {
        adapter.submitList(vacancies) {
            with(binding) {
                loadMoreProgressBar.isVisible = false
                llError.isVisible = false
                progressBar.isVisible = false
                tvSearchResultMessage.text = binding.root.context.resources.getQuantityString(
                    R.plurals.vacancies_count,
                    foundNumber,
                    foundNumber
                )
                clSearchResult.isVisible = true
                rvFoundVacanciesList.isVisible = true
                tvSearchResultMessage.isVisible = true
            }
        }
    }

    private fun showError(image: Int, text: Int? = null, messageState: Boolean = false) {
        with(binding) {
            llError.isVisible = true
            progressBar.isVisible = false
            clSearchResult.isVisible = true
            rvFoundVacanciesList.isVisible = false
            ivSearchResult.isVisible = true
            loadMoreProgressBar.isVisible = false
            ivSearchResult.setImageResource(image)
            tvSearchResultMessage.isVisible = messageState
            tvSearchResultMessage.setText(R.string.no_found_vacancies)
            if (text == null) {
                tvError.text = ""
            } else {
                tvError.setText(text)
            }
        }
    }

    private fun setStartOptions(isQueryEmpty: Boolean) {
        if (isQueryEmpty) {
            showError(R.drawable.vacancy_search_start, null)
        }

        with(binding) {
            ivClearRequest.isVisible = !isQueryEmpty
            ivSearch.isVisible = isQueryEmpty
        }
    }

    private fun openFilters() {
        val action = SearchFragmentDirections.actionSearchFragmentToFiltersFragment()
        findNavController().navigate(action)
    }

    private fun openVacancy(vacancyId: Long) {
        val action = SearchFragmentDirections.actionSearchFragmentToVacancyFragment(vacancyId)
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
        private const val ZERO = 0
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 100L
    }
}
