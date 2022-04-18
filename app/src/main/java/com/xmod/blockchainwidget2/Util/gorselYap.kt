package com.sout.cryptocurrencytracker.Util

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

import com.sout.cryptocurrencytracker.R


fun ImageView.gorselIndir(url:String){
    val options = RequestOptions()
        .placeholder(R.color.white)
        .error(R.color.white)

    Glide.with(context).setDefaultRequestOptions(options).load(url).into(this)
}