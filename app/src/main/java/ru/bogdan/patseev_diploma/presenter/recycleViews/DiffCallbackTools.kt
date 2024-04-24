package ru.bogdan.patseev_diploma.presenter.recycleViews

import androidx.recyclerview.widget.DiffUtil
import ru.bogdan.patseev_diploma.domain.models.StorageRecord
import ru.bogdan.patseev_diploma.domain.models.Tool

class DiffCallbackTools : DiffUtil.ItemCallback<Tool>() {
    override fun areItemsTheSame(p0: Tool, p1: Tool): Boolean {
        return p0 == p1
    }

    override fun areContentsTheSame(p0: Tool, p1: Tool): Boolean {
        return p0 == p1
    }

}