package ru.practicum.android.diploma.filters.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.practicum.android.diploma.common.domain.model.VacancyFromList
import ru.practicum.android.diploma.databinding.VacancyListItemBinding
import ru.practicum.android.diploma.filters.domain.model.Area

class RegionListAdapter(
    private val onItemClick: (Area) -> Unit
) : ListAdapter<Area, RegionListViewHolder>(RegionItemComparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegionListViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return RegionListViewHolder(RegionsListItemBinding.inflate(layoutInspector, parent, false))
    }

    override fun onBindViewHolder(holder: RegionListViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }
}
