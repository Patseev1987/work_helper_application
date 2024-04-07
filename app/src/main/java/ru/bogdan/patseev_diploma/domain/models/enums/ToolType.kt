package ru.bogdan.patseev_diploma.domain.models.enums

enum class ToolType (val type:String){
    INNER("Пластина"),
    HOLDER("Державка"),
    DRILL("Сверло"),
    MILLING("Фреза"),
    MEASURE("Инструмент для измерений"),
    HELPERS("Приспособления"),
}