package ru.bogdan.patseev_diploma.domain.useCases

import ru.bogdan.patseev_diploma.domain.ApplicationRepository
import javax.inject.Inject

class CheckLoginUseCase @Inject constructor(
    private val repository: ApplicationRepository
) {
    suspend operator fun invoke(
        login: String,
        password: String
    ) = repository.checkLogin(login, password)
}