package ru.practicum.android.diploma.filters.ui

import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.filters.domain.model.Area

class RegionListViewHolder(private val binding: RegionsListItemBinding) :  //
    RecyclerView.ViewHolder(binding.root) {
    fun bind(model: Area) = with(binding) {
        tvVacancyName.text = model.name  //
    }
}
