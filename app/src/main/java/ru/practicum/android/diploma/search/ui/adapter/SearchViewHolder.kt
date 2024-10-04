package ru.practicum.android.diploma.search.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.VacancyListItemBinding
import ru.practicum.android.diploma.search.domain.model.Vacancy

class SearchViewHolder(private val binding: VacancyListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val radius: Float = 2 * itemView.resources.displayMetrics.density
    fun bind(model: Vacancy) = with(binding) {
        tvVacancyName.text = model.vacancyName
        tvEmployment.text = model.region
        tvSalary.text = model.salary
        Glide.with(itemView).load(model.employerLogoUrl).centerCrop()
            .transform(RoundedCorners(radius.toInt())).placeholder(
                R.drawable.cover_border //Временная заглушка
            ).into(ivVacancyCover)
    }
}
