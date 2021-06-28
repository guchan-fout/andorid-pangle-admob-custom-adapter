package com.bytedance.pangle.admob.adapter.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import timber.log.Timber

class RewardAdActivity : AppCompatActivity() {

    private var mRewardedAd: RewardedAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reward_ad)
        loadRewardVideo()
    }

    private fun loadRewardVideo() {
        Timber.d("loadRewardVideo")

        val testID = "ca-app-pub-3940256099942544/5224354917"
        val realID = "ca-app-pub-2748478898138855/2595714578"

        var adRequest = AdRequest.Builder().build()

        RewardedAd.load(this, realID, adRequest, object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Timber.d(adError?.message)
                mRewardedAd = null
            }

            override fun onAdLoaded(rewardedAd: RewardedAd) {
                Timber.d("Ad was loaded.")
                mRewardedAd = rewardedAd
                showRewardedVideo()
            }
        })

    }


    private fun showRewardedVideo() {

        if (mRewardedAd != null) {
            mRewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    Timber.d("Ad was dismissed.")
                    // Don't forget to set the ad reference to null so you
                    // don't show the ad a second time.
                    mRewardedAd = null
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                    Timber.d("Ad failed to show.")
                    // Don't forget to set the ad reference to null so you
                    // don't show the ad a second time.
                    mRewardedAd = null
                }

                override fun onAdShowedFullScreenContent() {
                    Timber.d("Ad showed fullscreen content.")
                    // Called when ad is dismissed.
                }
            }
        }
        mRewardedAd?.show(
            this,
            OnUserEarnedRewardListener() {
                fun onUserEarnedReward(rewardItem: RewardItem) {
                    var rewardAmount = rewardItem.amount
                    Timber.d("TAG", "User earned the reward.")
                }
            }
        )


    }
}
