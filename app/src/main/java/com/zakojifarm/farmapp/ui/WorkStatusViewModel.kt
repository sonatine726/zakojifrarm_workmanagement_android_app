package com.zakojifarm.farmapp.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zakojifarm.farmapp.data.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkStatusViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val eventRepository: EventRepository
) : ViewModel() {
    companion object {
        private val TAG = WorkStatusViewModel::class.java.simpleName
    }

    private val _workStatus = MutableStateFlow(WorkStatus.OFF_DUTY)
    val workStatus: StateFlow<WorkStatus> = _workStatus

    private val _workKind = MutableStateFlow(WorkKind.MOWING)
    val workKind: StateFlow<WorkKind> = _workKind

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName

    private val _user = MutableStateFlow<UserEntity?>(null)
    val user: StateFlow<UserEntity?> = _user

    private val _events = MutableStateFlow(emptyList<EventEntity>())
    val events: StateFlow<List<EventEntity>> = _events

    init {
        updateUser()
        updateEvents()
    }

    fun updateUser() {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.getAll().collect {
                it.firstOrNull()?.let { entity ->
                    Log.v(TAG, "updateUser.$entity")
                    _user.value = entity
                }
            }
        }
    }

    fun updateEvents() {
        user.value?.let { user ->
            viewModelScope.launch(Dispatchers.IO) {
                eventRepository.getAllOfUser(user).collect {
                    Log.v(TAG, "updateEvents.$it")
                    _events.value = it
                }
            }
        }
    }

    fun addUser(user: UserEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.add(user)
            updateUser()
        }
    }

    fun addEvent(event: EventEntity) {
        Log.v(TAG, "TesTes.addEvent.$event")
        user.value?.let {
            viewModelScope.launch(Dispatchers.IO) {
                Log.v(TAG, "addEvent.$it,$event")
                eventRepository.add(it, event)
                updateEvents()
            }
        }
    }

    fun setWorkStatus(newStatus: WorkStatus) {
        _workStatus.value = newStatus
    }

    fun setWorkKind(newKind: WorkKind) {
        _workKind.value = newKind
    }

    fun setUserName(newName: String) {
        _userName.value = newName
    }

    private val _currentScreen = MutableStateFlow<Screens>(Screens.DrawerScreens.Home)
    val currentScreen: StateFlow<Screens> = _currentScreen

    fun setCurrentScreen(screen: Screens) {
        _currentScreen.value = screen
    }
}