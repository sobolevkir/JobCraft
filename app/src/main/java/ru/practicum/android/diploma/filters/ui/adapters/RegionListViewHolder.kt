package ru.practicum.android.diploma.filters.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.RegionListItemBinding
import ru.practicum.android.diploma.filters.domain.model.Area

class RegionListViewHolder(private val binding: RegionListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(model: Area) = with(binding) {
        tvCountryOrRegion.text = model.name
    }
}
