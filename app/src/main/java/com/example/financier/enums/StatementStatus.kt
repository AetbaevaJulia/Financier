package com.example.financier.enums

import kotlinx.serialization.Serializable

@Serializable
enum class StatementStatus {
    UPLOADED,
    PARSING,
    ENRICHING,
    ANALYZING,
    GENERATING_REPORT,
    REPORT_READY,
    FAILED
}