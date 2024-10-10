package ru.practicum.android.diploma.search.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.domain.model.VacancyFromList
import ru.practicum.android.diploma.common.ext.viewBinding
import ru.practicum.android.diploma.common.ui.VacancyListAdapter
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.search.presentation.SearchState
import ru.practicum.android.diploma.search.presentation.SearchViewModel

class SearchFragment : Fragment(R.layout.fragment_search) {
    private val binding by viewBinding(FragmentSearchBinding::bind)
    private val viewModel by viewModel<SearchViewModel>()

    // Инициализация адаптера при объявлении
    private val adapter: VacancyListAdapter by lazy {
        VacancyListAdapter(onItemClick = { if (clickDebounce()) startVacancy(it.id) })
    }

    private var isClickAllowed = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setStartOptions(true)

        binding.rvFoundVacanciesList.adapter = adapter
        adapter.submitList(listOf())
        viewModel.getSearchRes().observe(viewLifecycleOwner) { setError(it) }

        binding.etSearchRequest.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                // Empty
            }

            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
                // Empty
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                viewModel.setIsSearch(s.isNotEmpty())
                setStartOptions(s.isEmpty())
                viewModel.search(s.toString())
            }
        })

        binding.rvFoundVacanciesList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > ZERO) {
                    val pos =
                        (binding.rvFoundVacanciesList.layoutManager as LinearLayoutManager)
                            .findLastVisibleItemPosition()
                    val itemsCount = adapter.itemCount
                    if (pos >= itemsCount - 1) {
                        viewModel.onLastItemReached()
                    }
                }
            }
        })

        binding.ivClearRequest.setOnClickListener {
            binding.etSearchRequest.setText("")
            setStartOptions(true)
        }

        binding.btnFilters.setOnClickListener {
            openFilters()
        }
    }

    private fun setError(code: SearchState) {
        when (code) {
            is SearchState.InternetError -> setInternetError()
            is SearchState.ServerError -> setServerError()
            is SearchState.NothingFound -> setNothingFound()
            is SearchState.SearchResult -> showResults(code.vacancies, code.found)
            is SearchState.Loading -> setLoading()
        }
    }

    private fun setInternetError() {
        bindErrorImage(R.drawable.er_no_internet, R.string.no_internet)
    }

    private fun setServerError() {
        bindErrorImage(R.drawable.er_server_error, R.string.server_error)
    }

    private fun setNothingFound() {
        bindErrorImage(R.drawable.er_nothing_found, R.string.no_vacancies)
    }

    private fun setLoading() {
        binding.progressBar.isVisible = true
    }

    private fun showResults(list: List<VacancyFromList>, found: Int) {
        adapter.submitList(list)
        with(binding) {
            layoutError.isVisible = false
            rvFoundVacanciesList.isVisible = true
            tvSearchResultMessage.isVisible = true
            tvSearchResultMessage.text = binding.root.context.resources.getQuantityString(
                R.plurals.vacancies_count,
                found,
                found
            )
        }
    }

    private fun bindErrorImage(image: Int, text: Int? = null, messageState: Boolean = false) {
        with(binding) {
            layoutError.isVisible = true
            ivSearchResult.setImageResource(image)
            tvSearchResultMessage.isVisible = messageState
            progressBar.isVisible = false
            tvSearchResultMessage.setText(R.string.no_found_vacancies)
            if (text == null) {
                tvError.text = ""
            } else {
                tvError.setText(text)
            }
        }
    }

    private fun setStartOptions(isEmpty: Boolean) {
        // Показать начальную картинку
        if (isEmpty) {
            binding.layoutError.isVisible = false
            binding.ivSearchResult.isVisible = true
            bindErrorImage(R.drawable.vacancy_search_start, null)
        }

        // Показать кнопку поиска или очистки
        with(binding) {
            ivClearRequest.isVisible = !isEmpty
            ivSearch.isVisible = isEmpty
        }
    }

    private fun openFilters() {
        val action = SearchFragmentDirections.actionSearchFragmentToFiltersFragment()
        findNavController().navigate(action)
    }

    fun startVacancy(vacancyId: Long) {
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
        const val ZERO = 0
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 100L
    }
}
