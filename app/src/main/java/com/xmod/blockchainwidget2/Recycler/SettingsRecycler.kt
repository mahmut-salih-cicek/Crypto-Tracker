package com.sout.cryptocurrencytracker.Recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sout.cryptocurrencytracker.Model.SettingsCurrencyModel
import com.sout.cryptocurrencytracker.R
import kotlinx.android.synthetic.main.row_currency_widget.view.*

class SettingsRecycler(var settingsCurrencyList: MutableList<SettingsCurrencyModel>, var listener:Listener):RecyclerView.Adapter<SettingsRecycler.Holder>() {
companion object{
    var index = ""
}

    interface Listener{
        fun OnClickListener(settingsCurrencyList: SettingsCurrencyModel, currencyIndex: Int)
    }

    class Holder(itemview:View): RecyclerView.ViewHolder(itemview){

        fun bind(
            settingsCurrencyModel: SettingsCurrencyModel,
            listener: Listener,
            currencyIndex: Int
        ) {
            itemView.setOnClickListener {
                listener.OnClickListener(settingsCurrencyModel,currencyIndex)

            }
            itemView.row_currency.text = settingsCurrencyModel.currencyName

        }

   }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        var inflater = LayoutInflater.from(parent.context).inflate(R.layout.row_currency_widget,parent,false)
        return Holder(inflater)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(settingsCurrencyList[position],listener,settingsCurrencyList[position].currencyIndex)

    }

    override fun getItemCount(): Int {
        return settingsCurrencyList.size
    }


}