package com.xmod.blockchainwidget2.View



import android.os.Bundle

import android.app.Dialog
import android.view.View
import android.view.Window
import android.widget.Button
import com.bumptech.glide.Glide
import android.widget.ImageView
import com.sout.cryptocurrencytracker.R
import com.sout.cryptocurrencytracker.View.MainActivity


class CustomDialogClass     // TODO Auto-generated constructor stub
    (var c: MainActivity) : Dialog(c), View.OnClickListener {


    var yes: Button? = null
    var no:android.widget.Button? = null

    companion object{
        var startHowTo : Boolean = true
    }


     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.custom_dialog)

//         val cdd = CustomDialogClass(MainActivity)


         val imageView = findViewById<View>(R.id.gif) as ImageView
         Glide.with(context)
             .asGif()
             .load(R.drawable.how)
             .into(imageView);





         yes =  findViewById(R.id.ok) as Button;
         no = findViewById(R.id.dontshow) as Button;

         yes!!.setOnClickListener(this);
         no!!.setOnClickListener(this);


     }

    override fun onClick(v: View) {
        when (v.getId()) {
            R.id.ok -> dismiss()
            R.id.dontshow -> sharedData()
            else -> {
            }
        }
        dismiss()
    }


    fun sharedData(){
        startHowTo = false
        MainActivity.sharedPreferences.edit().putBoolean("howTo",startHowTo).apply()
        dismiss()
    }




}

