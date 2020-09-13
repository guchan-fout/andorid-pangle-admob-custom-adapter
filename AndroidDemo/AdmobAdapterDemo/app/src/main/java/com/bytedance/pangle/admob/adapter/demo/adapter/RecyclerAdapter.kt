package com.bytedance.pangle.admob.adapter.demo.adapter

import android.view.LayoutInflater.from
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bytedance.pangle.admob.adapter.demo.R
import kotlinx.android.synthetic.main.recyclerview_item.view.*

class RecyclerAdapter (private val customList: Array<String>) : RecyclerView.Adapter<RecyclerAdapter.RecyclerAdapterViewHolder>(){

    lateinit var listener: OnItemClickListener

    class RecyclerAdapterViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapterViewHolder {

        val item = from(parent.context).inflate(R.layout.recyclerview_item, parent, false)
        return RecyclerAdapterViewHolder(
            item
        )
    }

    override fun getItemCount(): Int {
        return customList.size
    }


    override fun onBindViewHolder(holder: RecyclerAdapterViewHolder, position: Int) {
        holder.view.item_name.text = customList[position]

        holder.view.setOnClickListener {
            listener.onItemClickListener(it, position, customList[position])
        }
    }

    interface OnItemClickListener{
        fun onItemClickListener(view: View, position: Int, clickedText: String)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }

}
