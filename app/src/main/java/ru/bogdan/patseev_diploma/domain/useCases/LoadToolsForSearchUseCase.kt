package ru.bogdan.patseev_diploma.domain.useCases

import ru.bogdan.patseev_diploma.domain.ApplicationRepository
import javax.inject.Inject

class LoadToolsForSearchUseCase @Inject constructor(
    private val repository: ApplicationRepository
) {
    suspend operator fun invoke(code: String) = repository.loadToolsForSearch(code)
}