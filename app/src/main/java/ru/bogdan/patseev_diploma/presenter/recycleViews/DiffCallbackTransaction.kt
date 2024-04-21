package ru.bogdan.patseev_diploma.presenter.recycleViews

import androidx.recyclerview.widget.DiffUtil
import ru.bogdan.patseev_diploma.domain.models.Transaction

class DiffCallbackTransaction : DiffUtil.ItemCallback<Transaction>() {
    override fun areItemsTheSame(p0: Transaction, p1: Transaction): Boolean {
        return p0 == p1
    }

    override fun areContentsTheSame(p0: Transaction, p1: Transaction): Boolean {
        return p0 == p1
    }

}