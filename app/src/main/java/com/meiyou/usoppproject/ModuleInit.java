package com.meiyou.usoppproject;

import com.meiyou.usopp.annotations.Activity;
import com.meiyou.usopp.annotations.AppApplication;
import com.meiyou.usopp.annotations.AppBackground;
import com.meiyou.usopp.annotations.AppForground;
import com.meiyou.usopp.annotations.ModuleApplication;
import com.meiyou.usopp.annotations.Thread;
import com.meiyou.usopp.annotations.Usopp;

/**
 * Author: lwh
 * Date: 17/8/16 16:21.
 */
@Usopp("ModuleCommunity")
public class ModuleInit {


    /**
     * Module在Application UI线程初始化
     */
    @ModuleApplication
    public void init(){
        try {
            java.lang.Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Module在Application 线程初始化
     */

    @AppApplication
    @Thread
    public void init2(){
        try {
            java.lang.Thread.sleep(25);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * App退到后台
     * 如：按Home键
     * 这里也可以加 @Thread来进行线程处理
     */
    @AppBackground
    public void init22(){

    }

    /**
     * App在从后台回到前台
     * 这里从后台回到前台不包括杀进程重启的情况，只适合按home键或者被其他第三方app覆盖的情况
     * 这里也可以加 @Thread来进行线程处理
     */
    @AppForground
    public void init23(){

    }

    /**
     * App进入com.meiyou.usoppproject.MainActivity时需要做什么
     * 这里也可以加 @Thread来进行线程处理
     */
    @Activity("com.meiyou.usoppproject.MainActivity")
    public void init233(){

    }

}
