package ru.bogdan.patseev_diploma.presenter.viewModels

import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import ru.bogdan.patseev_diploma.domain.models.Tool

class CameraFragmentViewModel:ViewModel() {
    fun getInformationAboutTool(inputString:String):Map<String,String>{
        val gson = Gson()
        val tool = gson.fromJson(inputString,Tool::class.java)
        val toolInfo:MutableMap<String,String> = mutableMapOf()
        toolInfo[CODE] = tool.code
        toolInfo[NAME] = tool.description
        toolInfo[SHELF] = tool.place.shelf
        toolInfo[COLUMN] = tool.place.column
        toolInfo[ROW] = tool.place.row
        return toolInfo
    }


    companion object{
        const val CODE = "code"
        const val NAME = "name"
        const val SHELF = "shelf"
        const val COLUMN = "column"
        const val ROW = "row"
    }
}