package com.example.android.politicalpreparedness.election

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.database.ElectionRepository
import com.example.android.politicalpreparedness.network.CivicsApi

@Suppress("UNCHECKED_CAST")
class VoterInfoViewModelFactory(private val context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return VoterInfoViewModel(
            CivicsApi.retrofitService,
            ElectionRepository(ElectionDatabase.getInstance(context).electionDao)
        ) as T
    }
}