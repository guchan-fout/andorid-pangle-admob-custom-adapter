package com.bytedance.pangle.admob.adapter.demo.pangle.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.TTFullScreenVideoAd;
import com.google.android.gms.ads.mediation.MediationAdRequest;
import com.google.android.gms.ads.mediation.customevent.CustomEventInterstitial;
import com.google.android.gms.ads.mediation.customevent.CustomEventInterstitialListener;

import org.json.JSONObject;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Adapter for Full Screen Video ad, please set this with package name on Admob
 */
@SuppressWarnings("unused")
public class AdmobFullVideoAdapter implements CustomEventInterstitial {
    private static final String ADAPTER_NAME = "AdmobFullVideoAdapter";
    private static final String SLOT_ID = "slotID";
    private String mSlotID = "";

    private TTFullScreenVideoAd mttFullVideoAd;

    private CustomEventInterstitialListener admobAdListener;
    private Context context;

    private AtomicBoolean isLoadSuccess = new AtomicBoolean(false);


    @Override
    public void requestInterstitialAd(Context context,
                                      CustomEventInterstitialListener listener,
                                      String serverParameter,
                                      MediationAdRequest mediationAdRequest,
                                      Bundle customEventExtras) {

        this.context = context;
        this.admobAdListener = listener;
        //obtain ad placement_id from admob server
        mSlotID = getSlotId(serverParameter);
        Log.e("PlacementId:", mSlotID);

        //init Pangle ad manager
        TTAdManager mTTAdManager = TTAdSdk.getAdManager();
        TTAdNative mTTAdNative = mTTAdManager.createAdNative(context.getApplicationContext());

        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(mSlotID)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(1080, 1920)
                .setOrientation(TTAdConstant.VERTICAL)//required parameter ，Set how you wish the video ad to be displayed ,choose from TTAdConstant.HORIZONTAL or TTAdConstant.VERTICAL
                .build();

        mTTAdNative.loadFullScreenVideoAd(adSlot, mTTFullScreenAdListener);
    }

    @Override
    public void showInterstitial() {
        if (mttFullVideoAd != null && isLoadSuccess.get()) {
            this.mttFullVideoAd.showFullScreenVideoAd((Activity) this.context);
        }
    }

    private TTAdNative.FullScreenVideoAdListener mTTFullScreenAdListener = new TTAdNative.FullScreenVideoAdListener() {

        @Override
        public void onError(int i, String s) {
            isLoadSuccess.set(false);
            if (admobAdListener != null) {
                admobAdListener.onAdFailedToLoad(i);
            }
            Toast.makeText(context, "Pangle Ad Failed to load, error code is:" + i, Toast.LENGTH_SHORT).show();
            AdmobFullVideoAdapter.this.admobAdListener.onAdFailedToLoad(i);
        }

        @Override
        public void onFullScreenVideoAdLoad(TTFullScreenVideoAd ttFullScreenVideoAd) {
            isLoadSuccess.set(true);
            if (admobAdListener != null) {
                admobAdListener.onAdLoaded();
            }
            AdmobFullVideoAdapter.this.mttFullVideoAd = ttFullScreenVideoAd;
            AdmobFullVideoAdapter.this.mttFullVideoAd.setFullScreenVideoAdInteractionListener(new TTFullScreenVideoAd.FullScreenVideoAdInteractionListener() {
                @Override
                public void onAdShow() {
                    if (admobAdListener != null) {
                        admobAdListener.onAdOpened();
                    }
                }

                @Override
                public void onAdVideoBarClick() {
                    if (admobAdListener != null) {
                        admobAdListener.onAdClicked();
                    }
                }

                @Override
                public void onAdClose() {
                    if (admobAdListener != null) {
                        admobAdListener.onAdClosed();
                    }
                }

                @Override
                public void onVideoComplete() {

                }

                @Override
                public void onSkippedVideo() {
                }
            });
        }

        @Override
        public void onFullScreenVideoCached() {

        }
    };

    @Override
    public void onDestroy() {
        if (mttFullVideoAd != null) {
            mttFullVideoAd = null;
        }
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    private String getSlotId(String serverParameters) {
        if (serverParameters != null) {
            try {
                JSONObject jsonObject = new JSONObject(serverParameters);
                if (jsonObject.has(SLOT_ID)) {
                    return jsonObject.getString(SLOT_ID);
                }
            } catch (Throwable t) {
                Log.e(ADAPTER_NAME, "Could not parse malformed JSON: " + serverParameters);
            }
        }
        return"";
    }
}
