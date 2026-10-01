package com.example.data.model

data class Appointment(
    val id: String,
    val propertyId: String,
    val propertyTitle: String,
    val landlordName: String,
    val date: String,
    val time: String,
    val contactPhone: String,
    val address: String,
    val status: String = "Đã xác nhận",
    val note: String = "Xem phòng trực tiếp"
)
