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
import ru.bogdan.patseev_diploma.domain.models.Tool

class ToolsAdapter(private val onClickListener: ((Tool) -> Unit)? = null) :
    ListAdapter<Tool, ToolsAdapter.ToolViewHolder>(
        DiffCallbackTools()
    ) {

    class ToolViewHolder(itemView: CardView) : RecyclerView.ViewHolder(itemView) {
        private val twName = itemView.findViewById<TextView>(R.id.record_card_tw_name)
        private val twCode = itemView.findViewById<TextView>(R.id.record_card_tw_code)
        private val iwIcon = itemView.findViewById<ImageView>(R.id.record_card_icon_tool)

        fun bind(tool: Tool, onClickListener: ((Tool) -> Unit)? = null) {
            val newName = tool.name.replace(" ", "\n")
            twName.text = newName
            twCode.text = tool.code

            Glide.with(itemView)
                .load(tool.icon)
                .into(iwIcon)

            if (onClickListener != null) {
                itemView.setOnClickListener {
                    onClickListener(tool)
                }
            }
        }

        companion object {
            fun inflateFrom(p0: ViewGroup): ToolViewHolder {
                val inflater = LayoutInflater.from(p0.context)
                val view = inflater.inflate(R.layout.tool_card_for_search, p0, false) as CardView
                return ToolViewHolder(view)
            }
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ToolViewHolder =
        ToolViewHolder.inflateFrom(p0)

    override fun onBindViewHolder(p0: ToolViewHolder, p1: Int) {
        val tool = getItem(p1)
        p0.bind(tool, onClickListener)

    }
}