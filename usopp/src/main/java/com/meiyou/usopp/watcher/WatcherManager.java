package com.meiyou.usopp.watcher;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Author: ice
 * Date: 17/11/24 10:20.
 */

public class WatcherManager {

    private static final String SP_WATCHER = "android-app-watcher-sp";
    private static final String SP_WATCHER_VALUE = "android-app-watcher-sp-value";
    private Context mContext;
    //前后台
    private boolean isAppBgAtMemorry;
    //启动页名称
    private String mLauncherActivityName="";

    private List<String> mIgnoreList = new ArrayList();

    public WatcherManager(Context context){
        mContext = context.getApplicationContext();
    }
    /***前后台标识****/
    public void setAppBgAtDisk(boolean flag){
        setAppBgAtMemorry(flag);
        SharedPreferences sp = mContext.getSharedPreferences(SP_WATCHER, Context.MODE_PRIVATE);
        sp.edit().putBoolean(SP_WATCHER_VALUE, flag).apply();
    }

    public boolean isAppBgAtDisk() {
        SharedPreferences sp = mContext.getSharedPreferences(SP_WATCHER, Context.MODE_PRIVATE);
        return sp.getBoolean(SP_WATCHER_VALUE,false);
    }

    public void setAppBgAtMemorry(boolean flag){
        isAppBgAtMemorry  = flag;
    }

    public boolean isAppBgAtMemorry() {
        return isAppBgAtMemorry;
    }
    /***Launcher页面****/
    public String getLauncherActivityName() {
        return mLauncherActivityName;
    }

    public void setLauncherActivityName(String launcherActivityName) {
        mLauncherActivityName = launcherActivityName;
    }


    /***忽略页面****/
    public void addIgnoreActivity(String activitySimpleName) {
        if(activitySimpleName!=null && !this.mIgnoreList.contains(activitySimpleName)) {
            this.mIgnoreList.add(activitySimpleName);
        }

    }

    public void addIgnoreActivityList(List<String> listActivityName) {
        if(listActivityName != null && listActivityName.size() > 0) {
            this.mIgnoreList.addAll(listActivityName);
        }

    }

    public void removeIgnoreActivity(String activityName) {
        try {
            if(activityName!=null) {
                this.mIgnoreList.remove(activityName);
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    public List<String> getIgnoreList() {
        return this.mIgnoreList;
    }

    public boolean isInIgnoreList(String activityName) {
        try {
            Iterator var2 = this.mIgnoreList.iterator();

            while(var2.hasNext()) {
                String value = (String)var2.next();
                if(value.equals(activityName)) {
                    return true;
                }
            }
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return false;
    }
}
