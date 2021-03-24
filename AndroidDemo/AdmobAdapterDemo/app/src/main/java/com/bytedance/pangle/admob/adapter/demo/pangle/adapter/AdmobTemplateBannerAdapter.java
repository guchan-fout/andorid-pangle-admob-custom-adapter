package com.bytedance.pangle.admob.adapter.demo.pangle.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdDislike;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.mediation.MediationAdRequest;
import com.google.android.gms.ads.mediation.customevent.CustomEventBanner;
import com.google.android.gms.ads.mediation.customevent.CustomEventBannerListener;

import org.json.JSONObject;

import java.util.List;


/**
 * Adapter for template banner, please set this with package name on Admob
 */
@SuppressWarnings("unused")
@SuppressLint("LongLogTag")
public class AdmobTemplateBannerAdapter implements CustomEventBanner {

    private static final String ADAPTER_NAME = "AdmobTemplateBannerAdapter";

    private static final String PLACEMENT_ID = "placementID";

    private String mPlacementID = "";

    private TTNativeExpressAd mTTNativeExpressAd;

    private CustomEventBannerListener mCustomEventBannerListener;

    private Context mContext = null;

    @Override
    public void requestBannerAd(Context context, CustomEventBannerListener customEventBannerListener, String serverParameter, AdSize adSize, MediationAdRequest mediationAdRequest, Bundle bundle) {
        this.mContext = context;
        this.mCustomEventBannerListener = customEventBannerListener;
        this.mPlacementID = getPlacementId(serverParameter);
        Log.d("PlacementId:", mPlacementID + ",adSize-getWidth=" + adSize.getWidth() + ",getHeight=" + adSize.getHeight());
        if (mPlacementID.isEmpty()) {
            Log.e(ADAPTER_NAME, "mediation PlacementID is null");
            return;
        }

        //(notice : make sure the Pangle sdk had been initialized) obtain Pangle ad manager
        TTAdManager mTTAdManager = AdmobAdapterUtil.getPangleSdkManager();
        TTAdNative mTTAdNative = mTTAdManager.createAdNative(context.getApplicationContext());

        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(mPlacementID)
                .setSupportDeepLink(true)
                .setAdCount(1)
                .setExpressViewAcceptedSize(adSize.getWidth(), adSize.getHeight())
                .build();
        mTTAdNative.loadBannerExpressAd(adSlot, mTTBannerNativeExpressAdListener);
        Log.d(ADAPTER_NAME, "loadBannerExpressAd.....");
    }

    @Override
    public void onDestroy() {
        if (mTTNativeExpressAd != null) {
            mTTNativeExpressAd.destroy();
        }
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }


    private TTAdNative.NativeExpressAdListener mTTBannerNativeExpressAdListener = new TTAdNative.NativeExpressAdListener() {

        @Override
        public void onError(int code, String message) {
            Log.e(ADAPTER_NAME, " onBannerFailed.-code=" + code + "," + message);
            if (mCustomEventBannerListener != null) {
                mCustomEventBannerListener.onAdFailedToLoad(new AdError(code, message, message));
            }
        }

        @Override
        public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
            Log.d(ADAPTER_NAME, " onNativeExpressAdLoad");

            if (ads == null || ads.size() == 0) {
                return;
            }
            mTTNativeExpressAd = ads.get(0);
            mTTNativeExpressAd.setExpressInteractionListener(mExpressAdInteractionListener);
            bindDislike(mTTNativeExpressAd);
            mTTNativeExpressAd.render();
        }
    };

    private TTNativeExpressAd.ExpressAdInteractionListener mExpressAdInteractionListener = new TTNativeExpressAd.ExpressAdInteractionListener() {
        @Override
        public void onAdClicked(View view, int type) {
            Log.d(ADAPTER_NAME, " onAdClicked");
            if (mCustomEventBannerListener != null) {
                mCustomEventBannerListener.onAdClicked();
            }
        }

        @Override
        public void onAdShow(View view, int type) {
            Log.d(ADAPTER_NAME, " onAdShow");
            if (mCustomEventBannerListener != null) {
                mCustomEventBannerListener.onAdOpened();
            }
        }

        @Override
        public void onRenderFail(View view, String msg, int code) {
            Log.e(ADAPTER_NAME, " onBannerFailed.-code=" + code + "," + msg);
            if (mCustomEventBannerListener != null) {
                mCustomEventBannerListener.onAdFailedToLoad(new AdError(code, msg, msg));
            }
        }

        @Override
        public void onRenderSuccess(View view, float width, float height) {
            Log.d(ADAPTER_NAME, " onRenderSuccess");
            if (mCustomEventBannerListener != null) {
                //render success add view to google view
                mCustomEventBannerListener.onAdLoaded(view);
            }

        }
    };

    private String getPlacementId(String serverParameters) {
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

    private void bindDislike(TTNativeExpressAd ad) {
        ad.setDislikeCallback((Activity) mContext, new TTAdDislike.DislikeInteractionCallback() {
            @Override
            public void onSelected(int position, String value) {
                Log.d(ADAPTER_NAME, " onSelected::" + position);
                mCustomEventBannerListener.onAdClosed();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onRefuse() {

            }
        });
    }
}
