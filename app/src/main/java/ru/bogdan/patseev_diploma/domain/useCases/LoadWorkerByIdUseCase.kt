package ru.bogdan.patseev_diploma.domain.useCases

import ru.bogdan.patseev_diploma.domain.ApplicationRepository
import ru.bogdan.patseev_diploma.domain.models.enums.Department
import javax.inject.Inject

class LoadWorkerByIdUseCase @Inject constructor(
    private val repository: ApplicationRepository
) {
    suspend operator fun invoke( token:String, id: Long) = repository
        .loadWorkerById(token, id)
}