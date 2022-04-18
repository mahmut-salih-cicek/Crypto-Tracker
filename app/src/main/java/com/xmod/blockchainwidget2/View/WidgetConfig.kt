package com.sout.cryptocurrencytracker.View

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.RemoteViews
import androidx.recyclerview.widget.LinearLayoutManager
import com.sout.cryptocurrencytracker.Model.SettingsCurrencyModel
import com.sout.cryptocurrencytracker.Recycler.SettingsRecycler
import com.sout.cryptocurrencytracker.SettingsData.LocalSettingsData.Companion.data
import com.sout.cryptocurrencytracker.View.MainActivity.Companion.strli
import com.sout.cryptocurrencytracker.R
import kotlinx.android.synthetic.main.activity_widget_config.*

class WidgetConfig : AppCompatActivity(), SettingsRecycler.Listener {

    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID
    private var editTextButton: EditText? = null


    companion object {
        var Kontrol:Boolean? = false
        lateinit var sharedPreferences100: SharedPreferences
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_widget_config)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = SettingsRecycler(data(),this@WidgetConfig)

        sharedPreferences100 = this.getSharedPreferences("com.widget",Context.MODE_PRIVATE)


        val configIntent = intent
        val extras = configIntent.extras
        if (extras != null) {
            appWidgetId = extras.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID
            )
        }
        val resultValue = Intent()
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(RESULT_CANCELED, resultValue)
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
        }
        editTextButton = findViewById(R.id.edit_text_button)

    }

    override fun OnClickListener(settingsCurrencyList: SettingsCurrencyModel, currencyIndex: Int) {
        var TOUCH_CURRENCY = settingsCurrencyList.currencyName
        confirmConfiguration(TOUCH_CURRENCY,currencyIndex)


    }


    fun confirmConfiguration(TOUCH_CURRENCY: String,currencyIndex:Int) {
        val appWidgetManager = AppWidgetManager.getInstance(this)
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        val buttonText = editTextButton!!.text.toString()
        val views = RemoteViews(
            this.packageName,
            R.layout.main_currency_widget
        ) ///burda sorun olabilir!!
        views.setOnClickPendingIntent(R.id.example_widget_button, pendingIntent)
        views.setCharSequence(R.id.example_widget_button, "setText", buttonText)
        //views.setViewVisibility(R.id.img,View.VISIBLE)
        BASILAN_COİN = TOUCH_CURRENCY.uppercase()
        INDEX = currencyIndex
        Kontrol=true
        appWidgetManager.updateAppWidget(appWidgetId, views)
        val prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString(KEY_BUTTON_TEXT + appWidgetId, buttonText)
        editor.apply()
        val resultValue = Intent()
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(RESULT_OK, resultValue)
        finish()
    }


}