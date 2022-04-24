package com.example.android.politicalpreparedness.election

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener

class ElectionsFragment: Fragment() {

    private val viewModel: ElectionsViewModel by viewModels {
        ElectionsViewModelFactory(activity as Context)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val binding: FragmentElectionBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_election, container, false)

        // Add ViewModel values and create ViewModel, and add binding values
        binding.electionsViewModel = viewModel
        binding.lifecycleOwner = this

        // Initiate recycler adapters
        val savedElectionsListAdapter = ElectionListAdapter(ElectionListener {
            // TODO navigate to voter info
        })
        val upcomingElectionsListAdapter = ElectionListAdapter(ElectionListener {
            // TODO navigate to voter info
        })

        binding.savedElectionsRecyclerView.adapter = savedElectionsListAdapter
        binding.upcomingElectionsRecyclerView.adapter = upcomingElectionsListAdapter

        // Populate recycler adapters
        viewModel.savedElections.observe(viewLifecycleOwner) {
            savedElectionsListAdapter.submitList(it)
        }
        viewModel.upcomingElections.observe(viewLifecycleOwner) {
            upcomingElectionsListAdapter.submitList(it)
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        // Refresh adapters when fragment loads
        viewModel.refreshData()
    }
}