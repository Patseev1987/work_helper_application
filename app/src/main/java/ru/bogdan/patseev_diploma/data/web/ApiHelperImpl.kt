package ru.bogdan.m17_recyclerview.data


import kotlinx.coroutines.flow.*
import ru.bogdan.patseev_diploma.data.web.mappers.WorkerMapper
import ru.bogdan.patseev_diploma.domain.models.Worker

class ApiHelperImpl (
    private val apiService: ApiService,
    private val mapper: WorkerMapper
) : ApiHelper {
    override suspend fun checkLogin(login:String, password:String): Flow<Worker> {
      return  flow {
           emit ( mapper.workerWEBToWorker( apiService.checkLogin(login, password)) )
        }
    }


}
