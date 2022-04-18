package com.sout.cryptocurrencytracker.Recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sout.cryptocurrencytracker.Model.StoreModel
import com.sout.cryptocurrencytracker.R
import com.sout.cryptocurrencytracker.Util.gorselIndir
import kotlinx.android.synthetic.main.store_row.view.*

class StoreRecycler (var storeList : ArrayList<StoreModel> , var listener:Listener) : RecyclerView.Adapter<StoreRecycler.Holder>() {

    interface Listener{
        fun onClickListener(storeModel: StoreModel)
    }

    class Holder(var itemView:View) : RecyclerView.ViewHolder(itemView){

        fun bind(storeModel: StoreModel, listener: Listener) {


                itemView.setOnClickListener {
                    listener.onClickListener(storeModel)
                }


                itemView.ico.gorselIndir(storeModel.icon)
               // itemView.ss.gorselIndir(storeModel.ss)
                itemView.discount.text = storeModel.discount
                itemView.name.text = storeModel.name

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
       var inflater = LayoutInflater.from(parent.context).inflate(R.layout.store_row,parent,false)
        return Holder(inflater)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(storeList[position],listener)
    }

    override fun getItemCount(): Int {
       return storeList.size
    }

}