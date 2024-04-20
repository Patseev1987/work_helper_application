package ru.bogdan.patseev_diploma.presenter.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import ru.bogdan.m17_recyclerview.data.ApiFactory
import ru.bogdan.m17_recyclerview.data.ApiHelperImpl
import ru.bogdan.patseev_diploma.MyApplication
import ru.bogdan.patseev_diploma.domain.models.StorageRecord
import ru.bogdan.patseev_diploma.domain.models.enums.ToolType
import ru.bogdan.patseev_diploma.presenter.states.RecycleViewState


class RecycleViewViewModel(private val application: MyApplication) : ViewModel() {


    private val apiHelper = ApiHelperImpl(ApiFactory.apiService)

    val state: Flow<RecycleViewState> = apiHelper
        .loadStorageRecordByWorkerId(application.worker.id)
        .map {
            RecycleViewState.Result(it) as RecycleViewState
        }
        .onStart { RecycleViewState.Loading }
        .stateIn(
            scope = viewModelScope,
            initialValue = RecycleViewState.Loading,
            started = SharingStarted.Lazily
        )


    fun getList(records:List<StorageRecord> ,position: Int): List<StorageRecord> {
        when (position) {
            0 -> {
                return records.filter { it.tool.type == ToolType.CUTTING }
            }

            1 -> {
                return records.filter { it.tool.type == ToolType.MEASURE }
            }

            2 -> {
                return records.filter { it.tool.type == ToolType.HELPERS }
            }

            else -> {
                throw RuntimeException("Unknown position in ViewPager")
            }
        }
    }
}

