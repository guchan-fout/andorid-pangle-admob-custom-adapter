package com.bytedance.pangle.admob.adapter.demo

import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.*
import kotlinx.android.synthetic.main.activity_adaptive_banner_ad.*
import timber.log.Timber

class AdaptiveBannerAdActivity : AppCompatActivity() {

    private lateinit var adaptiveBannerView: AdView

    private var initialLayoutComplete = false

    // Determine the screen width (less decorations) to use for the ad width.
    // If the ad hasn't been laid out, default to the full screen width.
    private val adSize: AdSize
        get() {
            val display = windowManager.defaultDisplay
            val outMetrics = DisplayMetrics()
            display.getMetrics(outMetrics)

            val density = outMetrics.density

            var adWidthPixels = ad_view_container.width.toFloat()
            if (adWidthPixels == 0f) {
                adWidthPixels = outMetrics.widthPixels.toFloat()
            }

            val adWidth = (adWidthPixels / density).toInt()
            return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adaptive_banner_ad)

        adaptiveBannerView = AdView(this)
        ad_view_container.setBackgroundColor(Color.RED)

        ad_view_container.addView(adaptiveBannerView)
        // Since we're loading the banner based on the adContainerView size, we need to wait until this
        // view is laid out before we can get the width.
        ad_view_container.viewTreeObserver.addOnGlobalLayoutListener {
            if (!initialLayoutComplete) {
                initialLayoutComplete = true
                loadBanner()
            }
        }

        adaptiveBannerView.adListener = object: AdListener() {
            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Timber.d("onAdLoaded admob")
            }

            override fun onAdFailedToLoad(adError : LoadAdError) {
                // Code to be executed when an ad request fails.
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                Timber.d("onAdOpened admob")
            }

            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
                Timber.d("onAdClicked admob")
            }

            override fun onAdClosed() {
                Timber.d("onAdClosed admob")
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        }

    }

    private fun loadBanner() {
        adaptiveBannerView.adUnitId = AD_UNIT_ID

        adaptiveBannerView.adSize = adSize

        // Create an ad request.
        val adRequest = AdRequest.Builder().build()

        // Start loading the ad in the background.
        adaptiveBannerView.loadAd(adRequest)
    }

    companion object {
        // This is an ad unit ID for a test ad. Replace with your own banner ad unit ID.
        private val AD_UNIT_ID = "ca-app-pub-2748478898138855/8082411094"
    }
}