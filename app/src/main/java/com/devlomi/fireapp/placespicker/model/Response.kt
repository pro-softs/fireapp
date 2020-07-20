package com.devlomi.fireapp.placespicker.model

import com.devlomi.fireapp.placespicker.model.Venue
import com.google.gson.annotations.SerializedName

data class Response(
        @SerializedName("confident")
        val confident: Boolean,
        @SerializedName("venues")
        val venues: List<Venue>
)