package com.example.android.politicalpreparedness.election

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionRepository
import com.example.android.politicalpreparedness.network.CivicsApiService
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import kotlinx.coroutines.launch
import java.lang.Exception

class VoterInfoViewModel(
    private val civicsApiService: CivicsApiService,
    private val electionRepository: ElectionRepository) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _election = MutableLiveData<Election>()
    val election: LiveData<Election>
        get() = _election

    // Add live data to hold voter info
    private val _voterInfo = MutableLiveData<VoterInfoResponse>()
    val voterInfo: LiveData<VoterInfoResponse>
        get() = _voterInfo

    private val _voterInfoError = MutableLiveData<Boolean>()
    val voterInfoError: LiveData<Boolean>
        get() = _voterInfoError

    fun initElection(election: Election) {
        _election.value = election
    }

    fun populateVoterInfo() {
        _election.value?.let {
            viewModelScope.launch {
                val address = "${it.division.state},${it.division.country}"
                try {
                    _voterInfo.value = civicsApiService.getVoterInfo(address, it.id)
                    _voterInfoError.value = false
                } catch (e: Exception) {
                    Log.e(
                        "VoterInfoViewModel",
                        "Error retrieving voter info with address $address and election id ${it.id}",
                        e
                    )
                    _voterInfoError.value = true
                }
            }
        }
    }

    // Add var and methods to support loading URLs
    private val _loadingUrl = MutableLiveData<String>()
    val loadingUrl: LiveData<String>
        get() = _loadingUrl

    fun openVotingLocationFinderUrl() {
        _voterInfo.value?.state?.let {
            _loadingUrl.value = it[0].electionAdministrationBody.votingLocationFinderUrl
        }
    }

    fun openElectionInfoUrl() {
        _voterInfo.value?.state?.let {
            _loadingUrl.value = it[0].electionAdministrationBody.electionInfoUrl
        }
    }

    //TODO: Add var and methods to save and remove elections to local database
    //TODO: cont'd -- Populate initial state of save button to reflect proper action based on election saved status

    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */

}