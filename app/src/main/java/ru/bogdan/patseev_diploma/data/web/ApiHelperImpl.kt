package ru.bogdan.m17_recyclerview.data


import android.util.Log
import kotlinx.coroutines.flow.*
import ru.bogdan.patseev_diploma.data.web.WorkerMapper
import ru.bogdan.patseev_diploma.domain.models.Worker

import javax.inject.Inject
import kotlin.math.log

class ApiHelperImpl (
    private val apiService: ApiService,
    private val mapper: WorkerMapper
) : ApiHelper {
    override suspend fun checkLogin(login:String, password:String): Flow<Worker> {
      return  flow {
           emit ( mapper.WorkerWEBToWorker( apiService.checkLogin(login, password)) )
        }
    }


}
