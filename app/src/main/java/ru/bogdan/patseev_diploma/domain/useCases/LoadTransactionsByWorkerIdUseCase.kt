package ru.bogdan.patseev_diploma.domain.useCases

import ru.bogdan.patseev_diploma.domain.ApplicationRepository
import javax.inject.Inject

class LoadTransactionsByWorkerIdUseCase @Inject constructor(
    private val repository: ApplicationRepository
) {
    operator fun invoke(token: String, workerId: Long) = repository.loadTransactionsByWorkerId(token, workerId)
}