package com.meiyou.usoppproject;

import android.app.Application;

import com.meiyou.usopp.Usopp;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: ice
 * Date: 17/11/24 11:06.
 */

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        List<String> list = new ArrayList<>();
        list.add("ModuleOneInit");
        list.add("ModuleTwoInit");
        Usopp.getInstance().init(this,list);

        //Usopp.getInstance().getWatcherManager().addIgnoreActivity(MainActivity.class.getName());

        //Usopp.getInstance().getWatcherManager().setLauncherActivityName(MainActivity.class.getName());

    }
}
