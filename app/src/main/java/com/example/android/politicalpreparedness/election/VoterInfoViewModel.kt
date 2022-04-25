package com.example.android.politicalpreparedness.election

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.database.ElectionRepository
import com.example.android.politicalpreparedness.network.CivicsApiService
import com.example.android.politicalpreparedness.network.models.Election

class VoterInfoViewModel(
    private val civicsApiService: CivicsApiService,
    private val electionRepository: ElectionRepository) : ViewModel() {

    //TODO: Add live data to hold voter info
    private val _election = MutableLiveData<Election>()
    val election: LiveData<Election>
        get() = _election

    fun initElection(election: Election) {
        _election.value = election
    }

    //TODO: Add var and methods to populate voter info

    //TODO: Add var and methods to support loading URLs

    //TODO: Add var and methods to save and remove elections to local database
    //TODO: cont'd -- Populate initial state of save button to reflect proper action based on election saved status

    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */

}