package com.zakojifarm.farmapp.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zakojifarm.farmapp.data.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
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

    private val _isUserSignIn = MutableStateFlow(false)
    val isUserSignIn: StateFlow<Boolean> = _isUserSignIn

    private val _user = MutableStateFlow<UserEntity?>(null)
    val user: StateFlow<UserEntity?> = _user

    private val _events = MutableStateFlow(emptyList<EventEntity>())
    val events: StateFlow<List<EventEntity>> = _events

    private var collectingEventJob: Job? = null

    init {
        signInUser()
    }

    private fun signInUser() {
        viewModelScope.launch(Dispatchers.IO) {
            checkUserSignIn()
        }
    }

    fun checkUserSignIn() {
        val userEntities = userRepository.getAll()
        Log.v(TAG, "TesTes.3.$userEntities")
        userEntities.firstOrNull()?.let { entity ->
            Log.v(TAG, "updateUser.$entity")
            _user.value = entity
            _isUserSignIn.value = true
            launchCollectingEvents()
        }
    }

    private fun launchCollectingEvents() {
        user.value?.let { user ->
            viewModelScope.launch(Dispatchers.IO){
                collectingEventJob?.cancelAndJoin()

                collectingEventJob = viewModelScope.launch(Dispatchers.IO) {
                    eventRepository.getAllOfUserByFlow(user).collect {
                        Log.v(TAG, "updateEvents.$it")
                        _events.value = it
                    }
                }
            }
        }
    }

    fun addUser(user: UserEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.add(user)
            signInUser()
        }
    }

    fun addEvent(event: EventEntity) {
        Log.v(TAG, "TesTes.addEvent.$event")
        user.value?.let {
            viewModelScope.launch(Dispatchers.IO) {
                Log.v(TAG, "addEvent.$it,$event")
                eventRepository.add(it, event)
            }
        }
    }

    fun setWorkStatus(newStatus: WorkStatus) {
        _workStatus.value = newStatus
    }

    fun setWorkKind(newKind: WorkKind) {
        _workKind.value = newKind
    }

    fun initializeUser(name: String) {
        _isUserSignIn.value = true
        addUser(UserEntity.create(name))
        signInUser()
    }
}