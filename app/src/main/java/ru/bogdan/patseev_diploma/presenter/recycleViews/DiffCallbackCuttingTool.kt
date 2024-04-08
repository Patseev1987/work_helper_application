package ru.bogdan.m17_recyclerview.presentation.recycleView

import androidx.recyclerview.widget.DiffUtil
import ru.bogdan.patseev_diploma.domain.models.StorageRecord

class DiffCallbackCuttingTool : DiffUtil.ItemCallback<StorageRecord>() {
    override fun areItemsTheSame(p0: StorageRecord, p1: StorageRecord): Boolean {
        return p0 == p1
    }

    override fun areContentsTheSame(p0: StorageRecord, p1: StorageRecord): Boolean {
        return p0 == p1
    }

}