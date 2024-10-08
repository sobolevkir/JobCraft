package ru.practicum.android.diploma.vacancy.ui

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.TypedValue
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.domain.model.VacancyDetails
import ru.practicum.android.diploma.common.ext.viewBinding
import ru.practicum.android.diploma.databinding.FragmentVacancyBinding
import ru.practicum.android.diploma.vacancy.presentation.VacancyViewModel
import ru.practicum.android.diploma.vacancy.ui.model.ScreenMode

private const val CORNERRADIUS_DP = 12f

class VacancyFragment : Fragment(R.layout.fragment_vacancy) {

    companion object {
        private const val EXTRA_ID_VACANCY = "id_vacancy"                     // Тег для сохранения позиции таймера
    }

    private val viewModel by viewModel<VacancyViewModel>()
    private val binding by viewBinding(FragmentVacancyBinding::bind)
    private var vacancy: VacancyDetails? = null
    private var isFavorite: Boolean = false
    private var vacancyId = 0L


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.getVacancyLiveData().observe(viewLifecycleOwner) { newState ->
            vacancy = newState.vacancy
            when (newState.screenMode) {
                ScreenMode.LOADING ->  showLoading()
                ScreenMode.RESULTS ->  showVacancy(newState.vacancy!!)
                ScreenMode.ERROR -> showPlaceholder()
            }
        }

        vacancyId = requireArguments().getLong(EXTRA_ID_VACANCY, 0)

        viewModel.getVacancyDetails(vacancyId)

        binding.btnBack.setOnClickListener { findNavController().popBackStack() }
        binding.btnSend.setOnClickListener { sendVacancy() }
        binding.btnFavorite.setOnClickListener {
            if (vacancy != null) {
                isFavorite = !isFavorite
                changeFavoriteIcon()
                if (isFavorite) addToFavorites(vacancy!!) else removeFromFavorites(vacancyId)
            }
        }
    }
    // Отправка вакансии
    private fun sendVacancy () {
        if (vacancy != null) {
            viewModel.sendVacancy(vacancy!!.alternateUrl)
        }
    }

    private fun addToFavorites (vacancy: VacancyDetails) {
        viewModel.addToFavorites(vacancy)
    }

    private fun removeFromFavorites (vacancyId: Long) {
        viewModel.removeFromFavorites(vacancyId)
    }

    private fun showLoading() {
        binding.svVacancy.isVisible = false
        binding.llPlaceholder.isVisible = false
        binding.progressBar.isVisible = true
    }

    private fun showVacancy (vacancy: VacancyDetails) {
        binding.svVacancy.isVisible = true
        binding.llPlaceholder.isVisible = false
        binding.progressBar.isVisible = false
        bind(vacancy)
    }

    private fun showPlaceholder () {
        binding.svVacancy.isVisible = false
        binding.llPlaceholder.isVisible = true
        binding.progressBar.isVisible = false
    }

    private fun bind (vacancy: VacancyDetails) {

        isFavorite = vacancy.isFavorite
        changeFavoriteIcon()

        binding.tvVacancyName.text = vacancy.name

        if(vacancy.salary == null) {
            binding.salary.isVisible = false
        } else {
            binding.salary.isVisible = true
            binding.salary.text = vacancy.salary
        }

        Glide.with(requireContext())
            .load(vacancy.employerLogoUrl240)
            .placeholder(R.drawable.ic_cover_placeholder)
            .centerCrop()
            .transform(
                RoundedCorners(
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        CORNERRADIUS_DP,
                        requireContext().resources.displayMetrics
                    ).toInt()
                )
            )
            .into(binding.ivLogo)

        binding.tvEmployerName.text = vacancy.employerName
        binding.tvAddress.text = if (vacancy.address != null) vacancy.address else vacancy.areaName
        if (vacancy.experience == null) {
            binding.tvExperience.isVisible = false
            binding.tvExperienceTitle.isVisible = false
        } else {
            binding.tvExperience.isVisible = true
            binding.tvExperienceTitle.isVisible = true
            binding.tvExperience.text = vacancy.experience
        }
        if (vacancy.scheduleName == null) {
            binding.tvScheduleName.isVisible = false
        } else {
            binding.tvScheduleName.isVisible = true
            binding.tvScheduleName.setText(Html.fromHtml(vacancy.scheduleName, Html.FROM_HTML_MODE_COMPACT))
        }
        binding.tvDescription.setText(Html.fromHtml(vacancy.description, Html.FROM_HTML_MODE_COMPACT))

        if (vacancy.keySkills == null) {
            binding.tvKeySkillsTitle.isVisible = false
            binding.tvKeySkills.isVisible = false
        } else {
            binding.tvKeySkillsTitle.isVisible = true
            binding.tvKeySkills.isVisible = true
            binding.tvKeySkills.setText(Html.fromHtml(formattingKeySkills(vacancy.keySkills), Html.FROM_HTML_MODE_COMPACT))
        }
    }

    private fun formattingKeySkills (keySkills: List<String>): String {
        val builder = StringBuilder()
        for (s: String in keySkills) builder.append("<li>" + s + "</li>")
        return builder.toString()
    }

    // Иконка Избранное
    private fun changeFavoriteIcon () {
        if (isFavorite) {
            binding.btnFavorite.setImageResource(R.drawable.ic_favorite_on)
        } else {
            binding.btnFavorite.setImageResource(R.drawable.ic_favorite_off)
        }
    }

    /*
    // Формируем строку зарплата
    private fun getSalary (salary: Salary): String {
        var strFrom = ""
        var strTo = ""
        var strCurrency = ""
        if (salary.from != null) strFrom = "от " + salary.from.toString()
        if (salary.to != null) strTo = " до " + salary.to.toString()
        if (salary.currency != null) strCurrency = " " + salary.currency

        return (strFrom + strTo + strCurrency).trimStart()
    }*/

    /*// Формируем строку адрес
    private fun getAddress (address: Address): String {
        var strCity = ""
        var strStreet = ""
        if (address.city != null) strCity = address.city + ", "
        if (address.street != null) {
            strStreet = address.street
            if (address.building != null) strStreet += ", " + address.building
        }

        return strCity + strStreet
    }*/
}
