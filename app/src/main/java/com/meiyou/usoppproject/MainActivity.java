package com.meiyou.usoppproject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import com.meiyou.usopp.Usopp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<String> list = new ArrayList<>();
        list.add("ModuleInit");
        list.add("ModuleCommunity");
        Usopp.getInstance().init(list);
        Usopp.getInstance().fireApplication();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Usopp.printMethodCost();
            }
        },2000);
    }
}
