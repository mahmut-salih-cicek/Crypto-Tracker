package com.sout.cryptocurrencytracker.API



import com.sout.cryptocurrencytracker.Model.StoreModel
import io.reactivex.Observable
import retrofit2.http.GET


interface API {
    ///https://raw.githubusercontent.com/mahmut-salih-cicek/Play_Store_Picture/main/app.json
    @GET("mahmut-salih-cicek/Play_Store_Picture/main/app.json")
    fun getData():Observable<List<StoreModel>>
}