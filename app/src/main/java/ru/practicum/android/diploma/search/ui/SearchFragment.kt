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
import ru.practicum.android.diploma.common.ext.viewBinding
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.search.domain.api.VacancyOnClicked
import ru.practicum.android.diploma.search.presentation.SearchViewModel
import ru.practicum.android.diploma.search.ui.adapter.SearchAdapter


class SearchFragment : Fragment(R.layout.fragment_search) {
    private val binding by viewBinding(FragmentSearchBinding::bind)
    private val viewModel by viewModel<SearchViewModel>()

    private var isSearch = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setStartOptions()

        val vacancyOnClicked = object : VacancyOnClicked {
            override fun startVacancy() {
                TODO("Not yet implemented")
            }
        }

        val adapter = SearchAdapter(vacancyOnClicked)
        adapter.submitList(listOf())
        binding.rvFoundVacanciesList.adapter = adapter

        viewModel.getSearchRes().observe(requireActivity()) {
            binding.progressBar.isVisible = false
            binding.rvFoundVacanciesList.isVisible = true
            binding.tvSearchResultMessage.text = getCountResource(it.count)
            adapter.submitList(it.vacancies)
            setError(it.code)
        }

        fun searchDebounce() {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.search(binding.etSearchRequest.text.toString())
                delay(CLICK_DELAY)
                if (isSearch) {
                    adapter.submitList(listOf())
                    showError(true)
                    binding.progressBar.isVisible = true
                }
            }
        }

        binding.etSearchRequest.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                isSearch = s.isNotEmpty()
                setStartOptions()
                searchDebounce()
            }
        })

        binding.rvFoundVacanciesList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0) {
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
            setStartOptions()
        }

        binding.btnFilters.setOnClickListener {
            openFilters()
        }
    }

    private fun setError(code: Int) {
        when (code) {
            200 -> {
                showError(false)
            }

            401 -> {
                showError(true)
                bindErrorImage(R.drawable.er_no_internet, R.string.no_internet)
            }

            402 -> {
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

    private fun setStartOptions() {
        //Показать начальную картинку
        val isEmpty = binding.etSearchRequest.text.isEmpty()
        showError(isEmpty)
        bindErrorImage(R.drawable.vacancy_search_start, null)

        //Показать кнопку поиска или очистки
        with(binding) {
            ivClearRequest.isVisible = !isEmpty
            ivSearch.isVisible = isEmpty
        }
    }

    private fun getCountResource(count: Int): String {
        binding.tvSearchResultMessage.isVisible = true
        val countLast = count % 10
        if (count == 0) {
            showError(true)
            bindErrorImage(R.drawable.er_nothing_found, null)
            return "Таких вакансий нет"
        }
        return when {
            count == 11 or 12 or 13 or 14 -> "Найдено $count вакансий"
            countLast == 1 -> "Найдено $count вакансия"
            countLast == 2 or 3 or 4 -> "Найдено $count вакансии"
            else -> "Найдено $count вакансий"
        }
    }

    private fun openFilters() {
        val action = SearchFragmentDirections.actionSearchFragmentToFiltersFragment()
        findNavController().navigate(action)
    }

    companion object {
        const val CLICK_DELAY = 2000L
    }
}
