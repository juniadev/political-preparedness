package com.example.android.politicalpreparedness.network.jsonadapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DateAdapter {
    @FromJson
    fun dateFromJson(date: String): Date {
        val simpleDateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        return simpleDateFormatter.parse(date)!!
    }

    @ToJson
    fun dateToJson(date: Date): String {
        return date.toString()
    }
}