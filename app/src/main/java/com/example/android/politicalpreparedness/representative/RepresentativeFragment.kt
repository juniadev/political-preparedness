package com.example.android.politicalpreparedness.representative

import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import java.util.Locale

class DetailFragment : Fragment(), AdapterView.OnItemSelectedListener {

    companion object {
        //TODO: Add Constant for Location request
    }

    // Declare ViewModel
    private val viewModel: RepresentativeViewModel by viewModels {
        RepresentativeViewModelFactory()
    }

    private lateinit var binding: FragmentRepresentativeBinding

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        // Establish bindings
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_representative, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        populateStateSpinner()

        viewModel.representativesEmpty.observe(viewLifecycleOwner, Observer { empty ->
            binding.listPlaceholder.visibility = if (empty) View.VISIBLE else View.GONE
        })

        // Define and assign Representative adapter
        val representativeListAdapter = RepresentativeListAdapter()
        binding.representativesRecyclerView.adapter = representativeListAdapter
        viewModel.representatives.observe(viewLifecycleOwner) {
            representativeListAdapter.submitList(it)
        }

        //TODO: Establish button listeners for field and location search
        binding.buttonSearch.setOnClickListener { searchButtonClick() }

        return binding.root
    }

    private fun searchButtonClick() {
        hideKeyboard()
        viewModel.getAddressFromFields(
            binding.addressLine1.text.toString(),
            binding.addressLine2.text.toString(),
            binding.city.text.toString(),
            binding.state.selectedItem as String,
            binding.zip.text.toString()
        )
        viewModel.fetchRepresentatives()
    }

    private fun populateStateSpinner() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.states,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.state.adapter = adapter
        }
        binding.state.onItemSelectedListener = this
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //TODO: Handle location permission result to get location on permission granted
    }

    private fun checkLocationPermissions(): Boolean {
        return if (isPermissionGranted()) {
            true
        } else {
            //TODO: Request Location permissions
            false
        }
    }

    private fun isPermissionGranted() : Boolean {
        //TODO: Check if permission is already granted and return (true = granted, false = denied/other)
        return false
    }

    private fun getLocation() {
        //TODO: Get location from LocationServices
        //TODO: The geoCodeLocation method is a helper function to change the lat/long location to a human readable street address
    }

    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(context, Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
                .map { address ->
                    Address(address.thoroughfare, address.subThoroughfare, address.locality, address.adminArea, address.postalCode)
                }
                .first()
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        viewModel.setSelectedState(parent.getItemAtPosition(pos) as String)
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        viewModel.setSelectedState("")
    }
}