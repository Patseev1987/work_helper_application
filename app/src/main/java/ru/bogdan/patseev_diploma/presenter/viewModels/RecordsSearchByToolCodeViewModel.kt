package ru.bogdan.patseev_diploma.presenter.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import ru.bogdan.patseev_diploma.MyApplication
import ru.bogdan.patseev_diploma.R
import ru.bogdan.patseev_diploma.domain.models.Worker
import ru.bogdan.patseev_diploma.domain.models.enums.WorkerType
import ru.bogdan.patseev_diploma.domain.useCases.LoadStorageRecordByToolCodeInDepartmentUseCase
import ru.bogdan.patseev_diploma.domain.useCases.LoadStorageRecordByToolCodeUseCase
import ru.bogdan.patseev_diploma.domain.useCases.LoadWorkersByDepartmentUseCase
import ru.bogdan.patseev_diploma.presenter.states.RecordsSearchByToolCodeState
import ru.bogdan.patseev_diploma.presenter.states.RecycleViewWorkerState
import ru.bogdan.patseev_diploma.util.HTTP_406
import ru.bogdan.patseev_diploma.util.TokenBundle
import javax.inject.Inject

@OptIn(FlowPreview::class)
class RecordsSearchByToolCodeViewModel @Inject constructor(
    private val application: MyApplication,
    private val loadStorageRecordByToolCodeInDepartmentUseCase:
    LoadStorageRecordByToolCodeInDepartmentUseCase,
    private val loadStorageRecordByToolCodeUseCase: LoadStorageRecordByToolCodeUseCase,
    private val navController: NavController
) : ViewModel() {

    val tokenBundle = TokenBundle(application)

    val searchString: MutableStateFlow<String> = MutableStateFlow("")

    private val _state: MutableStateFlow<RecordsSearchByToolCodeState> =
        MutableStateFlow(RecordsSearchByToolCodeState.Loading)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            searchString
                .debounce(500)
                .collect { toolCode ->
                    loadingRecords(toolCode)
                }
        }
    }

    private fun loadingRecords(toolCode: String) {
        viewModelScope.launch {
            try {
                _state.value = RecordsSearchByToolCodeState.Loading
                val records = if (tokenBundle.getWorkerType() == WorkerType.WORKER)
                    loadStorageRecordByToolCodeInDepartmentUseCase(
                        tokenBundle.getToken(),
                        toolCode
                    )
                else loadStorageRecordByToolCodeUseCase(
                    tokenBundle.getToken(),
                    toolCode
                )
                _state.value = RecordsSearchByToolCodeState.Result(
                    records = records
                )
            } catch (e: retrofit2.HttpException) {
                if (e.message?.trim() == HTTP_406) {
                    tokenBundle.returnToLoginFragment(
                        navController,
                        R.id.action_recordSearchByToolCodeFragment_to_loginFragment
                    )
                }
            } catch (e: Exception) {
                _state.value = RecordsSearchByToolCodeState.ConnectionProblem(
                    application.getString(
                        R.string
                            .server_doesn_t_respond_try_again_a_little_bit_later
                    )
                )
            }
        }
    }
}