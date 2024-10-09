package ru.practicum.android.diploma.common.ui

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.domain.model.VacancyFromList
import ru.practicum.android.diploma.databinding.VacancyListItemBinding

class VacancyListViewHolder(val binding: VacancyListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(vacancy: VacancyFromList) {
        binding.tvVacancyName.text = vacancy.name
        binding.tvEmployment.text = vacancy.areaName
        binding.tvSalary.text = vacancy.salary.toString()
        val cornerRadius = binding.root.resources.getDimensionPixelSize(R.dimen.radius_small)
        Glide.with(binding.root)
            .load(vacancy.employerLogoUrl240)
            .centerCrop()
            .placeholder(R.drawable.ic_cover_placeholder)
            .transform(RoundedCorners(cornerRadius))
            .into(binding.ivVacancyCover)
    }

}
