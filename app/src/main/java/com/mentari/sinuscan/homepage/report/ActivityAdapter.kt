package com.mentari.sinuscan.homepage.report

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mentari.sinuscan.model.ReportItem
import com.mentari.sinuscan.R

class ActivityAdapter(
    private val reports: List<ReportItem>,
    private val onClick: (ReportItem) -> Unit,
    private val onDelete: (ReportItem) -> Unit  // callback for delete
) : RecyclerView.Adapter<ActivityAdapter.ReportViewHolder>() {

    inner class ReportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.tvReportTitle)
        val date = itemView.findViewById<TextView>(R.id.tvReportDate)
        val deleteButton = itemView.findViewById<ImageView>(R.id.btnDeleteReport)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_report, parent, false)
        return ReportViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        val report = reports[position]
        holder.title.text = report.reportName
        holder.date.text = report.reportDate

        holder.deleteButton.visibility = View.GONE  // hide by default

        // Normal click
        holder.itemView.setOnClickListener { onClick(report) }

        // Long press (3 seconds)
        holder.itemView.setOnLongClickListener {
            holder.deleteButton.visibility = View.VISIBLE
            true
        }

        // Delete button click
        holder.deleteButton.setOnClickListener {
            onDelete(report)
            holder.deleteButton.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = reports.size
}
