package com.bytedance.pangle.admob.adapter.demo.pangle.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.TTFullScreenVideoAd;
import com.google.android.gms.ads.mediation.MediationAdRequest;
import com.google.android.gms.ads.mediation.customevent.CustomEventInterstitial;
import com.google.android.gms.ads.mediation.customevent.CustomEventInterstitialListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Adapter for Full Screen Video ad, please set this with package name on Admob
 */
@SuppressWarnings("unused")
public class AdmobFullScreenVideoAdapter implements CustomEventInterstitial {
    private static final String ADAPTER_NAME = "AdmobFullScrnVA";
    private static final String PLACEMENT_ID = "placementID";
    private String mPlacementID = "";

    private TTFullScreenVideoAd mttFullVideoAd;

    private CustomEventInterstitialListener mAdmobAdListener;
    private Context mContext;

    private AtomicBoolean mIsLoadSuccess = new AtomicBoolean(false);


    @Override
    public void requestInterstitialAd(Context context,
                                      CustomEventInterstitialListener listener,
                                      String serverParameter,
                                      MediationAdRequest mediationAdRequest,
                                      Bundle customEventExtras) {

        this.mContext = context;
        this.mAdmobAdListener = listener;
        //obtain ad placement_id from admob server
        mPlacementID = getPlacementID(serverParameter);
        Log.d("PlacementId:", mPlacementID);

        if (mPlacementID.isEmpty()) {
            Log.e(ADAPTER_NAME, "mediation PlacementID is null");
            return;
        }

        //init Pangle ad manager
        TTAdManager mTTAdManager = TTAdSdk.getAdManager();

        //noinspection deprecation
        mTTAdManager.setData(getUserData());

        TTAdNative mTTAdNative = mTTAdManager.createAdNative(context.getApplicationContext());

        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(mPlacementID)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(1080, 1920)
                .setOrientation(TTAdConstant.VERTICAL)//required parameter ，Set how you wish the video ad to be displayed ,choose from TTAdConstant.HORIZONTAL or TTAdConstant.VERTICAL
                .build();

        mTTAdNative.loadFullScreenVideoAd(adSlot, mTTFullScreenAdListener);
    }

    @Override
    public void showInterstitial() {
        if (mttFullVideoAd != null && mIsLoadSuccess.get()) {
            this.mttFullVideoAd.showFullScreenVideoAd((Activity) this.mContext);
        } else {
            Log.e(ADAPTER_NAME, "Ad not loaded.");
        }
    }

    private TTAdNative.FullScreenVideoAdListener mTTFullScreenAdListener = new TTAdNative.FullScreenVideoAdListener() {

        @Override
        public void onError(int i, String s) {
            mIsLoadSuccess.set(false);
            if (mAdmobAdListener != null) {
                mAdmobAdListener.onAdFailedToLoad(i);
            }
            Log.e(ADAPTER_NAME, "Pangle Ad Failed to load, error code is:" + i + ", msg:" + s);
            AdmobFullScreenVideoAdapter.this.mAdmobAdListener.onAdFailedToLoad(i);
        }

        @Override
        public void onFullScreenVideoAdLoad(TTFullScreenVideoAd ttFullScreenVideoAd) {
            mIsLoadSuccess.set(true);
            if (mAdmobAdListener != null) {
                mAdmobAdListener.onAdLoaded();
            }
            AdmobFullScreenVideoAdapter.this.mttFullVideoAd = ttFullScreenVideoAd;
            AdmobFullScreenVideoAdapter.this.mttFullVideoAd.setFullScreenVideoAdInteractionListener(new TTFullScreenVideoAd.FullScreenVideoAdInteractionListener() {
                @Override
                public void onAdShow() {
                    if (mAdmobAdListener != null) {
                        mAdmobAdListener.onAdOpened();
                    }
                }

                @Override
                public void onAdVideoBarClick() {
                    if (mAdmobAdListener != null) {
                        mAdmobAdListener.onAdClicked();
                    }
                }

                @Override
                public void onAdClose() {
                    if (mAdmobAdListener != null) {
                        mAdmobAdListener.onAdClosed();
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

    private String getPlacementID(String serverParameters) {
        if (serverParameters != null) {
            try {
                JSONObject jsonObject = new JSONObject(serverParameters);
                if (jsonObject.has(PLACEMENT_ID)) {
                    return jsonObject.getString(PLACEMENT_ID);
                }
            } catch (Throwable t) {
                Log.e(ADAPTER_NAME, "Could not parse malformed JSON: " + serverParameters);
            }
        }
        return "";
    }

    private static String getUserData() {
        String result = "";
        try {
            JSONArray adData = new JSONArray();
            JSONObject mediationObject = new JSONObject();
            mediationObject.putOpt("name", "mediation");
            mediationObject.putOpt("value", "admob");
            adData.put(mediationObject);

            JSONObject adapterVersionObject = new JSONObject();
            adapterVersionObject.putOpt("name", "adapter_version");
            adapterVersionObject.putOpt("value", "1.2.1");
            adData.put(adapterVersionObject);
            result = adData.toString();
        } catch (Exception e) {

        }
        return result;
    }
}
