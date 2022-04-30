package com.example.android.politicalpreparedness.representative

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.network.CivicsApi

@Suppress("UNCHECKED_CAST")
class RepresentativeViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RepresentativeViewModel(
            CivicsApi.retrofitService
        ) as T
    }
}