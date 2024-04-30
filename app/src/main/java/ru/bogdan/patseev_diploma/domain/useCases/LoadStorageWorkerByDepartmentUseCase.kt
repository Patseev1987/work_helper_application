package ru.bogdan.patseev_diploma.domain.useCases

import ru.bogdan.patseev_diploma.domain.ApplicationRepository
import ru.bogdan.patseev_diploma.domain.models.enums.Department
import javax.inject.Inject

class LoadStorageWorkerByDepartmentUseCase @Inject constructor(
    private val repository: ApplicationRepository
) {
    suspend operator fun invoke(department:Department) = repository
        .loadStorageWorkerByDepartment(department)
}