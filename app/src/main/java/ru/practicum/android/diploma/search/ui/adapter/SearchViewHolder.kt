package ru.practicum.android.diploma.search.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.domain.model.Salary
import ru.practicum.android.diploma.common.domain.model.VacancyFromList
import ru.practicum.android.diploma.databinding.VacancyListItemBinding

class SearchViewHolder(private val binding: VacancyListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val radius: Float = 2 * itemView.resources.displayMetrics.density
    fun bind(model: VacancyFromList) = with(binding) {
        tvVacancyName.text = model.name
        tvEmployment.text = model.areaName
        tvSalary.text = getSalary(model.salary)
        Glide.with(itemView).load(model.employerLogoUrl240).centerCrop()
            .transform(RoundedCorners(radius.toInt())).placeholder(
                R.drawable.cover_border
            ).into(ivVacancyCover)
    }

    private fun getSalary(salary: Salary?):String{
        return if(salary == null){
            "Зарплата не указана"
        }
        else if(salary.currency != null){
            "${salary.currency}"
        }
        else if(salary.from != null && salary.to != null){
            "От ${salary.from} до ${salary.to}"
        }
        else if(salary.from != null){
            "От ${salary.from}"
        }
        else{
            "до ${salary.to}"
        }
    }
}
