package com.sout.cryptocurrencytracker.Model

import com.google.gson.annotations.SerializedName

data class CurrencyModel(
    @SerializedName("currency")
    var currency: String,
    @SerializedName("price")
    var price : String
)
