package com.sout.cryptocurrencytracker.View

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.billingclient.api.*
import com.sout.cryptocurrencytracker.API.API
import com.sout.cryptocurrencytracker.Model.StoreModel
import com.sout.cryptocurrencytracker.R
import com.sout.cryptocurrencytracker.Recycler.StoreRecycler
import com.xmod.blockchainwidget2.View.CustomDialogClass
import com.xmod.blockchainwidget2.View.Security.verifyPurchase
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_currency_widget.*
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.security.Security



class MainActivity : AppCompatActivity() ,PurchasesUpdatedListener,StoreRecycler.Listener{

    lateinit var compositeDisposable: CompositeDisposable
    private var BASE_URL = "https://raw.githubusercontent.com/"



    var purchaseStatus: TextView? = null
    var purchaseButton: Button? = null
    private var billingClient: BillingClient? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        sharedPreferences = this.getSharedPreferences("howTo",Context.MODE_PRIVATE);
        val cdd = CustomDialogClass(this@MainActivity)
        cdd.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        var startData = sharedPreferences.getBoolean("howTo",true)
        if (startData == false){

        }else{
            cdd.show()
        }


        compositeDisposable = CompositeDisposable();
        recyclerView2.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        if (isNetworkAvailbale()){
            getData()
        }




        purchaseStatus = findViewById<View>(R.id.purchase_status) as TextView
        purchaseButton = findViewById<View>(R.id.purchase_button) as Button

        billingClient = BillingClient.newBuilder(this)
            .enablePendingPurchases().setListener(this).build()
        billingClient!!.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    val queryPurchase = billingClient!!.queryPurchases(BillingClient.SkuType.INAPP)
                    val queryPurchases: List<Purchase>? = queryPurchase.purchasesList
                    if (queryPurchases != null && queryPurchases.size > 0) {
                        handlePurchases(queryPurchases)
                    }
                    //if purchase list is empty that means item is not purchased
                    //Or purchase is refunded or canceled
                    else{
                        savePurchaseValueToPref(false);
                    }
                }
            }

            override fun onBillingServiceDisconnected() {}
        })

        //item Purchased
        if (purchaseValueFromPref) {
            purchaseButton!!.visibility = View.GONE
            purchaseStatus!!.text = "Purchase Status : Purchased"
            checking = true
            strli = "Purchase Status : Purchased"



        }
        //item not Purchased
        else {
            purchaseButton!!.visibility = View.VISIBLE
            purchaseStatus!!.text = "Purchase Status : Not Purchased"

        }



    }


    fun getData(){
        var retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(API::class.java)

        compositeDisposable.add(retrofit.getData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::DataHandler)
        )

    }

    fun DataHandler(storeModel: List<StoreModel>){
        storeModel?.let {
            recyclerView2.adapter = StoreRecycler(it as ArrayList<StoreModel>,this@MainActivity)
        }
    }

    override fun onClickListener(storeModel: StoreModel) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(storeModel.link))
        startActivity(browserIntent)
    }






    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.settings, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {


            R.id.item1 -> {
                val intent = Intent()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                    intent.putExtra(Settings.EXTRA_APP_PACKAGE, this.getPackageName())
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
                    intent.putExtra("app_package", this.getPackageName())
                    intent.putExtra("app_uid", this.getApplicationInfo().uid)
                } else {
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    intent.addCategory(Intent.CATEGORY_DEFAULT)
                    intent.data = Uri.parse("package:" + this.getPackageName())
                }
                this.startActivity(intent)
                true
            }


            else -> super.onOptionsItemSelected(item)
        }


    }









    private val preferenceObject: SharedPreferences
        get() = applicationContext.getSharedPreferences(PREF_FILE, 0)

    private val preferenceEditObject: SharedPreferences.Editor
        get() {
            val pref: SharedPreferences = applicationContext.getSharedPreferences(PREF_FILE, 0)
            return pref.edit()
        }

    private val purchaseValueFromPref: Boolean
        get() = preferenceObject.getBoolean(PURCHASE_KEY, false)

    private fun savePurchaseValueToPref(value: Boolean) {
        preferenceEditObject.putBoolean(PURCHASE_KEY, value).commit()
    }

    //initiate purchase on button click
    fun purchase(view: View?) {
        //check if service is already connected

        if (billingClient!!.isReady) {
            initiatePurchase()
        }
        //else reconnect service
        else {
            billingClient = BillingClient.newBuilder(this).enablePendingPurchases().setListener(this).build()
            billingClient!!.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        initiatePurchase()
                    } else {
                        Toast.makeText(applicationContext, "Error " + billingResult.debugMessage, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onBillingServiceDisconnected() {}
            })
        }
    }

    private fun initiatePurchase() {

        val skuList: MutableList<String> = java.util.ArrayList()
        skuList.add(PRODUCT_ID)
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP)

        billingClient!!.querySkuDetailsAsync(params.build())
        { billingResult, skuDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                if (skuDetailsList != null && skuDetailsList.size > 0) {
                    val flowParams = BillingFlowParams.newBuilder()
                        .setSkuDetails(skuDetailsList[0])
                        .build()
                    billingClient!!.launchBillingFlow(this@MainActivity, flowParams)
                } else {
                    //try to add item/product id "purchase" inside managed product in google play console

                    Toast.makeText(applicationContext, "Purchase Item not Found", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(applicationContext,
                    " Error " + billingResult.debugMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: List<Purchase>?) {
        //if item newly purchased

        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            handlePurchases(purchases)
        }
        //if item already purchased then check and reflect changes
        else if (billingResult.responseCode == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
            val queryAlreadyPurchasesResult = billingClient!!.queryPurchases(BillingClient.SkuType.INAPP)
            val alreadyPurchases: List<Purchase>? = queryAlreadyPurchasesResult.purchasesList
            if (alreadyPurchases != null) {
                handlePurchases(alreadyPurchases)
            }
        }
        //if purchase cancelled
        else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            Toast.makeText(applicationContext, "Purchase Canceled", Toast.LENGTH_SHORT).show()
        }
        // Handle any other error msgs
        else {
            Toast.makeText(applicationContext, "Error " + billingResult.debugMessage, Toast.LENGTH_SHORT).show()
        }
    }

    fun handlePurchases(purchases: List<Purchase>) {
        for (purchase in purchases) {
            //if item is purchased

            if (PRODUCT_ID == purchase.sku && purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                if (!verifyValidSignature(purchase.originalJson, purchase.signature)) {
                    // Invalid purchase
                    // show error to user

                    Toast.makeText(applicationContext, "Error : Invalid Purchase", Toast.LENGTH_SHORT).show()
                    return
                }
                // else purchase is valid
                //if item is purchased and not acknowledged


                if (!purchase.isAcknowledged) {
                    val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(purchase.purchaseToken)
                        .build()
                    billingClient!!.acknowledgePurchase(acknowledgePurchaseParams, ackPurchase)
                }
                //else item is purchased and also acknowledged
                else {
                    // Grant entitlement to the user on item purchase
                    // restart activity

                    if (!purchaseValueFromPref) {
                        savePurchaseValueToPref(true)
                        strli = "Purchase Status : Purchased"
                        Toast.makeText(applicationContext, "Item Purchased >> $strli", Toast.LENGTH_SHORT).show()
                        checking = true
                        recreate()
                    }
                }
            }
            //if purchase is pending
            else if (PRODUCT_ID == purchase.sku && purchase.purchaseState == Purchase.PurchaseState.PENDING) {
                Toast.makeText(applicationContext,
                    "Purchase is Pending. Please complete Transaction", Toast.LENGTH_SHORT).show()
            }
            //if purchase is refunded or unknown
            else if (PRODUCT_ID == purchase.sku && purchase.purchaseState == Purchase.PurchaseState.UNSPECIFIED_STATE) {
                savePurchaseValueToPref(false)
                purchaseStatus!!.text = "Purchase Status : Not Purchased"
                purchaseButton!!.visibility = View.VISIBLE
                Toast.makeText(applicationContext, "Purchase Status Unknown", Toast.LENGTH_SHORT).show()
            }
        }
    }

    var ackPurchase = AcknowledgePurchaseResponseListener { billingResult ->
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            //if purchase is acknowledged
            // Grant entitlement to the user. and restart activity

            savePurchaseValueToPref(true)
            strli = "Purchase Status : Purchased"
            Toast.makeText(applicationContext, "Item Purchased >> $strli", Toast.LENGTH_SHORT).show()
            checking = true
            recreate()
        }
    }

    private fun verifyValidSignature(signedData: String, signature: String): Boolean {
        return try {
            com.xmod.blockchainwidget2.View.Security.verifyPurchase(base64Key, signedData, signature)
        } catch (e: IOException) {
            false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (billingClient != null) {
            billingClient!!.endConnection()
        }
    }

    companion object {
        lateinit var sharedPreferences: SharedPreferences
        const val PREF_FILE = "MyPref"
        const val PURCHASE_KEY = "xmod"
        const val PRODUCT_ID = "xmod"
        var checking = false
    }

    fun  isNetworkAvailbale():Boolean{
        val conManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val internetInfo =conManager.activeNetworkInfo
        return internetInfo!=null && internetInfo.isConnected
    }



}

