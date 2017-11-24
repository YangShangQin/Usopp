package com.meiyou.usopp;

import android.util.Log;

import com.meiyou.usopp.data.TimerData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 耗时统计
 * Author: lwh
 * Date: 17/9/19 16:58.
 */

class UsoppTimerCounter {

    private static final String TAG = "UsoppTimerCounter";
    private static List<TimerData> mTimerDatas = new ArrayList<>();

    public static void putData(TimerData data){
        mTimerDatas.add(data);
    }

    public static String printMethodCost(){
        return printMethodCost(true);
    }

    public static String printMethodCost(boolean isIncludeThreadMethod){
        try {
            //降序
            Collections.sort(mTimerDatas, new Comparator() {
                @Override
                public int compare(Object lhs, Object rhs) {
                    return ((TimerData) rhs).getCost().compareTo(((TimerData) lhs).getCost());
                }
            });
            //过滤条件
            List<TimerData> list = new ArrayList<>();
            for(TimerData timerData:mTimerDatas){
                if(timerData.isThread()){
                    if(isIncludeThreadMethod){
                        list.add(timerData);
                    }
                }else{
                    list.add(timerData);
                }
            }
            //打印
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Usopp耗时统计排行：").append("\n");
            for(TimerData timerData:list){
                stringBuilder.append("Cost:"+timerData.getCost()+"=>isThread:"+timerData.isThread()+"=>"+timerData.getMethod()).append("\n");
            }
            Log.d(TAG,stringBuilder.toString());
            return stringBuilder.toString();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return "";

    }
}
