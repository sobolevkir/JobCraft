package ru.practicum.android.diploma.search.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import ru.practicum.android.diploma.search.domain.model.VacancyListItem

class VacancyItemComparator : DiffUtil.ItemCallback<VacancyListItem>() {
    override fun areItemsTheSame(oldItem: VacancyListItem, newItem: VacancyListItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: VacancyListItem, newItem: VacancyListItem): Boolean {
        return oldItem == newItem
    }

}
