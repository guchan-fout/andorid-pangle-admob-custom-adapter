package com.bytedance.pangle.admob.adapter.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdCallback
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import timber.log.Timber

class RewardAdActivity : AppCompatActivity() {

    private lateinit var mRewardedAd: RewardedAd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reward_ad)
        loadRewardVideo()
    }

    private fun loadRewardVideo() {
        Timber.d("loadRewardVideo")

        val testID = "ca-app-pub-3940256099942544/5224354917"
        val realID = "ca-app-pub-2748478898138855/2595714578"
        mRewardedAd = RewardedAd(this, realID)
        mRewardedAd.loadAd(
            AdRequest.Builder().build(),
            object : RewardedAdLoadCallback() {
                override fun onRewardedAdLoaded() {
                    Toast.makeText(this@RewardAdActivity, "onRewardedAdLoaded", Toast.LENGTH_LONG)
                        .show()
                    Timber.d("onRewardedAdLoaded")
                    showRewardedVideo()
                }

                override fun onRewardedAdFailedToLoad(errorCode: Int) {
                    Toast.makeText(this@RewardAdActivity, "onRewardedAdFailedToLoad", Toast.LENGTH_LONG)
                        .show()
                    Timber.d("onRewardedAdFailedToLoad->${errorCode}")
                }
            }
        )
    }

    private fun showRewardedVideo() {
        if (mRewardedAd.isLoaded) {
            mRewardedAd.show(
                this,
                object : RewardedAdCallback() {
                    override fun onUserEarnedReward(
                        rewardItem: RewardItem
                    ) {
                        Toast.makeText(this@RewardAdActivity, "onUserEarnedReward", Toast.LENGTH_LONG).show()
                    }

                    override fun onRewardedAdClosed() {
                        Toast.makeText(this@RewardAdActivity, "onRewardedAdClosed", Toast.LENGTH_LONG).show()
                    }

                    override fun onRewardedAdFailedToShow(errorCode: Int) {
                        Toast.makeText(this@RewardAdActivity, "onRewardedAdFailedToShow", Toast.LENGTH_LONG).show()
                    }

                    override fun onRewardedAdOpened() {
                        Toast.makeText(this@RewardAdActivity, "onRewardedAdOpened", Toast.LENGTH_LONG).show()
                    }
                }
            )
        }
    }
}
