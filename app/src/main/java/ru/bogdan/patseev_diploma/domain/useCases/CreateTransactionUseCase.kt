package ru.bogdan.patseev_diploma.domain.useCases

import ru.bogdan.patseev_diploma.domain.ApplicationRepository
import ru.bogdan.patseev_diploma.domain.models.Tool
import ru.bogdan.patseev_diploma.domain.models.Worker
import javax.inject.Inject

class CreateTransactionUseCase @Inject constructor(private val repository: ApplicationRepository) {
    suspend operator fun invoke(
        token: String,
        sender: Worker,
        receiver: Worker,
        tool: Tool,
        amount: Int
    ) = repository.createTransaction(token, sender, receiver, tool, amount)
}