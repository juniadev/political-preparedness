package com.example.android.politicalpreparedness.representative

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.CivicsApiService
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.launch
import java.lang.Exception

class RepresentativeViewModel(val apiService: CivicsApiService): ViewModel() {

    // Establish live data for representatives and address
    private var _representatives = MutableLiveData<List<Representative>>()
    val representatives: LiveData<List<Representative>>
        get() = _representatives

    private val _address = MutableLiveData<Address>()
    val address: LiveData<Address>
        get() = _address

    private val _representativesEmpty = MutableLiveData<Boolean>()
    val representativesEmpty: LiveData<Boolean>
        get() = _representativesEmpty

    fun setSelectedState(state: String) {
        _address.value?.state = state
    }

    // Create function to fetch representatives from API from a provided address
    fun fetchRepresentatives() {
        _address.value?.let { addr ->
            viewModelScope.launch {
                try {
                    val representativeResponse = apiService.getRepresentatives(addr.toSearchString())
                    val offices = representativeResponse.offices
                    val officials = representativeResponse.officials
                    _representatives.value = offices.flatMap { it.getRepresentatives(officials) }
                    _representativesEmpty.value = _representatives.value?.isEmpty() ?: true
                } catch (e: Exception) {
                    _representativesEmpty.value = true
                    Log.e("RepresentativeViewModel", "Error retrieving representatives", e)
                }
            }
        }
    }

    /**
     *  The following code will prove helpful in constructing a representative from the API. This code combines the two nodes of the RepresentativeResponse into a single official :

    val (offices, officials) = getRepresentativesDeferred.await()
    _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }

    Note: getRepresentatives in the above code represents the method used to fetch data from the API
    Note: _representatives in the above code represents the established mutable live data housing representatives

     */

    //TODO: Create function get address from geo location

    // Create function to get address from individual fields
    fun getAddressFromFields(line1: String = "", line2: String = "", city: String = "", state: String = "", zip: String = "") {
        _address.value = Address(line1, line2, city, state, zip)
    }
}
