package com.example.financier.enums

import kotlinx.serialization.Serializable

@Serializable
enum class ClassificationSource {
    BANK,
    RULES,
    USER,
    AI,
    UNKNOWN
}