package ru.bogdan.patseev_diploma.presenter.viewModels

import androidx.lifecycle.ViewModel
import ru.bogdan.patseev_diploma.MyApplication
import ru.bogdan.patseev_diploma.R

class TabLayoutVieModel(private val application: MyApplication) : ViewModel() {
    val tabNames = listOf(
        application.getString(R.string.cutting_tools),
        application.getString(R.string.measuring_tools),
        application.getString(R.string.helper_tools)
    )
}