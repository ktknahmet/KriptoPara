package com.ahmetaktekin.kriptopara.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ahmetaktekin.kriptopara.adapter.RecyclerViewAdapter
import com.ahmetaktekin.kriptopara.databinding.ActivityMainBinding
import com.ahmetaktekin.kriptopara.model.KriptoPara
import com.ahmetaktekin.kriptopara.service.KriptoApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

// b96814a75b0877c3a528746a6a4fe3d0014b4d09
class MainActivity : AppCompatActivity() ,RecyclerViewAdapter.Listener{
    private lateinit var binding: ActivityMainBinding
    private var recyclerviewAdapter: RecyclerViewAdapter? =null
    private val BASEURL = "https://api.nomics.com/v1/"
    //gelen verileri alacağımız arraylist
    private var cryptoModel : ArrayList<KriptoPara>?=null

    private var compositeDispossible:CompositeDisposable?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val layoutManager: RecyclerView.LayoutManager= LinearLayoutManager(this)
        binding.recyclerview.layoutManager=layoutManager
        compositeDispossible = CompositeDisposable()
        loadData()

    }

    private fun loadData(){
        val retrofit = Retrofit.Builder()
            .baseUrl(BASEURL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                //rx java kullanacağımızı belirttik
            .build().create(KriptoApi::class.java)
        compositeDispossible?.add(retrofit.getData()
                //obje oluşturduk
                //farklı farklı istekleri buraya ekledik arka planda dinlerken ön planda işleme aldık
               //farklı farklı birsüsü get post işlemi olduğunda abonelik gibi çalışır
               //abone istenilen get post değiştiği zaman dinleme ve gösterilme işlemi yapar
            .subscribeOn(Schedulers.io())//veriye kayıt olma işlemini arka planda yaparız
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handleResponse))
        //compositeDispossible gelen veriyi dinler arka planda işler

       val service = retrofit.create(KriptoApi::class.java)
       //service api ile retrofiti birbirine bağlamak için
       val call = service.getData()

      call.enqueue(object:Callback<List<KriptoPara>>{
           override fun onResponse(
               call: Call<List<KriptoPara>>,
               response: Response<List<KriptoPara>>
           ) {
               //cevap varsa
               if(response.isSuccessful){
                   response.body()?.let {
                       //eğer bu null değilse
                       cryptoModel=ArrayList(it)
                       cryptoModel?.let{
                           recyclerviewAdapter=RecyclerViewAdapter(cryptoModel!!,this@MainActivity)
                           binding.recyclerview.adapter=recyclerviewAdapter
                       }

                       for(cryptoModel:KriptoPara in cryptoModel!!){
                           println(cryptoModel.currency)
                           println(cryptoModel.price)
                       }
                   }
               }
           }

           override fun onFailure(call: Call<List<KriptoPara>>, t: Throwable) {
             //hata varsa
               t.printStackTrace()
           }

       })

    }

    override fun itemclick(cryptoModel: KriptoPara) {
        println("tıklandı:${cryptoModel.currency}")
    }
    private fun handleResponse(cryptoList:List<KriptoPara>){
        cryptoModel=ArrayList(cryptoList)
        cryptoModel?.let{
            recyclerviewAdapter=RecyclerViewAdapter(cryptoModel!!,this@MainActivity)
            binding.recyclerview.adapter=recyclerviewAdapter
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDispossible?.clear()
    }
}