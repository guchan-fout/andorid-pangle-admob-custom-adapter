package com.bytedance.pangle.admob.adapter.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import timber.log.Timber

class FullScreenVideoActivity : AppCompatActivity() {

    private lateinit var mInterstitialAd: InterstitialAd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen_video)

        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = "ca-app-pub-2748478898138855/6478494541"

        mInterstitialAd.loadAd(AdRequest.Builder().build())
        setListener()
    }

    private fun setListener() {
        mInterstitialAd.adListener = object: AdListener() {
            override fun onAdLoaded() {
                Timber.d("onAdLoaded")
                mInterstitialAd.show()
            }

            override fun onAdFailedToLoad(errorCode: Int) {
                Timber.d("onAdLoaded:${errorCode}")
            }

            override fun onAdOpened() {
                Timber.d("onAdOpened")
            }

            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            override fun onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            override fun onAdClosed() {
                // Code to be executed when the interstitial ad is closed.
            }
        }
    }

}
