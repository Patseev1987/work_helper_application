package ru.bogdan.patseev_diploma.data.web.mappers

import ru.bogdan.patseev_diploma.data.web.pojo.PlaceWEB
import ru.bogdan.patseev_diploma.data.web.pojo.StorageRecordWEB
import ru.bogdan.patseev_diploma.data.web.pojo.ToolWEB
import ru.bogdan.patseev_diploma.data.web.pojo.TransactionWEB
import ru.bogdan.patseev_diploma.data.web.pojo.WorkerWEB
import ru.bogdan.patseev_diploma.domain.models.Place
import ru.bogdan.patseev_diploma.domain.models.StorageRecord
import ru.bogdan.patseev_diploma.domain.models.Tool
import ru.bogdan.patseev_diploma.domain.models.Transaction
import ru.bogdan.patseev_diploma.domain.models.Worker
import java.time.LocalDate


fun Worker.toWorkerWEB(): WorkerWEB {
    return WorkerWEB(
        id = this.id,
        lastName = this.secondName,
        firstName = this.firstName,
        department = this.department,
        patronymic = this.patronymic,
        login = this.login,
        password = this.password,
        joinDate = this.joinDate.toString(),
        type = this.type,
    )
}

fun WorkerWEB.toWorker(): Worker {
    return Worker(
        id = this.id,
        secondName = this.lastName,
        firstName = this.firstName,
        type = this.type,
        joinDate = LocalDate.parse(this.joinDate),
        department = this.department,
        patronymic = this.patronymic,
        login = this.login,
        password = this.password,
    )
}

fun PlaceWEB.toPlace(): Place {
    return Place(
        shelf = this.shelf,
        column = this.column,
        row = this.row

    )
}

fun Place.toPlaceWEB(): PlaceWEB {
    return PlaceWEB(
        shelf = this.shelf,
        column = this.column,
        row = this.row
    )
}

fun ToolWEB.toTool(): Tool {
    return Tool(
        code = this.code,
        name = this.name,
        description = this.description,
        notes = this.additionalInfo,
        icon = this.icon,
        place = this.place.toPlace(),
        type = this.type,
    )
}

fun Tool.toToolWEB(): ToolWEB {
    return ToolWEB(
        additionalInfo = this.notes,
        code = this.code,
        name = this.name,
        description = this.description,
        icon = this.icon,
        place = this.place.toPlaceWEB(),
        type = this.type
    )
}

fun StorageRecordWEB.toStorageRecord(): StorageRecord {
    return StorageRecord(
        id = this.id,
        worker = this.worker.toWorker(),
        tool = this.tool.toTool(),
        amount = this.amount
    )
}

fun StorageRecord.toStorageRecordWEB(): StorageRecordWEB {
    return StorageRecordWEB(
        id = this.id,
        worker = this.worker.toWorkerWEB(),
        tool = this.tool.toToolWEB(),
        amount = this.amount
    )
}

fun TransactionWEB.toTransaction():Transaction{
    return Transaction(
        id = this.id,
        sender = this.sender.toWorker(),
        receiver = this.receiver.toWorker(),
        date = LocalDate.parse(this.transactionDate),
        tool = this.tool.toTool(),
        amount = this.amount
    )
}

fun Transaction.toTransactionWEB():TransactionWEB {
    return TransactionWEB(
        id = this.id,
        sender = this.sender.toWorkerWEB(),
        receiver = this.receiver.toWorkerWEB(),
        transactionDate = this.date.toString(),
        tool = this.tool.toToolWEB(),
        amount = this.amount
    )
}