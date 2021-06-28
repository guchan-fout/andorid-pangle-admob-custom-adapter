package com.bytedance.pangle.admob.adapter.demo

import android.app.Activity
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bytedance.pangle.admob.adapter.demo.pangle.adapter.AdmobNativeFeedAdAdapter
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import kotlinx.android.synthetic.main.activity_native_ad.*
import timber.log.Timber

const val ADMOB_AD_UNIT_ID = "ca-app-pub-2748478898138855/5247133926"

class NativeAdActivity : AppCompatActivity() {

    var currentNativeAd: NativeAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_native_ad)
        loadUnifiedNativeAd()
    }

    private fun loadUnifiedNativeAd() {
        Timber.d("Load Native Ad.")

        val adLoader = AdLoader.Builder(this, ADMOB_AD_UNIT_ID)
            .forNativeAd { ad: NativeAd ->
                // Show the ad.
                var activityDestroyed = false
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    activityDestroyed = isDestroyed
                }
                if (activityDestroyed || isFinishing || isChangingConfigurations) {
                    ad.destroy()
                    return@forNativeAd
                }
                // You must call destroy on old ads when you are done with them,
                // otherwise you will have a memory leak.
                currentNativeAd?.destroy()
                currentNativeAd = ad
                val adView = layoutInflater
                    .inflate(R.layout.ad_unified, null) as NativeAdView
                populateNativeAdView(ad, adView)
                ad_frame.removeAllViews()
                ad_frame.addView(adView)
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    // Handle the failure by logging, altering the UI, and so on.
                    Timber.d("Ad failed to loade " + adError.message)
                }

                override fun onAdLoaded() {
                    Timber.d("Ad loaded")
                }
            })
            .withNativeAdOptions(
                NativeAdOptions.Builder()
                    // Methods in the NativeAdOptions.Builder class can be
                    // used here to specify individual options settings.
                    .build()
            )
            .build()

        adLoader.loadAd(AdRequest.Builder().build())

/*
        val builder = AdLoader.Builder(this, ADMOB_AD_UNIT_ID)

        builder.forNativeAd { nativeAd ->
            // OnUnifiedNativeAdLoadedListener implementation.
            // If this callback occurs after the activity is destroyed, you must call
            // destroy and return or you may get a memory leak.

            Timber.d("Load Native nativeAd here.")
            var activityDestroyed = false
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                activityDestroyed = isDestroyed
            }
            if (activityDestroyed || isFinishing || isChangingConfigurations) {
                nativeAd.destroy()
                return@forNativeAd
            }
            // You must call destroy on old ads when you are done with them,
            // otherwise you will have a memory leak.
            currentNativeAd?.destroy()
            currentNativeAd = nativeAd
            val adView = layoutInflater
                .inflate(R.layout.ad_unified, null) as NativeAdView
            populateNativeAdView(nativeAd, adView)
            ad_frame.removeAllViews()
            ad_frame.addView(adView)

            val adLoader = builder.withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    Toast.makeText(
                        this@NativeAdActivity, "Failed to load native ad with error",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onAdLoaded() {
                    Toast.makeText(
                        this@NativeAdActivity, "Native ad loaded.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }).build()

            adLoader.loadAd(AdRequest.Builder().build())
        }

 */
    }

    private fun populateNativeAdView(
        nativeAd: NativeAd,
        adView: NativeAdView
    ) {
        // Set the media view. Media content will be automatically populated in the media view once
        // adView.setNativeAd() is called.
        val mediaView: MediaView = adView.findViewById(R.id.ad_media)
        adView.mediaView = mediaView

        // Set other ad assets.
        adView.headlineView = adView.findViewById(R.id.ad_headline)
        adView.bodyView = adView.findViewById(R.id.ad_body)
        adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
        adView.iconView = adView.findViewById(R.id.ad_app_icon)
        //        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.starRatingView = adView.findViewById(R.id.ad_stars)
        adView.storeView = adView.findViewById(R.id.ad_store)
        adView.advertiserView = adView.findViewById(R.id.ad_advertiser)

        // The headline is guaranteed to be in every UnifiedNativeAd.
        (adView.headlineView as TextView).text = nativeAd.headline

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.body == null) {
            adView.bodyView.visibility = View.INVISIBLE
        } else {
            adView.bodyView.visibility = View.VISIBLE
            (adView.bodyView as TextView).text = nativeAd.body
        }
        if (nativeAd.callToAction == null) {
            adView.callToActionView.visibility = View.INVISIBLE
        } else {
            adView.callToActionView.visibility = View.VISIBLE
            (adView.callToActionView as Button).text = nativeAd.callToAction
        }
        if (nativeAd.icon == null) {
            adView.iconView.visibility = View.GONE
        } else {
            if (nativeAd.icon.drawable != null) {
                (adView.iconView as ImageView).setImageDrawable(
                    nativeAd.icon.drawable
                )
            } else {
                if (!isDestroy(this)) {
                    Glide.with(this).load(nativeAd.icon.uri).into((adView.iconView as ImageView))
                }
            }
            adView.iconView.visibility = View.VISIBLE
        }
        val imageView = adView.findViewById<ImageView>(R.id.tt_ad_logo)
        if (imageView != null && nativeAd.extras != null) {
            imageView.setImageBitmap(
                nativeAd.extras.getParcelable<Parcelable>(
                    AdmobNativeFeedAdAdapter.KEY_PANGLE_LOGO
                ) as Bitmap?
            )
        }
        if (nativeAd.store == null) {
            adView.storeView.visibility = View.INVISIBLE
        } else {
            adView.storeView.visibility = View.VISIBLE
            (adView.storeView as TextView).text = nativeAd.store
        }
        if (nativeAd.starRating == null || nativeAd.starRating < 3) {
            adView.starRatingView.visibility = View.INVISIBLE
        } else {
            (adView.starRatingView as RatingBar).rating = nativeAd.starRating.toFloat()
            adView.starRatingView.visibility = View.VISIBLE
        }
        if (nativeAd.advertiser == null) {
            adView.advertiserView.visibility = View.INVISIBLE
        } else {
            (adView.advertiserView as TextView).text = nativeAd.advertiser
            adView.advertiserView.visibility = View.VISIBLE
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad. The SDK will populate the adView's MediaView
        // with the media content from this native ad.
        adView.setNativeAd(nativeAd)
    }

    fun isDestroy(activity: Activity?): Boolean {
        return activity == null || activity.isFinishing || Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed
    }
}
