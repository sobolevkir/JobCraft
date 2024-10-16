package ru.practicum.android.diploma.filters.ui.adapters

import androidx.recyclerview.widget.DiffUtil
import ru.practicum.android.diploma.filters.domain.model.Industry
import ru.practicum.android.diploma.filters.ui.IndustryForUi

class IndustryItemComparator : DiffUtil.ItemCallback<IndustryForUi>() {
    override fun areItemsTheSame(oldItem: IndustryForUi, newItem: IndustryForUi): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: IndustryForUi, newItem: IndustryForUi): Boolean {
        return oldItem == newItem
    }
}
