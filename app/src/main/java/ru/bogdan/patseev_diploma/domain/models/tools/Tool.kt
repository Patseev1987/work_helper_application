package ru.bogdan.patseev_diploma.domain.models.tools

abstract class Tool(
   open val code:String,
   open val name: String,
   open val description: String,
   open val notes: String?,
   open val icon: String,
   open val place: Place
) {

}