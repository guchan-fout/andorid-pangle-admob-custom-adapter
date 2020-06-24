package com.bytedance.pangle.admob.adapter.demo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.ajalt.timberkt.BuildConfig
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber


class MainActivity : AppCompatActivity() {

    private lateinit var mAdView: AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(
            this
        ) {
            Timber.d("initialize AdMob")
        }

        val list = arrayOf("Reward Video", "Banner", "Interstitial")
        val adapter = RecyclerAdapter(list)
        val layoutManager = LinearLayoutManager(this)

        recycler_view.layoutManager = layoutManager
        recycler_view.adapter = adapter
        recycler_view.setHasFixedSize(true)

        adapter.setOnItemClickListener(object : RecyclerAdapter.OnItemClickListener {
            override fun onItemClickListener(view: View, position: Int, clickedText: String) {

                when (position) {
                    0 -> {
                        val intent = Intent(this@MainActivity, RewardAdActivity::class.java)
                        startActivity(intent)
                    }
                    1 -> {
                        loadBannerAd()
                    }
                    2 -> {
                    }
                    else -> {
                    }
                }
            }
        })
    }


    fun loadBannerAd() {
        mAdView = findViewById(R.id.ad_view)
        val adRequest = AdRequest.Builder().build()


        mAdView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                Timber.d("onAdLoaded")
            }

            override fun onAdFailedToLoad(errorCode: Int) {
                // Code to be executed when an ad request fails.
                Timber.d("onAdFailedToLoad ${errorCode}")
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                Timber.d("onAdOpened")
            }

            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
                Timber.d("onAdClicked")

            }

            override fun onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                Timber.d("onAdLeftApplication")
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
                Timber.d("onAdClosed")
            }
        }

        mAdView.loadAd(adRequest)
    }
}
