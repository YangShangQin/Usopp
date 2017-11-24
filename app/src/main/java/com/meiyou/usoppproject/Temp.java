package com.meiyou.usoppproject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author: lwh
 * Date: 17/8/18 08:14.
 */

public class Temp {


    private static JSONObject mJSONObject = new JSONObject();
    public static String getUsoppJson(){
        JSONArray arrayApplication = new JSONArray();
        JSONObject jsonApp = new JSONObject();
        arrayApplication.put(jsonApp);
        try {
            mJSONObject.put("application",arrayApplication);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mJSONObject.toString();
    }
}
