package ru.practicum.android.diploma.filters.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.IndustryListItemBinding
import ru.practicum.android.diploma.filters.ui.IndustryForUi

class IndustriesAdapter(
    private val onItemSelect: (IndustryForUi) -> Unit
) : ListAdapter<IndustryForUi, IndustriesAdapter.IndustriesViewHolder>(IndustryItemComparator()) {

    inner class IndustriesViewHolder(private val binding: IndustryListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: IndustryForUi) {
            with(binding.industryItemSelect){
                text = model.name
                isChecked = model.isSelected

                setOnClickListener{
                    if (!model.isSelected) {
                        onItemSelect(model)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndustriesViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return IndustriesViewHolder(IndustryListItemBinding.inflate(layoutInspector, parent, false))
    }

    override fun onBindViewHolder(holder: IndustriesViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}
