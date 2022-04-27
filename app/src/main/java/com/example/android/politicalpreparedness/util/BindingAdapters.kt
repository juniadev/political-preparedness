package com.example.android.politicalpreparedness.util

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@BindingAdapter("electionDate")
fun bindElectionDate(textView: TextView, electionDate: Date) {
    val sdf = SimpleDateFormat("EEE MMM dd HH':'mm':'ss z yyyy", Locale.US)
    textView.text = sdf.format(electionDate)
}

@BindingAdapter("isLoading")
fun bindLoadingImage(imageView: ImageView, loading: Boolean) {
    if (loading) {
        imageView.visibility = View.VISIBLE
        imageView.setImageResource(R.drawable.loading_animation)
    } else {
        imageView.visibility = View.GONE
    }
}