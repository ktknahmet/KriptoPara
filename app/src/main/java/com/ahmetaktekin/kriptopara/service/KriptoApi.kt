package com.ahmetaktekin.kriptopara.service

import com.ahmetaktekin.kriptopara.model.KriptoPara
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET

interface KriptoApi {
    //get post update gibi metodlar yazıcaz
    @GET("prices?key=b96814a75b0877c3a528746a6a4fe3d0014b4d09")
    //fun getData():Call<List<KriptoPara>>
    //geriye liste halinde içinde kriptopara nesnesi olacak şekilde geri dönüş olacak

    fun getData():Observable<List<KriptoPara>>
}