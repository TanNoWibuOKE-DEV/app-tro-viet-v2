package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_properties")
data class SavedPropertyEntity(
    @PrimaryKey val id: String,
    val savedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "appointments")
data class AppointmentEntity(
    @PrimaryKey val id: String,
    val propertyId: String,
    val propertyTitle: String,
    val landlordName: String,
    val date: String,
    val time: String,
    val contactPhone: String,
    val address: String,
    val status: String,
    val note: String,
    val createdAt: Long = System.currentTimeMillis()
)
