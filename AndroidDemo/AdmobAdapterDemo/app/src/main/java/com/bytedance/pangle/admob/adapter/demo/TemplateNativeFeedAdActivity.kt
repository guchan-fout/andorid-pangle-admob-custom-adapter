package com.bytedance.pangle.admob.adapter.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bytedance.pangle.admob.adapter.demo.adapter.ContentAdapter
import com.bytedance.pangle.admob.adapter.demo.model.CellContent
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class TemplateNativeFeedAdActivity : AppCompatActivity() {

    private lateinit var adapter: ContentAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    lateinit var mAdView: AdView
    lateinit var mContentlist: ArrayList<CellContent>

    val adPosition = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mContentlist = initList()

        adapter =
            ContentAdapter(mContentlist)
        viewManager = LinearLayoutManager(this)

        recycler_view.layoutManager = viewManager
        recycler_view.adapter = adapter
        recycler_view.setHasFixedSize(true)


        mAdView = AdView(this)
        mAdView.adSize = AdSize.MEDIUM_RECTANGLE
        mAdView.adUnitId = "ca-app-pub-2748478898138855/2016493981"
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        mAdView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                Timber.d("onAdLoaded")
                val content = CellContent()
                content.isAd = true
                content.adView = mAdView
                mContentlist.add(adPosition,content)
                Timber.d("size :" + mContentlist.size)
                adapter.notifyItemInserted(adPosition)
            }

            override fun onAdFailedToLoad(adError: Int) {
                Timber.d("onAdFailedToLoad")
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            override fun onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
                mAdView.destroy()
                mContentlist.removeAt(adPosition)
                adapter.notifyDataSetChanged()
            }
        }
    }

    // Called when leaving the activity
    public override fun onPause() {
        mAdView.pause()
        super.onPause()
    }

    // Called when returning to the activity
    public override fun onResume() {
        super.onResume()
        mAdView.resume()
    }

    // Called before the activity is destroyed
    public override fun onDestroy() {
        mAdView.destroy()
        super.onDestroy()
    }

    private fun initList(): ArrayList<CellContent> {
        val list =
            arrayOf("sunday", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday")
        val contentList: ArrayList<CellContent> = ArrayList()

        list.forEach { element ->
            val content = CellContent()
            content.content = element
            content.isAd = false
            contentList.add(content)
        }
        return contentList
    }

}