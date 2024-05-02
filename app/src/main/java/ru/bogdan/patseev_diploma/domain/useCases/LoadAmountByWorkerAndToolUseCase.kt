package ru.bogdan.patseev_diploma.domain.useCases

import ru.bogdan.patseev_diploma.domain.ApplicationRepository
import ru.bogdan.patseev_diploma.domain.models.Tool
import ru.bogdan.patseev_diploma.domain.models.Worker
import javax.inject.Inject

class LoadAmountByWorkerAndToolUseCase @Inject constructor(
    private val repository: ApplicationRepository
) {
    suspend operator fun invoke(worker: Worker, tool: Tool) = repository
        .loadAmountByWorkerAndTool(worker, tool)
}