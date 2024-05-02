package ru.bogdan.patseev_diploma.presenter.recycleViews

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.bogdan.patseev_diploma.R
import ru.bogdan.patseev_diploma.domain.models.StorageRecord

class StorageRecordsAdapter(private val onClickListener: ((StorageRecord) -> Unit)? = null) :
    ListAdapter<StorageRecord, StorageRecordsAdapter.CuttingToolViewHolder>(
        DiffCallbackStorageRecords()
    ) {

    class CuttingToolViewHolder(itemView: CardView) : RecyclerView.ViewHolder(itemView) {
        private val twName = itemView.findViewById<TextView>(R.id.tw_name)
        private val twCode = itemView.findViewById<TextView>(R.id.tw_code)
        private val twAmount = itemView.findViewById<TextView>(R.id.tw_amount_tool)
        private val iwIcon = itemView.findViewById<ImageView>(R.id.icon_tool)

        fun bind(storageRecord: StorageRecord, onClickListener: ((StorageRecord) -> Unit)? = null) {
            val newName = storageRecord.tool.name.replace(" ", "\n")
            twName.text = newName
            twCode.text = storageRecord.tool.code
            twAmount.text = storageRecord.amount.toString()

            Glide.with(itemView)
                .load(storageRecord.tool.icon)
                .into(iwIcon)

            if (onClickListener != null) {
                itemView.setOnClickListener {
                    onClickListener(storageRecord)
                }
            }
        }

        companion object {
            fun inflateFrom(p0: ViewGroup): CuttingToolViewHolder {
                val inflater = LayoutInflater.from(p0.context)
                val view = inflater.inflate(R.layout.tool_card, p0, false) as CardView
                return CuttingToolViewHolder(view)
            }
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CuttingToolViewHolder =
        CuttingToolViewHolder.inflateFrom(p0)

    override fun onBindViewHolder(p0: CuttingToolViewHolder, p1: Int) {
        val storageRecord = getItem(p1)
        p0.bind(storageRecord, onClickListener)

    }
}