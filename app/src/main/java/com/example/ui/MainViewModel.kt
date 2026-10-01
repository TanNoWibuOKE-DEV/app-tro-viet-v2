package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.Appointment
import com.example.data.model.RoomProperty
import com.example.data.repository.PropertyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    val repository = PropertyRepository(application)

    private val _properties = MutableStateFlow(repository.getAllProperties())
    val properties: StateFlow<List<RoomProperty>> = _properties.asStateFlow()

    val savedPropertyIds: StateFlow<List<String>> = repository.getSavedPropertyIds()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val appointments: StateFlow<List<Appointment>> = repository.getAppointments()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun toggleSave(property: RoomProperty) {
        viewModelScope.launch {
            val isCurrentSaved = savedPropertyIds.value.contains(property.id)
            repository.toggleSaveProperty(property.id, isCurrentSaved)
        }
    }

    fun bookAppointment(
        property: RoomProperty,
        date: String,
        time: String,
        phone: String,
        note: String
    ) {
        viewModelScope.launch {
            repository.bookAppointment(
                propertyId = property.id,
                propertyTitle = property.title,
                landlordName = property.landlordName,
                date = date,
                time = time,
                contactPhone = phone,
                address = property.address,
                note = note
            )
        }
    }

    fun cancelAppointment(appointmentId: String) {
        viewModelScope.launch {
            repository.cancelAppointment(appointmentId)
        }
    }
}
