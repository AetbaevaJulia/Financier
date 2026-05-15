package com.example.financier.enums

import com.google.gson.annotations.SerializedName

enum class StatementStatus {
    @SerializedName("uploaded")
    UPLOADED,
    @SerializedName("parsing")
    PARSING,
    @SerializedName("enriching")
    ENRICHING,
    @SerializedName("analyzing")
    ANALYZING,
    @SerializedName("generating_report")
    GENERATING_REPORT,
    @SerializedName("report_ready")
    REPORT_READY,
    @SerializedName("failed")
    FAILED
}