package com.zakojifarm.farmapp.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
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
    val eventRepository: EventRepository
) : ViewModel() {
    companion object {
        private val TAG = WorkStatusViewModel::class.java.simpleName

        private const val LIMIT_OF_COLLECTING_LATEST_EVENTS = 6

    }

    private val _isUserSignIn = MutableStateFlow(false)
    val isUserSignIn: StateFlow<Boolean> = _isUserSignIn

    private val _user = MutableStateFlow<UserEntity?>(null)
    val user: StateFlow<UserEntity?> = _user

    private val _latestEvents = MutableStateFlow(emptyList<EventEntity>())
    val latestEvents: StateFlow<List<EventEntity>> = _latestEvents

    private val _todayEvents = MutableStateFlow(emptyList<EventEntity>())
    val todayEvents: StateFlow<List<EventEntity>> = _todayEvents

    private var collectingLatestEventJob: Job? = null
    private var collectingTodayEventJob: Job? = null

    private val _isRequestedLocationUpdates = MutableStateFlow(false)
    val isRequestedLocationUpdates: StateFlow<Boolean> = _isRequestedLocationUpdates

    private val _currentLatLng = MutableStateFlow(LatLng(0.0, 0.0))
    val currentLatLng: StateFlow<LatLng> = _currentLatLng

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
            viewModelScope.launch(Dispatchers.IO) {
                collectingTodayEventJob?.cancelAndJoin()

                collectingTodayEventJob = viewModelScope.launch(Dispatchers.IO) {
                    eventRepository.getTodayAllOfUserByFlow(user).collect {
                        Log.v(TAG, "getTodayAllOfUserByFlow update.$it")
                        _todayEvents.value = it
                        _latestEvents.value = when {
                            it.isEmpty() -> emptyList()
                            it.size == 1 -> it.slice(0..0)
                            else -> it.slice(0..1)
                        }
                    }
                }

                viewModelScope.launch(Dispatchers.IO) {
                    collectingTodayEventJob?.cancelAndJoin()
                    collectingTodayEventJob = viewModelScope.launch(Dispatchers.IO) {
                        eventRepository.getTodayAllOfUserByFlow(user).collect {
                            Log.v(TAG, "getTodayAllOfUserByFlow update.$it")
                            _todayEvents.value = it
                        }
                    }

                    collectingLatestEventJob?.cancelAndJoin()
                    collectingLatestEventJob = viewModelScope.launch(Dispatchers.IO) {
                        eventRepository.getAllOfUserByFlow(user, LIMIT_OF_COLLECTING_LATEST_EVENTS)
                            .collect {
                                Log.v(TAG, "getAllOfUserByFlow update.$it")
                                _latestEvents.value = it
                            }
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

    fun initializeUser(name: String) {
        _isUserSignIn.value = true
        addUser(UserEntity.create(name))
        signInUser()
    }

    fun deleteAllEvent() {
        user.value?.let {
            viewModelScope.launch(Dispatchers.IO) {
                eventRepository.deleteAllOfUser(it)
            }
        }
    }

    fun setIsRequestedLocationUpdates(value: Boolean) {
        _isRequestedLocationUpdates.value = value
    }

    fun setCurrentLocation(value: LatLng) {
        _currentLatLng.value = value
    }
}
