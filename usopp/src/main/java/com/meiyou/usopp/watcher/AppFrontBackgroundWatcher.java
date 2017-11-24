package com.meiyou.usopp.watcher;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import com.meiyou.usopp.Usopp;
import com.meiyou.usopp.UsoppCompiler;
import com.meiyou.usopp.utils.LogUtils;

/**
 * Author: ice
 * Date: 17/11/24 10:09.
 */

public class AppFrontBackgroundWatcher implements Application.ActivityLifecycleCallbacks {
    private static final String TAG = "AppFrontBackgroundWatcher";
    private int mCount = 0;
    private boolean canSendBgEvent = true;

    private WatcherManager mWatcherManager;
    public AppFrontBackgroundWatcher(Context context) {
        mWatcherManager = new WatcherManager(context);
    }

    public void onActivityCreated(Activity activity, Bundle bundle) {
        try {

            String activityName = activity.getClass().getName();
            UsoppCompiler.getInstance().fireActivity(activityName);
            if(mWatcherManager.isAppBgAtDisk() && !mWatcherManager.isAppBgAtMemorry()){
                //有两种情况：
                //1、页面被回收：app被回收引起的页面重建（这种要发出回到前台事件）
                //2、app回到后台，被用户手动杀死app后，重新点击icon启动（这种不发出回到前台事件，目前只能根据launcher页面来进行判断）
                if(mWatcherManager.getLauncherActivityName().equals(activityName)) {
                    mWatcherManager.setAppBgAtDisk(false);
                }
            }
            handleSendFrontEvent(activity);
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    public void onActivityStarted(Activity activity) {
    }

    public void onActivityResumed(Activity activity) {
        ++this.mCount;
        LogUtils.d("AppFrontBackgroundWatcher", "==>onActivityResumed:" + activity.getClass().getSimpleName() + "==>mCount:" + this.mCount);
        this.handleSendFrontEvent(activity);
    }

    public void onActivityPaused(Activity activity) {
        --this.mCount;
        LogUtils.d("AppFrontBackgroundWatcher", "==>onActivityPaused:" + activity.getClass().getSimpleName() + "==>mCount:" + this.mCount);
    }

    public void onActivityStopped(Activity activity) {
        LogUtils.d("AppFrontBackgroundWatcher", "==>onActivityStopped:" + activity.getClass().getSimpleName() + "==>mCount:" + this.mCount);
        this.handleBackAppEvent(activity);
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    public void onActivityDestroyed(Activity activity) {
    }

    private void handleBackAppEvent(Activity activity) {
        try {
            if(this.mCount != 0) {
                return;
            }

            String activitName = activity.getClass().getName();
            if(mWatcherManager.isInIgnoreList(activitName)) {
                LogUtils.d("AppFrontBackgroundWatcher", "--> 处于忽略列表，不发送后台事件");
                return;
            }

            if(!this.canSendBgEvent) {
                return;
            }

            if(this.canSendBgEvent) {
                mWatcherManager.setAppBgAtDisk(true);
                UsoppCompiler.getInstance().fireAppBackground();
                LogUtils.d("AppFrontBackgroundWatcher", "--> 发送后台事件-->" + activitName);
                this.canSendBgEvent = false;
                //防暴力情况
                (new Handler()).postDelayed(new Runnable() {
                    public void run() {
                        canSendBgEvent = true;
                    }
                }, 1000L);
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    private void handleSendFrontEvent(Activity activity) {
        try {
            if(mWatcherManager.isAppBgAtDisk()){
                mWatcherManager.setAppBgAtDisk(false);
                String activityName = activity.getClass().getName();
                if(!mWatcherManager.isInIgnoreList(activityName)){
                    UsoppCompiler.getInstance().fireAppForground();
                    LogUtils.d(TAG,"fireAppForground:"+activityName);
                }else{
                    LogUtils.d(TAG,"isInIgnoreList:"+activityName);
                }
            }
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }


    public WatcherManager getWatcherManager() {
        return mWatcherManager;
    }
}