package com.meiyou.usoppproject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import com.meiyou.usopp.Usopp;
import com.meiyou.usopp.UsoppCompiler;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*List<String> list = new ArrayList<>();
        list.add("ModuleInit");
        list.add("ModuleCommunity");
        UsoppCompiler.getInstance().init(list);
        UsoppCompiler.getInstance().fireApplication();*/

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Usopp.getInstance().printMethodCost(true);
            }
        },2000);
    }
}
