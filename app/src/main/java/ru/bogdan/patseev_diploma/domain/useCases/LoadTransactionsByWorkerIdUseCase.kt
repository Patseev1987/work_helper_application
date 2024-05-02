package ru.bogdan.patseev_diploma.domain.useCases

import ru.bogdan.patseev_diploma.domain.ApplicationRepository
import javax.inject.Inject

class LoadTransactionsByWorkerIdUseCase @Inject constructor(
    private val repository: ApplicationRepository
) {
    operator fun invoke(workerId: Long) = repository.loadTransactionsByWorkerId(workerId)
}