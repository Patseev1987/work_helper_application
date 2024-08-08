package ru.bogdan.patseev_diploma.util


import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.navigation.NavController
import com.auth0.android.jwt.JWT
import ru.bogdan.patseev_diploma.R
import ru.bogdan.patseev_diploma.domain.models.enums.Department
import ru.bogdan.patseev_diploma.domain.models.enums.WorkerType

data class TokenBundle(private val context:Context){



    fun setToken( token: String) {
        val sharedPref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString(TOKEN_KEY, token)
            apply()
        }
    }

    fun getToken(): String {
        val sharedPref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        return sharedPref.getString(TOKEN_KEY, "").toString()
    }


    fun getWorkerId():Long {
        val jwt = JWT(getTokenWithoutBearer())
        return jwt.getClaim(WORKER_ID).asLong() ?: throw IllegalStateException("wrong worker id")
    }

    fun getWorkerType():WorkerType {
        val jwt = JWT(getTokenWithoutBearer())

        val type = jwt.getClaim(WORKER_TYPE).asString() ?: throw IllegalStateException("wrong worker type")
        Log.d("TOKEN_TOKEN_TOKEN",jwt.getClaim(WORKER_TYPE).asString().toString())
        return WorkerType.valueOf(type)
    }

    fun getDepartment(): Department{
        val jwt = JWT(getTokenWithoutBearer())
        val type = jwt.getClaim(DEPARTMENT).asString() ?: throw IllegalStateException("wrong department")
        return Department.valueOf(type)
    }

    fun returnToLoginFragment(navController: NavController, destinationId:Int){
        navController.navigate(destinationId)
        Toast.makeText(context, context.getString(R.string.token_was_expired),Toast.LENGTH_SHORT).show()
    }

    private fun getTokenWithoutBearer(): String {
        val sharedPref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        return sharedPref.getString(TOKEN_KEY, "")?.replace(BEARER,"").toString()
    }

    companion object{
        private const val SHARED_PREF_NAME = "Bogdan_helper"
        private const val TOKEN_KEY = "token_key"
        private const val WORKER_TYPE = "worker_type"
        private const val WORKER_ID = "worker_id"
        private const val DEPARTMENT = "department"
        private const val BEARER = "Bearer "
    }
}