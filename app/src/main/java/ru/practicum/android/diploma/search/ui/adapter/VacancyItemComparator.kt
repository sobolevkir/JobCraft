package ru.practicum.android.diploma.search.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import ru.practicum.android.diploma.search.domain.model.Vacancy

class VacancyItemComparator : DiffUtil.ItemCallback<Vacancy>() {
    override fun areItemsTheSame(oldItem: Vacancy, newItem: Vacancy): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Vacancy, newItem: Vacancy): Boolean {
        return oldItem == newItem
    }

}
