package ru.bogdan.patseev_diploma.util

import ru.bogdan.patseev_diploma.domain.models.enums.Department


fun Department.toNormalName():String{
  return  when (this) {
       Department.DEPARTMENT_19 -> {"Цех №19"}
        Department.MAIN_STORAGE -> {"Главный склад"}
        Department.DEPARTMENT_65 -> {"Управление Главного технолога"}
        Department.DEPARTMENT_15 -> {"Цех №15"}
        Department.DEPARTMENT_41 -> {"Цех №41"}
        Department.DEPARTMENT_17 -> {"Цех №17"}
        Department.DEPARTMENT_28 -> {"Цех №28"}
        Department.SHARPENING -> {"Заточка инструмента"}
        Department.STORAGE_DECOMMISSIONED_TOOLS -> {"Склад списанного инструмента"}
    }
}





