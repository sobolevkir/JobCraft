package ru.practicum.android.diploma.search.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.ext.viewBinding
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.search.domain.api.VacancyOnClicked
import ru.practicum.android.diploma.search.presentation.SearchViewModel
import ru.practicum.android.diploma.search.ui.adapter.SearchAdapter

class SearchFragment : Fragment(R.layout.fragment_search) {
    private val binding by viewBinding(FragmentSearchBinding::bind)
    private val viewModel by viewModel<SearchViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setStartOptions(true)

        val vacancyOnClicked = object : VacancyOnClicked {
            override fun startVacancy(vacancyId: Long) {
                val action = SearchFragmentDirections.actionSearchFragmentToVacancyFragment(vacancyId)
                findNavController().navigate(action)
            }
        }

        val adapter = SearchAdapter(vacancyOnClicked)
        adapter.submitList(listOf())
        binding.rvFoundVacanciesList.adapter = adapter

        viewModel.getSearchRes().observe(requireActivity()) {
            if (it.code == ERROR_500) {
                if (it.isSearch) {
                    adapter.submitList(listOf())
                    showError(true)
                    binding.progressBar.isVisible = true
                }
            } else {
                binding.progressBar.isVisible = false
                binding.rvFoundVacanciesList.isVisible = true
                binding.tvSearchResultMessage.text = getCountResource(it.count)
                adapter.submitList(it.vacancies)
                setError(it.code)
            }
        }

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
                        (binding.rvFoundVacanciesList.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
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

    private fun setError(code: Int) {
        when (code) {
            ERROR_200 -> {
                showError(false)
            }

            ERROR_401 -> {
                showError(true)
                bindErrorImage(R.drawable.er_no_internet, R.string.no_internet)
            }

            ERROR_402 -> {
                showError(true)
                bindErrorImage(R.drawable.er_server_error, R.string.server_error)
            }
        }
    }

    private fun showError(visible: Boolean) {
        binding.layoutError.isVisible = visible
    }

    private fun bindErrorImage(image: Int, text: Int?) {
        binding.ivSearchResult.setImageResource(image)
        if (text == null) {
            binding.tvError.text = ""
        } else {
            binding.tvError.setText(text)
        }
    }

    private fun setStartOptions(isEmpty: Boolean) {
        // Показать начальную картинку
        binding.ivSearchResult.isVisible = isEmpty
        bindErrorImage(R.drawable.vacancy_search_start, null)

        // Показать кнопку поиска или очистки
        with(binding) {
            ivClearRequest.isVisible = !isEmpty
            ivSearch.isVisible = isEmpty
        }
    }

    private fun getCountResource(count: Int): String {
        binding.tvSearchResultMessage.isVisible = true
        val countLast = count % TEN
        if (count == 0) {
            showError(true)
            bindErrorImage(R.drawable.er_nothing_found, null)
            return "Таких вакансий нет"
        }
        return when {
            count == CHECK_11 or CHECK_12 or CHECK_13 or CHECK_14 -> "Найдено $count вакансий"
            countLast == CHECK_1 -> "Найдено $count вакансия"
            countLast == CHECK_2 or CHECK_3 or CHECK_4 -> "Найдено $count вакансии"
            else -> "Найдено $count вакансий"
        }
    }

    private fun openFilters() {
        val action = SearchFragmentDirections.actionSearchFragmentToFiltersFragment()
        findNavController().navigate(action)
    }

    companion object {
        const val ERROR_200 = 200
        const val ERROR_401 = 401
        const val ERROR_402 = 402
        const val ERROR_500 = 500
        const val CHECK_11 = 11
        const val CHECK_12 = 12
        const val CHECK_13 = 13
        const val CHECK_14 = 14
        const val CHECK_1 = 1
        const val CHECK_2 = 2
        const val CHECK_3 = 3
        const val CHECK_4 = 4
        const val ZERO = 0
        const val TEN = 10
    }
}
