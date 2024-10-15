package ru.practicum.android.diploma.filters.ui.adapters

import androidx.recyclerview.widget.DiffUtil
import ru.practicum.android.diploma.filters.domain.model.Industry

class IndustryItemComparator : DiffUtil.ItemCallback<Industry>()  {
    override fun areItemsTheSame(oldItem: Industry, newItem: Industry): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Industry, newItem: Industry): Boolean {
        return oldItem == newItem
    }
}
