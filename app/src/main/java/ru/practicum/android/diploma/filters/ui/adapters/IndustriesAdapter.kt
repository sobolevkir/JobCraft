package ru.practicum.android.diploma.filters.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.IndustryListItemBinding
import ru.practicum.android.diploma.databinding.VacancyListItemBinding
import ru.practicum.android.diploma.filters.domain.model.Industry

class IndustriesAdapter(
    private val onItemSelect: (Industry) -> Unit
): ListAdapter<Industry, IndustriesViewHolder>(IndustryItemComparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndustriesViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return IndustriesViewHolder(IndustryListItemBinding.inflate(layoutInspector, parent, false))
    }

    override fun onBindViewHolder(holder: IndustriesViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item.name)
        holder.itemView.setOnClickListener{
            onItemSelect(item)
        }
    }
}

class IndustriesViewHolder(private val binding: IndustryListItemBinding):
    RecyclerView.ViewHolder(binding.root){

        fun bind(name: String){
            binding.tvIndustry.text = name
        }
    }
