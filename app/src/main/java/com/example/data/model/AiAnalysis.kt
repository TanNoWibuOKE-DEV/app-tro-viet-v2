package com.example.data.model

data class AiAnalysis(
    val confidenceRate: Int = 96,
    val maxBudget: String = "≤ 3.200.000 đ/tháng",
    val radiusKm: String = "≤ 3.0 km từ ĐH Ngoại Thương",
    val requiredAmenities: List<String> = listOf("WC riêng", "Điều hòa", "Chỗ để xe"),
    val softWeightings: List<SoftWeight> = listOf(
        SoftWeight("Yên tĩnh học bài", 0.9f),
        SoftWeight("An ninh xe máy", 0.85f)
    )
)

data class SoftWeight(
    val label: String,
    val weight: Float
)

data class ChatMessage(
    val id: String,
    val isUser: Boolean,
    val text: String,
    val timestamp: String,
    val structuredAnalysis: AiAnalysis? = null,
    val recommendedProperties: List<RoomProperty>? = null,
    val summaryText: String? = null
)
