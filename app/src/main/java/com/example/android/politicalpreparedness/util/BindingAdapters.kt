package com.example.android.politicalpreparedness.util

import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.time.format.DateTimeFormatter
import java.util.Date

@BindingAdapter("electionDate")
fun bindElectionDate(textView: TextView, electionDate: Date) {
    val formatter = DateTimeFormatter.ofPattern("ddd MMM dd HH':'mm':'ss zzz yyyy")
    textView.text = formatter.format(electionDate.toInstant())
}