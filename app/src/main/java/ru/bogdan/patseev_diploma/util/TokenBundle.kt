package ru.bogdan.patseev_diploma.util


import android.content.Context
import com.auth0.android.jwt.JWT

object TokenBundle {
    private const val SHARED_PREF_NAME = "Bogdan_helper"
    private const val TOKEN_KEY = "token_key"
    private const val ROLE = "role"
    private const val WORKER_ID = "worker_id"
    private const val STORAGE_WORKER_ID = "storage_worker_id"
    private const val DEPARTMENT = "department"
    private const val BEARER = "Bearer "


    fun setToken(context: Context, token: String) {
        val sharedPref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString(TOKEN_KEY, token)
            apply()
        }
    }

    fun getToken(context: Context): String {
        val sharedPref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        return sharedPref.getString(TOKEN_KEY, "").toString()
    }


    fun getWorkerId(context: Context):Long {
        val jwt = JWT(getTokenWithoutBearer(context))
        val id = jwt.getClaim(WORKER_ID).asLong() ?: throw IllegalStateException("wrong worker id")
        return id
    }


    private fun getTokenWithoutBearer(context: Context): String {
        val sharedPref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        return sharedPref.getString(TOKEN_KEY, "")?.replace(BEARER,"").toString()
    }

}