package com.example.aml.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aml.R
import com.example.aml.model.ReportDetailItem

class ReportAdapter(private val items: List<ReportDetailItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int) = when (items[position]) {
        is ReportDetailItem.Header -> 0
        is ReportDetailItem.SummaryCard -> 1
        is ReportDetailItem.Section -> 2
        is ReportDetailItem.Disclaimer -> 3
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            0 -> HeaderViewHolder(inflater.inflate(R.layout.item_header, parent, false))
            1 -> SummaryViewHolder(inflater.inflate(R.layout.item_summary_card, parent, false))
            2 -> SectionViewHolder(inflater.inflate(R.layout.item_section, parent, false))
            else -> DisclaimerViewHolder(inflater.inflate(R.layout.item_disclaimer, parent, false))
        }
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is ReportDetailItem.Header -> (holder as HeaderViewHolder).bind(item)
            is ReportDetailItem.SummaryCard -> (holder as SummaryViewHolder).bind(item)
            is ReportDetailItem.Section -> (holder as SectionViewHolder).bind(item)
            is ReportDetailItem.Disclaimer -> (holder as DisclaimerViewHolder).bind(item)
        }
    }

    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: ReportDetailItem.Header) {
            itemView.findViewById<TextView>(R.id.tvHeader).text = item.title
        }
    }

    class SummaryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: ReportDetailItem.SummaryCard) {
            itemView.findViewById<TextView>(R.id.tvSymptom).text = "${item.percentage}%"
            itemView.findViewById<TextView>(R.id.tvRisk).text = item.match
        }
    }

    class SectionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: ReportDetailItem.Section) {
            itemView.findViewById<TextView>(R.id.tvTitle).text = item.title
            itemView.findViewById<TextView>(R.id.tvContent).text = item.content
        }
    }

    class DisclaimerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: ReportDetailItem.Disclaimer) {
            itemView.findViewById<TextView>(R.id.tvDisclaimer).text = item.content
        }
    }
}
