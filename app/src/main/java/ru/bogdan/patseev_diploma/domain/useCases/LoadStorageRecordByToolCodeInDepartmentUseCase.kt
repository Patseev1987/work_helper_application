package ru.bogdan.patseev_diploma.domain.useCases

import ru.bogdan.patseev_diploma.domain.ApplicationRepository
import ru.bogdan.patseev_diploma.domain.models.enums.ToolType
import javax.inject.Inject

class LoadStorageRecordByToolCodeInDepartmentUseCase @Inject constructor(
    private val repository: ApplicationRepository
) {
    suspend operator fun invoke(
        token: String,
        toolCode: String
    ) = repository.getRecordsByToolCodeInDepartment(
        token,
        toolCode
    )
}