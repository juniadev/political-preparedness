package com.example.android.politicalpreparedness.election

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding

class VoterInfoFragment : Fragment() {

    private val viewModel: VoterInfoViewModel by viewModels {
        VoterInfoViewModelFactory(activity as Context)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val binding: FragmentVoterInfoBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_voter_info, container, false)

        val election = VoterInfoFragmentArgs.fromBundle(requireArguments()).election

        // Add ViewModel values and create ViewModel
        viewModel.initElection(election)

        // Add binding values
        binding.viewModel = viewModel

        // Populate voter info -- hide views without provided data
        viewModel.populateVoterInfo()
        viewModel.voterInfoError.observe(viewLifecycleOwner, Observer { error ->
            binding.voterInfoError.visibility = if (error) View.VISIBLE else View.GONE
        })
        viewModel.voterInfo.observe(viewLifecycleOwner, Observer { voterInfo ->
            voterInfo.state?.let { states ->
                val electionAdministrationBody = states[0].electionAdministrationBody
                if (electionAdministrationBody.votingLocationFinderUrl != null) {
                    binding.stateLocations.visibility = View.VISIBLE
                }
                if (electionAdministrationBody.electionInfoUrl != null) {
                    binding.stateBallot.visibility = View.VISIBLE
                }
                if (electionAdministrationBody.correspondenceAddress != null) {
                    binding.addressGroup.visibility = View.VISIBLE
                    // for some reason if I set the address in the layout xml, the text does not appear
                    // in screen, so I'm setting it here instead
                    binding.address.text = electionAdministrationBody.correspondenceAddress.toFormattedString()
                }
            }
        })

        // Handle loading of URLs
        viewModel.loadingUrl.observe(viewLifecycleOwner, Observer { url ->
            loadUrlIntent(url)
        })

        // Handle save button UI state
        viewModel.saveAction.observe(viewLifecycleOwner, Observer { action ->
            binding.followUnfollowButton.text =
                if (action == SaveAction.FOLLOW) getString(R.string.follow_election)
                else getString(R.string.unfollow_election)
        })

        return binding.root
    }

    // Create method to load URL intents
    private fun loadUrlIntent(url: String) {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(url)
            )
        )
    }
}