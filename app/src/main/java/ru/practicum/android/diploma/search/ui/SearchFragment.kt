package ru.practicum.android.diploma.search.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.content.ContextCompat
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
            adapter.submitList(it.vacancies)
            setError(it.code, it.vacancies.size)
        }

        fun searchDebounce() {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.search(binding.etSearchRequest.text.toString())
                delay(CLICK_DELAY)
                if (isSearch) {
                    adapter.submitList(listOf())
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

    private fun setError(code: Int, count: Int) {
        when (code) {
            200 -> {
                with(binding) {
                    tvSearchResultMessage.text = getCountResource(count)
                    rvFoundVacanciesList.isVisible = true
                    tvError.isVisible = false
                }
            }

            400 -> {
                binding.tvSearchResultMessage.isVisible = false
                binding.tvError.isVisible = true
                bindErrorImage(ContextCompat.getDrawable(requireContext(), R.drawable.er_server_error))
            }

            401 -> {
                with(binding){
                    tvSearchResultMessage.isVisible = false
                    tvError.isVisible = true
                    tvError.text = getString(R.string.no_internet)
                }
                bindErrorImage(ContextCompat.getDrawable(requireContext(), R.drawable.er_no_internet))
            }
        }
    }

    private fun bindErrorImage(image: Drawable?) {
        binding.tvError.setCompoundDrawables(null, image, null, null)
    }

    private fun setStartOptions() {
        //Показать начальную картинку
        val isEmpty = binding.etSearchRequest.text.isEmpty()
        binding.tvError.isVisible = isEmpty
        binding.tvSearchResultMessage.isVisible = false
        bindErrorImage(ContextCompat.getDrawable(requireContext(), R.drawable.vacancy_search_start))

        //Показать кнопку поиска или очистки
        with(binding) {
            ivClearRequest.isVisible = !isEmpty
            ivSearch.isVisible = isEmpty
        }
    }

    private fun getCountResource(count: Int): String {
        val countLast = count % 10
        if (count == 0) {
            bindErrorImage(ContextCompat.getDrawable(requireContext(), R.drawable.er_nothing_found))
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
