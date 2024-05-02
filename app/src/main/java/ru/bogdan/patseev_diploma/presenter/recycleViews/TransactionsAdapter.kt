package ru.bogdan.patseev_diploma.presenter.recycleViews

import android.util.Log
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
import ru.bogdan.patseev_diploma.domain.models.enums.Department
import ru.bogdan.patseev_diploma.domain.models.enums.WorkerType

class TransactionsAdapter(private val onClickListener: ((Transaction) -> Unit)? = null) :
    ListAdapter<Transaction, TransactionsAdapter.TransactionViewHolder>(DiffCallbackTransaction()) {

    class TransactionViewHolder(itemView: CardView) : RecyclerView.ViewHolder(itemView) {
        private val twTool = itemView.findViewById<TextView>(R.id.tw_tool_transaction)
        private val twAmount = itemView.findViewById<TextView>(R.id.tw_amount_transaction)
        private val twSender = itemView.findViewById<TextView>(R.id.tw_sender)
        private val twReceiver = itemView.findViewById<TextView>(R.id.tw_receiver)
        private val iwIcon = itemView.findViewById<ImageView>(R.id.icon_transaction)
        private val twDate = itemView.findViewById<TextView>(R.id.tw_transaction_date)

        fun bind(transaction: Transaction, onClickListener: ((Transaction) -> Unit)? = null) {
            val newCode = transaction.tool.code.replace("-", "\n")
            twTool.text = newCode
            twAmount.text = transaction.amount.toString()
            twSender.text = transaction.sender.secondName
            twReceiver.text = transaction.receiver.secondName
            twDate.text = transaction.date.toString()

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
            val condition = sender.type == WorkerType.STORAGE_WORKER &&
                    (sender.department != Department.SHARPENING &&
                            sender.department != Department.STORAGE_OF_DECOMMISSIONED_TOOLS &&
                            sender.department != Department.MAIN_STORAGE)

            if (condition) {
                item.setCardBackgroundColor(
                    itemView.context.getColor(R.color.give_tool_back_to_storage)
                )
            } else {
                item.setCardBackgroundColor(
                    itemView.context.getColor(R.color.take_tool_from_storage)
                )
            }
        }


        companion object {
            fun inflateFrom(p0: ViewGroup): TransactionViewHolder {
                val inflater = LayoutInflater.from(p0.context)
                val view = inflater.inflate(
                    R.layout.transaction_card,
                    p0,
                    false
                ) as CardView
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