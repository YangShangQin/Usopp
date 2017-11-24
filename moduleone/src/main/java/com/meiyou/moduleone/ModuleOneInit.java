package com.meiyou.moduleone;

import android.util.Log;

import com.meiyou.usopp.annotations.Activity;
import com.meiyou.usopp.annotations.AppApplication;
import com.meiyou.usopp.annotations.AppBackground;
import com.meiyou.usopp.annotations.AppForground;
import com.meiyou.usopp.annotations.Thread;
import com.meiyou.usopp.annotations.Usopp;

/**
 *
 * Author: meetyou
 * Date: 17/8/16 15:49.
 */
@Usopp("ModuleOneInit")//标记此类为Usopp识别类
public class ModuleOneInit {

    private static final String TAG="ModuleOneInit";

    /**
     * App在Application UI线程初始化
     */
    @AppApplication
    public void runAtApplication(){
        Log.d(TAG,"runAtApplication");
    }

    @AppApplication
    @Thread
    public void runAtApplicationAtThread(){
        Log.d(TAG,"runAtApplicationAtThread");
    }

    /**
     * App退到后台
     * 如：按Home键
     * 这里也可以加 @Thread来进行线程处理
     */
    @AppBackground
    public void runWhenAppBackground(){
        Log.d(TAG,"runWhenAppBackground");
    }

    /**
     * App在从后台回到前台
     * 这里从后台回到前台不包括杀进程重启的情况，只适合按home键或者被其他第三方app覆盖的情况
     * 这里也可以加 @Thread来进行线程处理
     */
    @AppForground
    public void runWhenAppBacktoFront(){
        Log.d(TAG,"runWhenAppBacktoFront");
    }

    /**
     * App进入com.meiyou.usoppproject.MainActivity时需要做什么
     * 这里也可以加 @Thread来进行线程处理
     */
    @Activity("com.meiyou.usoppproject.MainActivity")
    public void runWhenAppStartMainActivity(){
        Log.d(TAG,"runWhenAppStartMainActivity");
    }
}
