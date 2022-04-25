package com.example.android.politicalpreparedness.election

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionRepository
import com.example.android.politicalpreparedness.network.CivicsApiService
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.launch

class ElectionsViewModel(
    private val api: CivicsApiService,
    private val repository: ElectionRepository): ViewModel() {

    private val _upcomingElections = MutableLiveData<List<Election>>()
    val upcomingElections: LiveData<List<Election>>
        get() = _upcomingElections

    private val _savedElections = MutableLiveData<List<Election>>()
    val savedElections: LiveData<List<Election>>
        get() = _savedElections

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    fun refreshData() {
        getUpcomingElections()
        getSavedElections()
    }

    private fun getUpcomingElections() {
        viewModelScope.launch {
            _loading.value = true
            val electionsResponse = api.getElections()
            _upcomingElections.value = electionsResponse.elections
            _loading.value = false
        }
    }

    private fun getSavedElections() {
        viewModelScope.launch {
            _savedElections.value = repository.getAllElections()
        }
    }
}