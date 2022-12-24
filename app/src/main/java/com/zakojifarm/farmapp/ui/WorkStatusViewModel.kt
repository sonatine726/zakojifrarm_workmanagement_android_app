package com.zakojifarm.farmapp.ui

import androidx.lifecycle.ViewModel
import com.zakojifarm.farmapp.data.EventRepository
import com.zakojifarm.farmapp.data.UserRepository
import com.zakojifarm.farmapp.data.WorkKind
import com.zakojifarm.farmapp.data.WorkStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class WorkStatusViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val eventRepository: EventRepository
) : ViewModel() {
    private val _workStatus = MutableStateFlow(WorkStatus.OFF_DUTY)
    val workStatus: StateFlow<WorkStatus> = _workStatus

    private val _workKind = MutableStateFlow(WorkKind.MOWING)
    val workKind: StateFlow<WorkKind> = _workKind

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName

    fun setWorkStatus(newStatus: WorkStatus) {
        _workStatus.value = newStatus
    }

    fun setWorkKind(newKind: WorkKind) {
        _workKind.value = newKind
    }

    fun setUserName(newName: String) {
        _userName.value = newName
    }
}