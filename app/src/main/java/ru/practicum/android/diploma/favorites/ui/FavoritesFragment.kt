package ru.practicum.android.diploma.favorites.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.domain.model.VacancyFromList
import ru.practicum.android.diploma.common.ext.viewBinding
import ru.practicum.android.diploma.common.ui.VacancyListAdapter
import ru.practicum.android.diploma.databinding.FragmentFavoritesBinding
import ru.practicum.android.diploma.favorites.presentation.FavoritesState
import ru.practicum.android.diploma.favorites.presentation.FavoritesViewModel

class FavoritesFragment : Fragment(R.layout.fragment_favorites) {
    private val viewModel: FavoritesViewModel by viewModel()
    private val binding by viewBinding(FragmentFavoritesBinding::bind)
    private var favoritesAdapter: VacancyListAdapter? = null
    private var isClickAllowed = true

    companion object {
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 100L
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favoritesAdapter = VacancyListAdapter(onItemClick = { if (clickDebounce()) openVacancy(it.id) })
        binding.rvFoundVacanciesList.adapter = favoritesAdapter
        viewModel.fillData()
        viewModel.observeState().observe(viewLifecycleOwner) { render(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        favoritesAdapter = null
        binding.rvFoundVacanciesList.adapter = null
    }

    override fun onResume() {
        super.onResume()
        viewModel.fillData()
    }

    private fun render(state: FavoritesState) {
        when (state) {
            is FavoritesState.Empty -> showEmpty()
            is FavoritesState.Content -> showContent(state.tracks)
            is FavoritesState.Error -> showNothingFound()
        }
    }

    private fun showEmpty() {
        binding.apply {
            rvFoundVacanciesList.visibility = View.GONE
            ivErrorImage.visibility = View.VISIBLE
            ivErrorImage.setImageResource(R.drawable.empty_list)
            tvErrorText.visibility = View.VISIBLE
            tvErrorText.text = requireContext().getText(R.string.list_is_empty)
        }
    }

    private fun showNothingFound() {
        binding.apply {
            rvFoundVacanciesList.visibility = View.GONE
            ivErrorImage.visibility = View.VISIBLE
            ivErrorImage.setImageResource(R.drawable.er_nothing_found)
            tvErrorText.visibility = View.VISIBLE
            tvErrorText.text = requireContext().getText(R.string.no_vacancies)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showContent(vacancies: List<VacancyFromList>) {
        binding.rvFoundVacanciesList.visibility = View.VISIBLE
        binding.ivErrorImage.visibility = View.GONE
        binding.tvErrorText.visibility = View.GONE
        favoritesAdapter?.submitList(vacancies)
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

    private fun openVacancy(vacancyId: Long) {
        val action = FavoritesFragmentDirections.actionFavoritesFragmentToVacancyFragment(vacancyId)
        findNavController().navigate(action)
    }
}
