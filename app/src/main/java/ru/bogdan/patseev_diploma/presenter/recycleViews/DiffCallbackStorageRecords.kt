package ru.bogdan.patseev_diploma.presenter.recycleViews

import androidx.recyclerview.widget.DiffUtil
import ru.bogdan.patseev_diploma.domain.models.StorageRecord

class DiffCallbackStorageRecords : DiffUtil.ItemCallback<StorageRecord>() {
    override fun areItemsTheSame(p0: StorageRecord, p1: StorageRecord): Boolean {
        return p0 == p1
    }

    override fun areContentsTheSame(p0: StorageRecord, p1: StorageRecord): Boolean {
        return p0 == p1
    }

}