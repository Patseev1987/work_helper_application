package ru.bogdan.patseev_diploma.presenter.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.bogdan.patseev_diploma.MyApplication
import ru.bogdan.patseev_diploma.R
import ru.bogdan.patseev_diploma.domain.models.enums.Department
import ru.bogdan.patseev_diploma.domain.useCases.LoadStorageWorkerByDepartmentUseCase
import ru.bogdan.patseev_diploma.domain.useCases.LoadTransactionsByWorkerIdUseCase
import ru.bogdan.patseev_diploma.domain.useCases.LoadWorkerByIdUseCase
import ru.bogdan.patseev_diploma.domain.useCases.UpdateTransactionUseCase
import ru.bogdan.patseev_diploma.presenter.states.StorageWorkerFragmentState
import ru.bogdan.patseev_diploma.util.HTTP_406
import ru.bogdan.patseev_diploma.util.TokenBundle
import javax.inject.Inject


class StorageWorkerViewModel @Inject constructor(
    private val application: MyApplication,
    private val loadStorageWorkerByDepartmentUseCase: LoadStorageWorkerByDepartmentUseCase,
    private val loadTransactionsByWorkerIdUseCase: LoadTransactionsByWorkerIdUseCase,
    private val updateTransactionUseCase: UpdateTransactionUseCase,
    private val loadWorkerByIdUseCase: LoadWorkerByIdUseCase,
    private val navController: NavController
) : ViewModel() {

    private val tokenBundle = TokenBundle(application)
    private val loadingFlow: MutableSharedFlow<StorageWorkerFragmentState> = MutableSharedFlow()

    val state: StateFlow<StorageWorkerFragmentState> = loadTransactionsByWorkerIdUseCase(
        tokenBundle.getToken(),
        tokenBundle.getWorkerId()
    )
        .onStart { StorageWorkerFragmentState.Loading }
        .map {transactions ->
            val worker = loadWorkerByIdUseCase(
                tokenBundle.getToken(),
                tokenBundle.getWorkerId()
            )
            val sharpen = loadStorageWorkerByDepartmentUseCase(
                tokenBundle.getToken(),
                Department.SHARPENING
            )
            val storageOfDecommissionedTools = loadStorageWorkerByDepartmentUseCase(
                tokenBundle.getToken(),
                Department.STORAGE_OF_DECOMMISSIONED_TOOLS
            )
                StorageWorkerFragmentState.ResultsTransaction(
                    transactions = transactions,
                    sharpen = sharpen,
                    storageOfDecommissionedTools = storageOfDecommissionedTools,
                    worker = worker,
                ) as StorageWorkerFragmentState
        }
        .mergeWith(loadingFlow)
        .catch {
            if (it.message?.trim() == HTTP_406) {
                withContext(Dispatchers.Main){
                    tokenBundle.returnToLoginFragment(
                        navController,
                        R.id.action_recycleViewWithWorkersFragment_to_loginFragment
                    )
                }
             } else {
                    loadingFlow.emit(
                        StorageWorkerFragmentState.ConnectionProblem(
                            application.getString(
                                R.string
                                    .server_doesn_t_respond_try_again_a_little_bit_later
                            )
                        )
                    )
                }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = StorageWorkerFragmentState.Loading
        )


    private fun <T> Flow<T>.mergeWith(anotherFlow: Flow<T>): Flow<T> {
        return merge(this, anotherFlow)
    }

    fun updateTransactions() {
        viewModelScope.launch {
            updateTransactionUseCase()
        }
    }

}