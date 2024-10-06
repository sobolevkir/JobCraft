package ru.practicum.android.diploma.search.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.practicum.android.diploma.databinding.VacancyListItemBinding
import ru.practicum.android.diploma.search.domain.api.VacancyOnClicked
import ru.practicum.android.diploma.search.domain.model.VacancyListItem

class SearchAdapter(
    private val vacancyOnClicked: VacancyOnClicked
) : ListAdapter<VacancyListItem, SearchViewHolder>(VacancyItemComparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return SearchViewHolder(VacancyListItemBinding.inflate(layoutInspector, parent, false))
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener {
            vacancyOnClicked.startVacancy()
        }
    }
}
