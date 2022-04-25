package com.example.android.politicalpreparedness.election

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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

        //TODO: Populate voter info -- hide views without provided data.
        /**
        Hint: You will need to ensure proper data is provided from previous fragment.
        */


        //TODO: Handle loading of URLs

        //TODO: Handle save button UI state
        //TODO: cont'd Handle save button clicks

        return binding.root
    }

    //TODO: Create method to load URL intents

}