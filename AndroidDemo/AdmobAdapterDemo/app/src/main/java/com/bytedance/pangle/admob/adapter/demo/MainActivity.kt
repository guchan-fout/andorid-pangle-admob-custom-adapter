package com.bytedance.pangle.admob.adapter.demo

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bytedance.sdk.openadsdk.TTAdConfig
import com.bytedance.sdk.openadsdk.TTAdConstant
import com.bytedance.sdk.openadsdk.TTAdSdk
import com.github.ajalt.timberkt.BuildConfig
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
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

        TTAdSdk.init(
            this,
            TTAdConfig.Builder()
                .appId("5081617") // Please use your own appId, this is for demo
                .useTextureView(false) // Use TextureView to play the video. The default setting is SurfaceView, when the context is in conflict with SurfaceView, you can use TextureView
                .appName(packageName)
                .titleBarTheme(TTAdConstant.TITLE_BAR_THEME_DARK)
                .allowShowPageWhenScreenLock(true) // Allow or deny permission to display the landing page ad in the lock screen
                .coppa(0) // Fields to indicate whether you are a child or an adult ，0:adult ，1:child
                .setGDPR(0) //Fields to indicate whether you are protected by GDPR,  the value of GDPR : 0 close GDRP Privacy protection ，1: open GDRP Privacy protection
                .build()
        )
        if (BuildConfig.DEBUG) {
            // Turn it on during the testing phase, you can troubleshoot with the log, remove it after launching the app
            TTAdConfig.Builder().debug(true)

        }


        val list = arrayOf("Reward Video", "Native", "Interstitial")
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
                        val intent = Intent(this@MainActivity, NativeAdActivity::class.java)
                        startActivity(intent)
                    }
                    2 -> {
                        val intent = Intent(this@MainActivity, FullScreenVideoActivity::class.java)
                        startActivity(intent)
                    }
                    else -> {
                    }
                }
            }
        })
    }
}
