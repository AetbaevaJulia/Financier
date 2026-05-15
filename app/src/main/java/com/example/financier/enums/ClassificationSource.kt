package com.example.financier.enums

import com.google.gson.annotations.SerializedName

enum class ClassificationSource {
    @SerializedName("bank")
    BANK,
    @SerializedName("rules")
    RULES,
    @SerializedName("user")
    USER,
    @SerializedName("ai")
    AI,
    @SerializedName("unknown")
    UNKNOWN
}