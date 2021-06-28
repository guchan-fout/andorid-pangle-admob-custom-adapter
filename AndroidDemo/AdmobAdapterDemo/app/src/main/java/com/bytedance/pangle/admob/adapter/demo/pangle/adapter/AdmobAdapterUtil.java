package com.bytedance.pangle.admob.adapter.demo.pangle.adapter;

import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdSdk;

import org.json.JSONArray;
import org.json.JSONObject;


public class AdmobAdapterUtil {

    public static String getUserData() {
        String result = "";
        try {
            JSONArray adData = new JSONArray();
            JSONObject mediationObject = new JSONObject();
            mediationObject.putOpt("name", "mediation");
            mediationObject.putOpt("value", "admob");
            adData.put(mediationObject);

            JSONObject adapterVersionObject = new JSONObject();
            adapterVersionObject.putOpt("name", "adapter_version");
            adapterVersionObject.putOpt("value", "1.3.0");
            adData.put(adapterVersionObject);
            result = adData.toString();
        } catch (Exception e) {

        }
        return result;
    }

    public static TTAdManager getPangleSdkManager() {
        TTAdSdk.getAdManager().setData(getUserData());
        return TTAdSdk.getAdManager();
    }
}
