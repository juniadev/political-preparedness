package com.example.android.politicalpreparedness.representative

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import java.util.Locale

class DetailFragment : Fragment(), AdapterView.OnItemSelectedListener {

    companion object {
        // Constant for Location request
        private const val LOCATION_REQUEST = 1
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
            if (empty) {
                Snackbar.make(
                    this.view!!,
                    requireContext().getString(R.string.no_representatives_found),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        })

        // Define and assign Representative adapter
        val representativeListAdapter = RepresentativeListAdapter()
        binding.representativesRecyclerView.adapter = representativeListAdapter
        viewModel.representatives.observe(viewLifecycleOwner) {
            representativeListAdapter.submitList(it)
        }

        // Establish button listeners for field and location search
        binding.buttonSearch.setOnClickListener { searchButtonClick() }
        binding.buttonLocation.setOnClickListener { locationButtonClick() }

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
        viewModel.clearRepresentatives()
        viewModel.fetchRepresentatives()
    }

    private fun locationButtonClick() {
        viewModel.clearRepresentatives()
        if (checkLocationPermissions()) {
            getLocation()
        }
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
        // Handle location permission result to get location on permission granted
        if (requestCode == LOCATION_REQUEST) {
            if (grantResults.isNotEmpty() && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLocation()
            } else {
                displayLocationPermissionError(this.view!!, requireContext())
            }
        }
    }

    private fun displayLocationPermissionError(view: View, context: Context) {
        Snackbar.make(
            view,
            context.getString(R.string.permission_denied_explanation),
            Snackbar.LENGTH_LONG
        ).show()
    }

    private fun checkLocationPermissions(): Boolean {
        return if (isPermissionGranted()) {
            true
        } else {
            // Request Location permissions
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_REQUEST
            )
            false
        }
    }

    private fun isPermissionGranted() : Boolean {
        // Check if permission is already granted and return (true = granted, false = denied/other)
        return ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        // Get location from LocationServices
        val client = LocationServices.getFusedLocationProviderClient(requireActivity())
        client.lastLocation.addOnSuccessListener { location ->
            // The geoCodeLocation method is a helper function to change the lat/long location to a human readable street address
            val address = geoCodeLocation(location)
            viewModel.setAddressFromGeolocation(address)
            viewModel.fetchRepresentatives()
        }
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