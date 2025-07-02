package com.example.aml.homepage.report

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aml.model.ReportItem
import com.example.aml.R

class ActivityAdapter(
    private val reports: List<ReportItem>,
    private val onClick: (ReportItem) -> Unit
) : RecyclerView.Adapter<ActivityAdapter.ReportViewHolder>() {

    inner class ReportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.tvReportTitle)
        val date = itemView.findViewById<TextView>(R.id.tvReportDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_report, parent, false)
        return ReportViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        val report = reports[position]
        holder.title.text = report.reportName
        holder.date.text = report.reportDate

        holder.itemView.setOnClickListener { onClick(report) }
    }

    override fun getItemCount(): Int = reports.size
}
