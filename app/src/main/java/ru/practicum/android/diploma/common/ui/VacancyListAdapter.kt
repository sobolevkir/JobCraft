package ru.practicum.android.diploma.common.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.common.domain.model.VacancyFromList
import ru.practicum.android.diploma.databinding.VacancyListItemBinding

class VacancyListAdapter(
    private val onItemClick: (VacancyFromList) -> Unit,
    private val onItemLongClick: ((VacancyFromList) -> Unit)? = null
) : RecyclerView.Adapter<VacancyListViewHolder>() {

    val vacancies = mutableListOf<VacancyFromList>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacancyListViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        val binding = VacancyListItemBinding.inflate(layoutInspector, parent, false)
        return VacancyListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VacancyListViewHolder, position: Int) {
        with(holder) {
            bind(vacancies[position])
            binding.root.setOnClickListener { onItemClick(vacancies[position]) }
            binding.root.setOnLongClickListener {
                onItemLongClick?.invoke(vacancies[position])?.let { true } ?: false
            }
        }
    }

    override fun getItemCount() = vacancies.size

}
