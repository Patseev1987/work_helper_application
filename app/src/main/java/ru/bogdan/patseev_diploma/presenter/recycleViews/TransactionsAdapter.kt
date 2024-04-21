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
import ru.bogdan.patseev_diploma.domain.models.Transaction
import ru.bogdan.patseev_diploma.domain.models.Worker
import ru.bogdan.patseev_diploma.domain.models.enums.WorkerType

class TransactionsAdapter(private val onClickListener: ((Transaction) -> Unit)? = null) :
    ListAdapter<Transaction, TransactionsAdapter.TransactionViewHolder>(DiffCallbackTransaction()) {

    class TransactionViewHolder(itemView: CardView) : RecyclerView.ViewHolder(itemView) {
        private val twTool = itemView.findViewById<TextView>(R.id.tw_tool)
        private val twAmount = itemView.findViewById<TextView>(R.id.tw_amount_transaction)
        private val twSender = itemView.findViewById<TextView>(R.id.tw_sender)
        private val twReceiver = itemView.findViewById<TextView>(R.id.tw_receiver)
        private val iwIcon = itemView.findViewById<ImageView>(R.id.icon_transaction)

        fun bind(transaction: Transaction, onClickListener: ((Transaction) -> Unit)? = null) {
            twTool.text = transaction.tool.code
            twAmount.text = transaction.amount.toString()
            twSender.text = transaction.sender.secondName
            twReceiver.text = transaction.receiver.secondName

            Glide.with(itemView)
                .load(transaction.tool.icon)
                .circleCrop()
                .into(iwIcon)

            setColorForTransaction(itemView as CardView, transaction.sender)

            if (onClickListener != null) {
                itemView.setOnClickListener {
                    onClickListener(transaction)
                }
            }
        }


        private fun setColorForTransaction(item: CardView, sender: Worker) {
            when (sender.type) {
                WorkerType.WORKER -> {
                    item.setCardBackgroundColor(
                        itemView.context.getColor(R.color.give_tool_back_to_storage)
                    )
                }

                WorkerType.STORAGE_WORKER -> {
                    item.setCardBackgroundColor(
                        itemView.context.getColor(R.color.take_tool_from_storage)
                    )
                }
            }
        }

        companion object {
            fun inflateFrom(p0: ViewGroup): TransactionViewHolder {
                val inflater = LayoutInflater.from(p0.context)
                val view = inflater.inflate(R.layout.tool_card, p0, false) as CardView
                return TransactionViewHolder(view)
            }
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): TransactionViewHolder =
        TransactionViewHolder.inflateFrom(p0)

    override fun onBindViewHolder(p0: TransactionViewHolder, p1: Int) {
        val transaction = getItem(p1)
        p0.bind(transaction = transaction, onClickListener)

    }
}