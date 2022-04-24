package com.example.android.politicalpreparedness.util

import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@BindingAdapter("electionDate")
fun bindElectionDate(textView: TextView, electionDate: Date) {
    val sdf = SimpleDateFormat("EEE MMM dd HH':'mm':'ss z yyyy", Locale.US)
    textView.text = sdf.format(electionDate)
}