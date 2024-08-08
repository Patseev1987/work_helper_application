package ru.bogdan.patseev_diploma.util

import ru.bogdan.patseev_diploma.domain.models.enums.Department

const val HTTP_406 = "HTTP 406"

fun Department.toNormalName(): String {
    return when (this) {
        Department.DEPARTMENT_19 -> {
            "Цех №19"
        }

        Department.MAIN_STORAGE -> {
            "Главный склад"
        }

        Department.SHARPENING -> {
            "Заточка инструмента"
        }

        Department.STORAGE_OF_DECOMMISSIONED_TOOLS -> {
            "Склад списанного инструмента"
        }
    }
}


const val BLANK_TOOL_CODE = ""





