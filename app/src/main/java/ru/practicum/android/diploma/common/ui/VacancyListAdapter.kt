package ru.practicum.android.diploma.common.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.practicum.android.diploma.common.domain.model.VacancyFromList
import ru.practicum.android.diploma.databinding.VacancyListItemBinding

class VacancyListAdapter(
    private val onItemClick: (VacancyFromList) -> Unit
) : ListAdapter<VacancyFromList, VacancyListViewHolder>(VacancyItemComparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacancyListViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return VacancyListViewHolder(VacancyListItemBinding.inflate(layoutInspector, parent, false))
    }

    override fun onBindViewHolder(holder: VacancyListViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onItemClick
        }
    }
}
