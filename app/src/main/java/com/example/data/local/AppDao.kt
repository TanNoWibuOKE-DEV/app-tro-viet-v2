package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    @Query("SELECT * FROM saved_properties")
    fun getAllSavedProperties(): Flow<List<SavedPropertyEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProperty(entity: SavedPropertyEntity)

    @Query("DELETE FROM saved_properties WHERE id = :propertyId")
    suspend fun removeSavedProperty(propertyId: String)

    @Query("SELECT EXISTS(SELECT 1 FROM saved_properties WHERE id = :propertyId)")
    fun isPropertySaved(propertyId: String): Flow<Boolean>

    @Query("SELECT * FROM appointments ORDER BY createdAt DESC")
    fun getAllAppointments(): Flow<List<AppointmentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppointment(appointment: AppointmentEntity)

    @Query("DELETE FROM appointments WHERE id = :appointmentId")
    suspend fun cancelAppointment(appointmentId: String)
}
