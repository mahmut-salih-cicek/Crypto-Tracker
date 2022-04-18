    package com.sout.cryptocurrencytracker.View

    import android.annotation.SuppressLint
    import android.app.*
    import android.appwidget.AppWidgetManager
    import android.appwidget.AppWidgetProvider
    import android.content.*
    import android.graphics.Bitmap
    import android.graphics.Canvas
    import android.graphics.Color
    import android.net.ConnectivityManager
    import android.os.*
    import android.util.Log
    import android.view.View
    import android.view.ViewStub
    import android.widget.RemoteViews
    import android.widget.Toast
    import androidx.appcompat.app.AppCompatActivity
    import androidx.core.app.NotificationCompat
    import com.androidplot.xy.*
    import com.loopj.android.http.AsyncHttpClient
    import com.loopj.android.http.JsonHttpResponseHandler
    import com.sout.cryptocurrencytracker.R
    import cz.msebera.android.httpclient.Header
    import kotlinx.android.synthetic.main.activity_main.*
    import okhttp3.*
    import org.json.JSONException
    import org.json.JSONObject
    import java.util.*
    import kotlin.collections.ArrayList
    import org.jetbrains.anko.doAsync
    import java.text.SimpleDateFormat
import com.androidplot.xy.LineAndPointFormatter
    import com.sout.cryptocurrencytracker.View.MainActivity.Companion.strli
    import com.sout.cryptocurrencytracker.View.MainCurrencyWidget.Companion.sharedPreferences99
    import com.sout.cryptocurrencytracker.View.WidgetConfig.Companion.BASILAN_COİN
    import com.sout.cryptocurrencytracker.View.WidgetConfig.Companion.Kontrol

    class MainCurrencyWidget : AppWidgetProvider() {


        companion object{
            lateinit var sharedPreferences99: SharedPreferences
        }

        //// Start Services fonksiyonu /////
        private fun startService(context: Context) {
            if (Build.VERSION.SDK_INT >= 26) {
                context.startForegroundService(Intent(context, MyService::class.java))
            } else {
                context.startService(Intent(context, MyService::class.java))
            }
        }

        private fun startService2(context: Context) {
            if (Build.VERSION.SDK_INT >= 26) {
                context.startForegroundService(Intent(context, MyService2::class.java))
            } else {
                context.startService(Intent(context, MyService2::class.java))
            }
        }


        override fun onUpdate(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetIds: IntArray
        ) {
            // There may be multiple widgets active, so update all of them

            for (appWidgetId in appWidgetIds) {
                val views = RemoteViews(context.packageName, R.layout.main_currency_widget)
                val intent = Intent(context, MainActivity::class.java)
                val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
                val prefs = context.getSharedPreferences(
                    WidgetConfig.SHARED_PREFS,
                    Context.MODE_PRIVATE
                )
                val buttonText =
                    prefs.getString(WidgetConfig.KEY_BUTTON_TEXT + appWidgetId, "Press me")
                views.setOnClickPendingIntent(R.id.example_widget_button, pendingIntent)
                views.setCharSequence(R.id.example_widget_button, "setText", buttonText)
                appWidgetManager.updateAppWidget(appWidgetId, views)
            }

            for (appWidgetId in appWidgetIds) {

                updateAppWidget(context, appWidgetManager, appWidgetId)
            }

        }

        override fun onEnabled(context: Context) {
            // Enter relevant functionality for when the first widget is created
                startService(context)
                startService2(context)

        }

        override fun onDisabled(context: Context) {
            // Enter relevant functionality for when the last widget is disabled
            context.stopService(Intent(context, MyService::class.java))
            context.stopService(Intent(context, MyService2::class.java))
        }


        class MyService : Service() {

            companion object{

               var  CryptoPrice5 = ""
               lateinit var sharedPreferences300 : SharedPreferences
                var oldCryptData = ""
                var raw = ""

            }
            private val TAG = MyService::class.java.simpleName
            private val mHandler = Handler()
            private var mScreenFlag = true
            var gStarted = false


            private fun updateWidget() {
                sharedPreferences300 = this.getSharedPreferences("com.crypto",Context.MODE_PRIVATE)
                if (BASILAN_COİN != ""){
                    sharedPreferences300.edit().putString("data200", BASILAN_COİN).apply()
                    sharedPreferences300.edit().putBoolean("bool", Kontrol!!).apply()
                }
                ////////////////////////////OK HTTP İLE PRİCE CEKİLDİ !!!!!!!!!!!!!!!!!!!!////////
                val client = OkHttpClient()
                val url = "https://api.binance.com/api/v3/ticker/price?symbol=${sharedPreferences300.getString("data200","").toString()}USDT"
                val request : Request = Request.Builder()
                    .url(url)
                    .build()

                client.newCall(request).enqueue(object : okhttp3.Callback {
                    override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
                        e.printStackTrace()
                    }

                    @Throws(java.io.IOException::class)
                    override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                        if (response.isSuccessful) {
                            //      val myResponse = response.body!!.string()

                            val response = client.newCall(request).execute()
                            val jsonDataString = response.body?.string()
                            val json = JSONObject(jsonDataString)
                            // val rawUrl = json.getJSONObject("urls").getString("raw")
                            val rawUrl = json.getString("price")
                             CryptoPrice5 = rawUrl.substring(0,7)
                            raw = rawUrl
                            println("BUNA BAKK " + rawUrl)

                        }
                    }
                })
                /////////////////////////////////////////////////////////////////////////////////////////


    ///////////////////////////////REMOTE VİEV INITIALİZLARI////////////////////////////
                val saat = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
                val tarih_gun = SimpleDateFormat("dd EEE", Locale.getDefault()).format(Date())
                val mViews = RemoteViews(packageName, R.layout.main_currency_widget)
                val mThisWidget = ComponentName(this, MainCurrencyWidget::class.java)
                mViews.setTextViewText(R.id.tarih_gun,tarih_gun)
                mViews.setTextViewText(R.id.saat,saat)
                mViews.setTextViewText(R.id.currencyName,"${sharedPreferences300.getString("data200","").toString().uppercase()}/USD")
                mViews.setTextViewText(R.id.currencyPricenNEW, CryptoPrice5)








                ///CryptoPrice5
                if (CryptoPrice5> oldCryptData){
                    mViews.setViewVisibility(R.id.arrow_up,View.VISIBLE)
                    mViews.setViewVisibility(R.id.arrow_down,View.GONE)
                    mViews.setViewVisibility(R.id.sabit,View.GONE)

                }else if (CryptoPrice5 < oldCryptData){
                    mViews.setViewVisibility(R.id.arrow_down,View.VISIBLE)
                    mViews.setViewVisibility(R.id.arrow_up,View.GONE)
                    mViews.setViewVisibility(R.id.sabit,View.GONE)

                }else if (CryptoPrice5 == oldCryptData){
                    mViews.setViewVisibility(R.id.arrow_down,View.GONE)
                    mViews.setViewVisibility(R.id.arrow_up,View.GONE)
                    mViews.setViewVisibility(R.id.sabit,View.VISIBLE)
                }
                oldCryptData = CryptoPrice5




                ///////Son Guncelleme /////kodu
                val mManager = AppWidgetManager.getInstance(this@MyService)
                mManager.updateAppWidget(mThisWidget, mViews)
                ///////////////////////////////////////////////////


            }

            private val r = object : Runnable {
                override fun run() {
                    updateWidget()
                    if (mScreenFlag) {
                        val now = System.currentTimeMillis()
                        val delay = 1000  - (now % 100)
                        mHandler.postDelayed(this, delay)
                    }
                }
            }

            private fun update() {
                mHandler.post(r)
            }


            @SuppressLint("WrongConstant")
            override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
                super.onStartCommand(intent, flags, startId)
                Log.d(TAG, "onStart")
                if (gStarted) {
                    Log.d(TAG, "already started")
                    return Service.START_NOT_STICKY
                }
                Log.d(TAG, "start")
                gStarted = true

                if (Build.VERSION.SDK_INT >= 26) {
                    val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    val channel = NotificationChannel(
                        "channel_1",
                        getString(R.string.channel_name),
                        NotificationManager.IMPORTANCE_NONE
                    )
                    ///   notification.flags |= Notification.FLAG_AUTO_CANCEL | Notification.FLAG_SHOW_LIGHTS;
                    channel.lightColor = Color.GREEN
                    channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
                    manager.createNotificationChannel(channel)
                    val notification = NotificationCompat.Builder(this, "channel_1")
                        .setContentTitle(getString(R.string.notification_title))
                        .setContentText(getString(R.string.notification_text))
                        .build()

                    startForeground(1, notification)

                }

                val bcr = object : BroadcastReceiver() {
                    override fun onReceive(context: Context?, intent: Intent?) {
                        Log.d(TAG, "onReceive context=${context} intent=${intent}")
                        if (intent?.action.equals(Intent.ACTION_SCREEN_ON)) {
                            Log.d(TAG, "onReceive ACTION_SCREEN_ON")
                            mScreenFlag = true
                            update()
                        } else if (intent?.action.equals(Intent.ACTION_SCREEN_OFF)) {
                            Log.d(TAG, "onReceive ACTION_SCREEN_OFF")
                            mScreenFlag = false
                        }
                    }
                }

                val filter = IntentFilter()
                filter.addAction(Intent.ACTION_SCREEN_OFF)
                filter.addAction(Intent.ACTION_SCREEN_ON)
                applicationContext.registerReceiver(bcr, filter)

                update()

                return Service.START_STICKY
            }

            override fun onCreate() {
                super.onCreate()
                Log.d(TAG, "onCreate")
            }

            override fun onDestroy() {
                super.onDestroy()
                mHandler.removeCallbacks(r)
                gStarted = false
                Log.d(TAG, "onDestroy")
            }

            override fun onBind(intent: Intent): IBinder? {
                return null
            }

        }


        class MyService2 : Service() {

            companion object{
                var priceDegisim3 = arrayListOf<Double>()
                var ListDelay : Long = 1000
                var ListCount = 0

            }


            private val TAG = MyService::class.java.simpleName
            private val mHandler = Handler()
            private var mScreenFlag = true
            var gStarted = false


            private fun updateWidget() {




                fun  isNetworkAvailbale():Boolean{
                    val conManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                    val internetInfo =conManager.activeNetworkInfo
                    return internetInfo!=null && internetInfo.isConnected
                }


              //  println("ilk kontrol  111111"+ sharedPreferences100.getBoolean("bool",true).toString())
                if (Kontrol == true || MyService.sharedPreferences300.getString("data200","").toString().isNotBlank()){
                //    println("if çalıştımı kontrol 2222" + sharedPreferences100.getBoolean("bool",true).toString())
                    ///////////////////////////////////////ilk plot işlemleri BASLANGICCCCCCCCCC//////////////////////////////////////
                    /////////////http client ile crypto geçmis listesi çekme /////////////////////////////////////////////
                    val client = OkHttpClient()
                   // val url = "https://min-api.cryptocompare.com/data/v2/histominute?fsym=${BASILAN_COİN.uppercase()}&tsym=USD&limit=1332&api_key=0646cc7b8a4d4b54926c74e0b20253b57fd4ee406df79b3d57d5439874960146"
                    val url = "https://min-api.cryptocompare.com/data/v2/histominute?fsym=${MyService.sharedPreferences300.getString("data200","").toString().uppercase()}&tsym=USD&limit=1332"
                    val request: Request = Request.Builder()
                        .url(url)
                        .build()
                    client.newCall(request).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: java.io.IOException) {
                            e.printStackTrace()
                        }
                        @Throws(java.io.IOException::class)
                        override fun onResponse(call: Call, response: Response) {
                            if (response.isSuccessful) {
                                var response = client.newCall(request).execute()
                                var jsonDataString = response.body?.string()
                                var json = JSONObject(jsonDataString)
                                var price = json.getJSONObject("Data")

                                for (k in 0..1332){
                                   var Global_Price = price.getJSONArray("Data").getJSONObject(k)["high"].toString()
                                    priceDegisim3.add(k,Global_Price.toDouble())

                                }


                            }
                        }
                    })



                    println(priceDegisim3)

                    if (ListCount >= 2){
                        ListDelay = 2000

                    }
                    ListCount++

                    if (ListCount >= 5){
                        ListDelay = 15000

                    }

                    val mViews = RemoteViews(packageName, R.layout.main_currency_widget)
                    val mThisWidget = ComponentName(this, MainCurrencyWidget::class.java)
                    val plot = XYPlot(this,"")
                    plot.layout(0, 0, 900, 600)


                    plot.backgroundPaint.color = Color.parseColor("#292556")
                    plot.setBackgroundColor(Color.parseColor("#292556"))
                    plot.graph.gridBackgroundPaint.color = Color.parseColor("#292556")
                    plot.graph.rangeSubGridLinePaint.color = Color.TRANSPARENT
                    plot.graph.domainGridLinePaint.color = Color.TRANSPARENT
                    plot.graph.rangeGridLinePaint.color = Color.TRANSPARENT
                    plot.graph.domainSubGridLinePaint.color = Color.TRANSPARENT


                    //   plot.getGraph().setPadding(0f,0f,600f,0f);
                    plot.setPlotMargins(0f, 0f, 0f, 0f)
                    //  plot.setBorderPaint(null);
                    plot.graph.backgroundPaint.color = Color.parseColor("#292556")
                    plot.borderPaint.color = Color.parseColor("#292556")
                    //     plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).getPaint().setColor(Color.TRANSPARENT);

                    //  plot.getGraph().marginBottom= 100f

                    val series1 : XYSeries = SimpleXYSeries((priceDegisim3),SimpleXYSeries.ArrayFormat.Y_VALS_ONLY,"")
                    val series1Format = LineAndPointFormatter(Color.CYAN,Color.TRANSPARENT,null,null)
                    series1Format.interpolationParams =
                        CatmullRomInterpolator.Params(40, CatmullRomInterpolator.Type.Centripetal)
                    plot.addSeries(series1,series1Format)
                    plot.addSeries(series1,series1Format)

                    val bitmap = Bitmap.createBitmap(900, 600, Bitmap.Config.ARGB_8888)
                    Bitmap.createBitmap(900, 600, Bitmap.Config.ARGB_8888)
                    plot.draw(Canvas(bitmap))

                    if (isNetworkAvailbale()){
                        mViews.setImageViewBitmap(R.id.plot_widget_img, bitmap)
                        plot.draw(Canvas(bitmap)).doAsync {
                            mViews.setImageViewBitmap(R.id.plot_widget_img, bitmap).doAsync {
                                priceDegisim3.clear()
                                plot.clear()
                            }
                            plot.clear()
                            priceDegisim3.clear()
                        }
                    }




                    val mManager = AppWidgetManager.getInstance(this@MyService2)
                    ///
                    mManager.updateAppWidget(mThisWidget, mViews)

                }









            }

            private val r = object : Runnable {
                override fun run() {
                    updateWidget()
                    if (mScreenFlag) {
                        val now = System.currentTimeMillis()
                        val delay = ListDelay - (now % 1000)
                        mHandler.postDelayed(this, delay)

                    }
                }
            }

            private fun update() {
                mHandler.post(r)

            }


            @SuppressLint("WrongConstant")
            override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
                super.onStartCommand(intent, flags, startId)
                Log.d(TAG, "onStart")
                if (gStarted) {
                    Log.d(TAG, "already started")
                    return Service.START_NOT_STICKY
                }
                Log.d(TAG, "start")
                gStarted = true

                if (Build.VERSION.SDK_INT >= 26) {
                    val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    val channel = NotificationChannel(
                        "channel_1",
                        getString(R.string.channel_name),
                        NotificationManager.IMPORTANCE_NONE
                    )
                    ///   notification.flags |= Notification.FLAG_AUTO_CANCEL | Notification.FLAG_SHOW_LIGHTS;
                    channel.lightColor = Color.GREEN
                    channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
                    manager.createNotificationChannel(channel)
                    val notification = NotificationCompat.Builder(this, "channel_1")
                        .setContentTitle(getString(R.string.notification_title))
                        .setContentText(getString(R.string.notification_text))
                        .build()


                    startForeground(1, notification)


                }

                val bcr = object : BroadcastReceiver() {
                    override fun onReceive(context: Context?, intent: Intent?) {
                        Log.d(TAG, "onReceive context=${context} intent=${intent}")
                        if (intent?.action.equals(Intent.ACTION_SCREEN_ON)) {
                            Log.d(TAG, "onReceive ACTION_SCREEN_ON")
                            mScreenFlag = true
                            update()
                        } else if (intent?.action.equals(Intent.ACTION_SCREEN_OFF)) {
                            Log.d(TAG, "onReceive ACTION_SCREEN_OFF")
                            mScreenFlag = false
                        }
                    }
                }

                val filter = IntentFilter()
                filter.addAction(Intent.ACTION_SCREEN_OFF)
                filter.addAction(Intent.ACTION_SCREEN_ON)
                applicationContext.registerReceiver(bcr, filter)

                update()

                return Service.START_STICKY
            }

            override fun onCreate() {
                super.onCreate()
                Log.d(TAG, "onCreate")
            }

            override fun onDestroy() {
                super.onDestroy()
                mHandler.removeCallbacks(r)
                gStarted = false
                Log.d(TAG, "onDestroy")
            }

            override fun onBind(intent: Intent): IBinder? {
                return null
            }

        }


    }




    internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int)
      {

          sharedPreferences99 = context.getSharedPreferences("xmod2",Context.MODE_PRIVATE)
          if (strli == "Purchase Status : Purchased"){
              sharedPreferences99.edit().putString("data2", strli).apply()
          }


          if(sharedPreferences99.getString("data2","").toString() == "Purchase Status : Purchased"){
              var Views = RemoteViews(context.packageName,R.layout.main_currency_widget)
              Views.setViewVisibility(R.id.oneday,View.VISIBLE)
              Views.setViewVisibility(R.id.free_time_counter , View.GONE)
              appWidgetManager.updateAppWidget(appWidgetId, Views)
          }else{

              object : CountDownTimer(60000,1000){

                  override fun onTick(millisUntilFinished: Long) {
                      val views = RemoteViews(context.packageName, R.layout.main_currency_widget)
                      views.setTextViewText(R.id.free_time_counter, "Free version ends after ${millisUntilFinished/1000} seconds")
                      views.setViewVisibility(R.id.oneday,View.GONE)
                      views.setViewVisibility(R.id.free_time_counter , View.VISIBLE)
                      appWidgetManager.updateAppWidget(appWidgetId, views)

                  }

                  override fun onFinish() {
                      var views2 = RemoteViews(context.packageName, R.layout.main_currency_widget)
                      views2.setViewVisibility(R.id.free_img,View.VISIBLE)
                      appWidgetManager.updateAppWidget(appWidgetId, views2)
                  }

              }.start()

          }






      }