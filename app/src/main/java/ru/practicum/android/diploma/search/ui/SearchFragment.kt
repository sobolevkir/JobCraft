package ru.practicum.android.diploma.search.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
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
            adapter.submitList(it.vacancies)
        }

        fun searchDebounce(){
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.search(binding.etSearchRequest.text.toString())
                delay(CLICK_DELAY)
                if(isSearch) {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }

        binding.etSearchRequest.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {}

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                isSearch = s.isNotEmpty()
                setStartOptions()
                searchDebounce()
            }
        })

        binding.ivClearRequest.setOnClickListener{
            binding.etSearchRequest.setText("")
            setStartOptions()
        }

        binding.btnFilters.setOnClickListener {
            openFilters()
        }
    }

    private fun setStartOptions(){
        //Показать начальную картинку
        val isEmpty = binding.etSearchRequest.text.isEmpty()
        with(binding.tvError){
            isVisible = isEmpty
            val errorImage =
                ContextCompat.getDrawable(requireContext(), R.drawable.vacancy_search_start)
            setCompoundDrawables(null, errorImage, null, null)
        }

        //Показать кнопку поиска или очистки
        with(binding){
            if (isEmpty){
                ivClearRequest.isVisible = false
                ivSearch.isVisible = true
            }
            else{
                ivClearRequest.isVisible = true
                ivSearch.isVisible = false
            }
        }

        viewModel.searchTest()

    }

    private fun openFilters() {
        val action = SearchFragmentDirections.actionSearchFragmentToFiltersFragment()
        findNavController().navigate(action)
    }

    companion object{
        const val CLICK_DELAY = 2000L
    }
}
