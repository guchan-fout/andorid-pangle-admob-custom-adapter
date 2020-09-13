package com.bytedance.pangle.admob.adapter.demo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bytedance.pangle.admob.adapter.demo.R
import com.bytedance.pangle.admob.adapter.demo.model.CellContent
import kotlinx.android.synthetic.main.recyclerview_item.view.*
import kotlinx.android.synthetic.main.template_native_feed_ad.view.*
import timber.log.Timber


class ContentAdapter(private val contentList: ArrayList<CellContent>) : RecyclerView.Adapter<RecyclerAdapter.RecyclerAdapterViewHolder>(){

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerAdapter.RecyclerAdapterViewHolder {
        val view: View
        return if (viewType == TYPE_NORMAL) { // for call layout
            view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.recyclerview_item, viewGroup, false)
            RecyclerAdapter.RecyclerAdapterViewHolder(view)
        } else {
            view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.template_native_feed_ad, viewGroup, false)
            RecyclerAdapter.RecyclerAdapterViewHolder(view)
        }

    }

    override fun onBindViewHolder(
        holder: RecyclerAdapter.RecyclerAdapterViewHolder,
        position: Int
    ) {
        if (getItemViewType(position) == TYPE_NORMAL) {
            holder.view.item_name.text = contentList[position].content
        } else {
            holder.view.template_ad_view.addView(contentList[position].adView)
        }
    }


    override fun getItemCount(): Int {
        return contentList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (!contentList[position].isAd) {
            TYPE_NORMAL
        } else {
            Timber.d("isAD!!")
            TYPE_AD
        }
    }

    companion object {
        private const val TYPE_NORMAL = 1
        private const val TYPE_AD = 2
    }
}