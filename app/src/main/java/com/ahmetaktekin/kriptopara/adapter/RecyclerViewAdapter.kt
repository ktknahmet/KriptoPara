package com.ahmetaktekin.kriptopara.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ahmetaktekin.kriptopara.R
import com.ahmetaktekin.kriptopara.databinding.RecyclerrowBinding
import com.ahmetaktekin.kriptopara.model.KriptoPara

class RecyclerViewAdapter(private val cryptolist:ArrayList<KriptoPara>,private val listener:Listener): RecyclerView.Adapter<RecyclerViewAdapter.RowHolder>() {
    //listener oluşturma kullanıcı tıklaması için
    interface  Listener{
        fun itemclick(cryptoModel: KriptoPara)
    }

    private val colors:Array<String> = arrayOf("#13bd27","#29c1e1","#b129e1","#f6bd0c")
    class RowHolder(val binding: RecyclerrowBinding): RecyclerView.ViewHolder(binding.root) {
          //view : görünüm demek
        fun bind(cryptoModel:KriptoPara,colors:Array<String>,position: Int,listener:Listener){
            itemView.setBackgroundColor(Color.parseColor(colors[position%4]))
            itemView.setOnClickListener{
                listener.itemclick(cryptoModel)
            }
            binding.textIsim.text=cryptoModel.currency
            binding.textFiyat.text=cryptoModel.price
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowHolder {
     //burada parent kullanarak row tasarımını bağlıyoruz
        val binding = RecyclerrowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return RowHolder(binding)
    }

    override fun onBindViewHolder(holder: RowHolder, position: Int) {
      //hangi item ne verisi gösterecek
        holder.bind(cryptolist[position],colors,position,listener)

    }

    override fun getItemCount(): Int {
       return cryptolist.count()
    }
}