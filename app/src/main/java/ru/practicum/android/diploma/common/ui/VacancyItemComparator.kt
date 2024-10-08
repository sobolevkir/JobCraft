package ru.practicum.android.diploma.common.ui

import androidx.recyclerview.widget.DiffUtil
import ru.practicum.android.diploma.common.domain.model.VacancyFromList

class VacancyItemComparator : DiffUtil.ItemCallback<VacancyFromList>() {
    override fun areItemsTheSame(oldItem: VacancyFromList, newItem: VacancyFromList): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: VacancyFromList, newItem: VacancyFromList): Boolean {
        return oldItem == newItem
    }

}
