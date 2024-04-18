package ru.bogdan.patseev_diploma.presenter.viewModels

import androidx.lifecycle.ViewModel
import ru.bogdan.patseev_diploma.domain.models.enums.ToolType
import java.net.Proxy

class TabLayoutVieModel:ViewModel() {
    val tabNames = listOf(ToolType.CUTTING.type, ToolType.MEASURE.type, ToolType.HELPERS.type)
}