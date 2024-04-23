package ru.bogdan.patseev_diploma.presenter.recycleViews

import androidx.recyclerview.widget.DiffUtil
import ru.bogdan.patseev_diploma.domain.models.Worker

class DiffCallbackWorker : DiffUtil.ItemCallback<Worker>() {
    override fun areItemsTheSame(p0: Worker, p1: Worker): Boolean {
        return p0 == p1
    }

    override fun areContentsTheSame(p0: Worker, p1: Worker): Boolean {
        return p0 == p1
    }

}