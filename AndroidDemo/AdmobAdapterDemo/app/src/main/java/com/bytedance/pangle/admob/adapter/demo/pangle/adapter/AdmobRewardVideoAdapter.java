package com.bytedance.pangle.admob.adapter.demo.pangle.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.mediation.Adapter;
import com.google.android.gms.ads.mediation.InitializationCompleteCallback;
import com.google.android.gms.ads.mediation.MediationAdLoadCallback;
import com.google.android.gms.ads.mediation.MediationConfiguration;
import com.google.android.gms.ads.mediation.MediationRewardedAd;
import com.google.android.gms.ads.mediation.MediationRewardedAdCallback;
import com.google.android.gms.ads.mediation.MediationRewardedAdConfiguration;
import com.google.android.gms.ads.mediation.VersionInfo;
import com.google.android.gms.ads.rewarded.RewardItem;

import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Adapter for reward video, please set this with package name on Admob
 */
@SuppressWarnings("unused")
public class AdmobRewardVideoAdapter extends Adapter implements MediationRewardedAd {

    private static final String ADAPTER_NAME = "AdmobRewardVideoAdapter";
    private static final String PLACEMENT_ID = "placementID";
    private MediationAdLoadCallback<MediationRewardedAd, MediationRewardedAdCallback> mAdmobAdLoadCallback;
    private MediationRewardedAdCallback mAdmobRewardedAdCallback;
    private TTRewardVideoAd mttRewardVideoAd;
    private AtomicBoolean isLoadSuccess = new AtomicBoolean(false);

    private TTRewardVideoAd.RewardAdInteractionListener PangleRewardedInteractiveListener = new TTRewardVideoAd.RewardAdInteractionListener() {
        @Override
        public void onAdShow() {
            if (mAdmobRewardedAdCallback != null) {
                mAdmobRewardedAdCallback.onAdOpened();
                mAdmobRewardedAdCallback.onVideoStart();
            }
        }

        @Override
        public void onAdVideoBarClick() {
            if (mAdmobRewardedAdCallback != null) {
                mAdmobRewardedAdCallback.reportAdClicked();
            }
        }

        @Override
        public void onAdClose() {
            if (mAdmobRewardedAdCallback != null) {
                mAdmobRewardedAdCallback.onAdClosed();
            }
        }

        @Override
        public void onVideoComplete() {
            if (mAdmobRewardedAdCallback != null) {
                mAdmobRewardedAdCallback.onVideoComplete();
            }
        }

        @Override
        public void onVideoError() {
            if (mAdmobRewardedAdCallback != null) {
                mAdmobRewardedAdCallback.onAdFailedToShow(new AdError(AdRequest.ERROR_CODE_INVALID_REQUEST,
                        "onVideoError",
                        "onVideoError"));
            }
        }

        @Override
        //Call back of video ad completion and reward validation. The parameters are valid, the number of rewards, and the name of the reward.
        public void onRewardVerify(boolean rewardVerify, int rewardAmount, String rewardName, int errorCode, String errorMsg) {

            if (rewardVerify) {
                final String rewardType = rewardName;
                final int amount = rewardAmount;

                RewardItem rewardItem = new RewardItem() {
                    @Override
                    public String getType() {
                        return rewardType;
                    }

                    @Override
                    public int getAmount() {
                        return amount;
                    }
                };
                if (mAdmobRewardedAdCallback != null) {
                    mAdmobRewardedAdCallback.onUserEarnedReward(rewardItem);
                }

            }
        }

        @Override
        public void onSkippedVideo() {

        }
    };
    private TTAdNative.RewardVideoAdListener mRewardedAdListener = new TTAdNative.RewardVideoAdListener() {
        @Override
        public void onError(int i, String msg) {
            isLoadSuccess.set(false);
            Log.e(ADAPTER_NAME, "loadRewardVideoAd.........errorCode =" + i + ",msg=" + msg);
            if (mAdmobAdLoadCallback != null) {
                AdmobRewardVideoAdapter.this.mAdmobAdLoadCallback.onFailure(new AdError(i, msg, msg));
            }
        }

        @Override
        public void onRewardVideoAdLoad(TTRewardVideoAd ttRewardVideoAd) {
            isLoadSuccess.set(true);
            Log.d(ADAPTER_NAME, "onRewardVideoAdLoad.........onRewardVideoAdLoad");
            mttRewardVideoAd = ttRewardVideoAd;
            mttRewardVideoAd.setRewardAdInteractionListener(PangleRewardedInteractiveListener);
            if (mAdmobAdLoadCallback != null) {
                mAdmobRewardedAdCallback = mAdmobAdLoadCallback.onSuccess(AdmobRewardVideoAdapter.this);
            }
        }

        @Override
        public void onRewardVideoCached() {

        }
    };

    @Override
    public void initialize(Context context, InitializationCompleteCallback initializationCompleteCallback, List<MediationConfiguration> list) {
        Log.e(ADAPTER_NAME, "custom event  AdmobRewardVideoAdapter  initialize");
        if (!(context instanceof Activity)) {
            // Context not an Activity context, fail the initialization.
            initializationCompleteCallback.onInitializationFailed("Pangle SDK requires an Activity context to initialize");
            return;
        }

        initializationCompleteCallback.onInitializationSucceeded();
    }

    @Override
    public void loadRewardedAd(MediationRewardedAdConfiguration mediationRewardedAdConfiguration, MediationAdLoadCallback<MediationRewardedAd, MediationRewardedAdCallback> mediationAdLoadCallback) {

        // get Pangle slot id which was set on Admob Mediation
        Context context = mediationRewardedAdConfiguration.getContext();
        if (!(context instanceof Activity)) {
            String logMessage = "Pangle SDK requires an Activity context to load ads.";
            Log.w(ADAPTER_NAME, logMessage);
            mediationAdLoadCallback.onFailure(new AdError(AdRequest.ERROR_CODE_INVALID_REQUEST,
                    logMessage, logMessage));
            return;
        }

        Bundle serverParameters = mediationRewardedAdConfiguration.getServerParameters();
        String placementID = getPlacementId(serverParameters);

        Log.d(ADAPTER_NAME, "placementID:" + placementID);

        if (placementID.isEmpty()) {
            Log.e(ADAPTER_NAME, "mediation placementID is null");
            return;
        }

        this.mAdmobAdLoadCallback = mediationAdLoadCallback;

        //(notice : make sure the Pangle sdk had been initialized) obtain Pangle ad manager
        TTAdManager mTTAdManager = AdmobAdapterUtil.getPangleSdkManager();
        TTAdNative mTTAdNative = mTTAdManager.createAdNative(context.getApplicationContext());

        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(placementID)
                .setImageAcceptedSize(1080, 1920) //Set size to fit your ad slot size
                .build();

        //load ad
        mTTAdNative.loadRewardVideoAd(adSlot, mRewardedAdListener);
    }

    @Override
    public VersionInfo getVersionInfo() {
        return getSDKVersionInfo();
    }

    @Override
    public VersionInfo getSDKVersionInfo() {
        String versionString = TTAdSdk.getAdManager().getSDKVersion();
        String splits[] = versionString.split("\\.");
        int major = Integer.parseInt(splits[0]);
        int minor = Integer.parseInt(splits[1]);
        int micro = Integer.parseInt(splits[2]) * 100 + Integer.parseInt(splits[3]);
        return new VersionInfo(major, minor, micro);
    }

    @Override
    public void showAd(Context context) {
        if (!(context instanceof Activity)) {
            // Context not an Activity context, fail the initialization.
            Log.e(ADAPTER_NAME, "Pangle SDK requires an Activity context to initialize");
            return;
        }
        if (mttRewardVideoAd != null && isLoadSuccess.get()) {
            mttRewardVideoAd.showRewardVideoAd((Activity) context);
        }
    }

    private String getPlacementId(Bundle serverParameters) {
        if (serverParameters != null) {
            try {
                String jsonParams = serverParameters.getString("parameter");
                if (jsonParams != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(jsonParams);
                        if (jsonObject.has(PLACEMENT_ID)) {
                            return jsonObject.getString(PLACEMENT_ID);
                        }
                    } catch (Throwable t) {
                        Log.e(ADAPTER_NAME, "Could not parse malformed JSON: " + jsonParams);
                    }
                }

            } catch (Exception e) {
                Log.e(ADAPTER_NAME, "loadRewardedAd() exception: " + e);
            }
        }

        return "";
    }
}
