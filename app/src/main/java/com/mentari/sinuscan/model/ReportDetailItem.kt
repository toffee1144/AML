package com.mentari.sinuscan.model

// com.example.aml.model.ReportDetailItem
sealed class ReportDetailItem {
    data class Header(val title: String) : ReportDetailItem()
    data class SummaryCard(val percentage: Int, val match: String) : ReportDetailItem()
    data class Section(val title: String, val content: String) : ReportDetailItem()
    data class Disclaimer(val content: String) : ReportDetailItem()
}
