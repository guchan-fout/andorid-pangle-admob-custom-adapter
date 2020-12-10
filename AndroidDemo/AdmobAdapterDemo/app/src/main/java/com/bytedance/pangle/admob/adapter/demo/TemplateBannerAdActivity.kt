package com.bytedance.pangle.admob.adapter.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import kotlinx.android.synthetic.main.activity_template_banner.*

class TemplateBannerAdActivity : AppCompatActivity() {

    private lateinit var mAdViewRectangle: AdView
    private lateinit var mAdViewBanner: AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_template_banner)

        mAdViewRectangle = findViewById(R.id.adView300_250)
        mAdViewBanner = findViewById(R.id.adView320_50)

        val adRequest = AdRequest.Builder().build()

        load300btn.setOnClickListener {
            // Start loading the ad in the background.
            mAdViewRectangle.loadAd(adRequest)
            mAdViewRectangle.adListener = object : AdListener() {
                //implement listener for admob here
            }
        }

        load320btn.setOnClickListener {
            // Start loading the ad in the background.
            mAdViewBanner.loadAd(adRequest)
            mAdViewBanner.adListener = object : AdListener() {
                //implement listener for admob here
            }
        }


    }

    // Called when leaving the activity
    public override fun onPause() {
        mAdViewRectangle.pause()
        mAdViewBanner.pause()
        super.onPause()
    }

    // Called when returning to the activity
    public override fun onResume() {
        super.onResume()
        mAdViewRectangle.resume()
        mAdViewBanner.resume()
    }

    // Called before the activity is destroyed
    public override fun onDestroy() {
        mAdViewRectangle.destroy()
        mAdViewBanner.destroy()
        super.onDestroy()
    }
}