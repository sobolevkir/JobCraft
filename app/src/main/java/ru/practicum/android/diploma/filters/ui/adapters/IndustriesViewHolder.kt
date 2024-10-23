package ru.practicum.android.diploma.filters.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.IndustryListItemBinding
import ru.practicum.android.diploma.filters.ui.IndustryForUi

class IndustriesViewHolder(private val binding: IndustryListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(model: IndustryForUi, onItemSelect: (IndustryForUi) -> Unit) {
        with(binding.rbIndustryItemSelect) {
            text = model.name
            isChecked = model.isSelected
        }
        binding.industryListItem.setOnClickListener {
            if (!model.isSelected) {
                onItemSelect(model)
            }
        }
    }
}
