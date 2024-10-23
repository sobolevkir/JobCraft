package ru.practicum.android.diploma.common.ui

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.domain.model.VacancyFromList
import ru.practicum.android.diploma.databinding.VacancyListItemBinding

class VacancyListViewHolder(private val binding: VacancyListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val cornerRadius = binding.root.resources.getDimensionPixelSize(R.dimen.radius_small)
    fun bind(model: VacancyFromList) = with(binding) {
        tvVacancyName.text = model.name
        tvEmployment.text = model.areaName
        tvSalary.text = model.salary
        Glide.with(itemView)
            .load(model.employerLogoUrl240)
            .centerCrop()
            .transform(RoundedCorners(cornerRadius))
            .placeholder(R.drawable.ic_cover_placeholder)
            .into(ivVacancyCover)
    }
}
