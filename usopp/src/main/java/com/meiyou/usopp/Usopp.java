package com.meiyou.usopp;

import android.app.Application;

import com.meiyou.usopp.watcher.AppFrontBackgroundWatcher;
import com.meiyou.usopp.watcher.WatcherManager;

import java.util.List;

/**
 * Author: ice
 * Date: 17/11/24 10:03.
 */

public class Usopp {
    private static final String TAG = "Usopp";
    private static Usopp instance;
    private AppFrontBackgroundWatcher mAppFrontBackgroundWatcher;
    public static Usopp getInstance() {
        if (instance == null) {
            synchronized (Usopp.class) {
                if (instance == null)
                    instance = new Usopp();
            }
        }
        return instance;
    }

    /**
     * 初始化
     * @param application
     * @param listUsoppName，初始化的模块名称
     * @return
     */
    public boolean init(Application application,List<String> listUsoppName){
        if(application==null)
            return false;
        //register app watcher
        mAppFrontBackgroundWatcher = new AppFrontBackgroundWatcher(application.getApplicationContext());
        application.registerActivityLifecycleCallbacks(mAppFrontBackgroundWatcher);
        //find usopp class
        UsoppCompiler.getInstance().init(listUsoppName);
        //invoke application oncreate
        UsoppCompiler.getInstance().fireApplication();
        return true;
    }

    /**
     * 拓展管理类
     * @return
     */
    public WatcherManager getWatcherManager() {
        if(mAppFrontBackgroundWatcher!=null)
            return mAppFrontBackgroundWatcher.getWatcherManager();
        return null;
    }


    /**
     * 打印方法耗时
     * @param isIncludeThreadMethod
     */
    public void printMethodCost(boolean isIncludeThreadMethod) {
        UsoppCompiler.getInstance().printMethodCost(isIncludeThreadMethod);
    }

}
