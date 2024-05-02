package ru.bogdan.patseev_diploma.presenter.recycleViews


import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.bogdan.patseev_diploma.R
import ru.bogdan.patseev_diploma.domain.models.Worker
import ru.bogdan.patseev_diploma.util.toNormalName


class WorkersAdapter(private val onClickListener: ((Worker) -> Unit)? = null) :
    ListAdapter<Worker, WorkersAdapter.WorkerViewHolder>(DiffCallbackWorker()) {

    class WorkerViewHolder(itemView: CardView) : RecyclerView.ViewHolder(itemView) {
        private val twName = itemView.findViewById<TextView>(R.id.tw_name_worker_card)
        private val twSurname = itemView.findViewById<TextView>(R.id.tw_surname_worker_card)
        private val twPatronymic = itemView.findViewById<TextView>(R.id.tw_patronymic_worker_card)
        private val twDepartment = itemView.findViewById<TextView>(R.id.tw_department_worker_card)

        fun bind(worker: Worker, onClickListener: ((Worker) -> Unit)? = null) {

            twName.text = worker.firstName
            twSurname.text = worker.secondName
            twPatronymic.text = worker.patronymic
            val prettyDepartment = worker.department.toNormalName().replace(" ", "\n")
            twDepartment.text = prettyDepartment

            if (onClickListener != null) {
                itemView.setOnClickListener {
                    onClickListener(worker)
                }
            }
        }


        companion object {
            fun inflateFrom(p0: ViewGroup): WorkerViewHolder {
                val inflater = LayoutInflater.from(p0.context)
                val view = inflater.inflate(R.layout.worker_card, p0, false) as CardView
                return WorkerViewHolder(view)
            }
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): WorkerViewHolder =
        WorkerViewHolder.inflateFrom(p0)

    override fun onBindViewHolder(p0: WorkerViewHolder, p1: Int) {
        val worker = getItem(p1)
        p0.bind(worker = worker, onClickListener)

    }
}