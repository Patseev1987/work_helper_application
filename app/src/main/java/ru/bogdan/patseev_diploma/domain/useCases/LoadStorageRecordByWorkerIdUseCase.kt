package ru.bogdan.patseev_diploma.domain.useCases

import ru.bogdan.patseev_diploma.domain.ApplicationRepository
import ru.bogdan.patseev_diploma.domain.models.enums.ToolType
import javax.inject.Inject

class LoadStorageRecordByWorkerIdUseCase @Inject constructor(
    private val repository: ApplicationRepository
) {
    suspend operator fun invoke(
        token: String,
        workerId: Long,
        toolType: ToolType,
        toolCode: String
    ) = repository.loadStorageRecordByWorkerId(
        token,
        workerId,
        toolType,
        toolCode
    )
}