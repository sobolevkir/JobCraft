package ru.practicum.android.diploma.search.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.domain.model.VacancyFromList
import ru.practicum.android.diploma.databinding.VacancyListItemBinding

class SearchViewHolder(private val binding: VacancyListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val radius: Float = 2 * itemView.resources.displayMetrics.density
    fun bind(model: VacancyFromList) = with(binding) {
        tvVacancyName.text = model.name
        tvEmployment.text = model.areaName
        tvSalary.text = model.salary ?: "Зарплата не указана"
        Glide.with(itemView).load(model.employerLogoUrl240).centerCrop()
            .transform(RoundedCorners(radius.toInt())).placeholder(
                R.drawable.ic_cover_placeholder
            ).into(ivVacancyCover)
    }
}
