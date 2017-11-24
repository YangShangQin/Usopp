package com.meiyou.usopp;

import android.text.TextUtils;
import android.util.Log;

import com.meiyou.usopp.annotations.Activity;
import com.meiyou.usopp.annotations.After;
import com.meiyou.usopp.annotations.AppApplication;
import com.meiyou.usopp.annotations.AppBackground;
import com.meiyou.usopp.annotations.AppForground;
import com.meiyou.usopp.annotations.Before;
import com.meiyou.usopp.annotations.FrameworkApplication;
import com.meiyou.usopp.annotations.ModuleApplication;
import com.meiyou.usopp.annotations.Thread;
import com.meiyou.usopp.data.TimerData;
import com.meiyou.usopp.data.UsoppData;
import com.meiyou.usopp.data.UsoppGenerateClass;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Author: meetyou
 * Date: 17/8/16 14:44.
 * <p>
 * UsoppCompiler 启动流程：
 * <p>
 * Appcliation
 * <p>
 * onCreate
 * <p>
 * on
 */

public class UsoppCompiler {
    private static final String TAG = "UsoppCompiler";
    private static UsoppCompiler instance;

    public static UsoppCompiler getInstance() {
        if (instance == null) {
            synchronized (UsoppCompiler.class) {
                if (instance == null)
                    instance = new UsoppCompiler();
            }
        }
        return instance;
    }

    private List<UsoppData> listUsoppData = new ArrayList<>();
    private List<UsoppData> listApplication = new ArrayList<>();
    private List<UsoppData> listModuleApplication = new ArrayList<>();
    private List<UsoppData> listFrameworkApplication = new ArrayList<>();
    //private List<UsoppData> listLauncher = new ArrayList<>();
    private List<UsoppData> listActivity = new ArrayList<>();
    private List<UsoppData> listBefore = new ArrayList<>();
    private List<UsoppData> listBack = new ArrayList<>();

    //按照注解类型找出对应的类和方法；
    //
    public void init(List<String> listModulePackagerName) {

        //调用方法
        long time = System.currentTimeMillis();
        try {
            clearData();
            //get All data
            Map<String, String> map = new HashMap<>();
            //先找默认
            try {
                Class classTemp = Class.forName(UsoppGenerateClass.fullName);
                Method getDataMethod = classTemp.getDeclaredMethod(UsoppGenerateClass.methodName);
                Map<String, String> tempMap = (Map<String, String>) getDataMethod.invoke(classTemp);
                if(tempMap!=null){
                    map.putAll(tempMap);
                }
            }catch (Exception ex){
                System.out.println("UsoppCompiler no default imp");
                //ex.printStackTrace();
            }
            if(listModulePackagerName!=null && listModulePackagerName.size()>0){
                for(String modulePackageName:listModulePackagerName){
                    try {
                        Class classTemp = Class.forName(UsoppGenerateClass.fullName+modulePackageName);
                        Method getDataMethod = classTemp.getDeclaredMethod(UsoppGenerateClass.methodName);
                        Map<String, String> tempMap = (Map<String, String>) getDataMethod.invoke(classTemp);
                        if(tempMap!=null){
                            map.putAll(tempMap);
                        }
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            }

            Iterator iter = map.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String clazzName = (String) entry.getValue();
                Class classUsopp = null;
                classUsopp = Class.forName(clazzName);
                Method[] methods = classUsopp.getDeclaredMethods();
                if (methods != null && methods.length > 0) {
                    for (Method method : methods) {
                        //set classname
                        UsoppData usoppData = new UsoppData();
                        usoppData.setClassName(clazzName);
                        //set method name
                        usoppData.setMethodName(method.getName());
                        Annotation[] annotations = method.getAnnotations();
                        if (annotations != null && annotations.length>0) {
                            for (Annotation annation : annotations) {
                                //Log.d(TAG, "==>annation:" + annation.toString() + "==>:" + annation.annotationType());
                                if (annation instanceof AppApplication) {
                                    usoppData.setApplication(true);
                                }
                                if (annation instanceof ModuleApplication) {
                                    usoppData.setModuleApplication(true);
                                }
                                if (annation instanceof FrameworkApplication) {
                                    usoppData.setFrameworkApplication(true);
                                } else if (annation instanceof Activity) {
                                    usoppData.setActivity(((Activity) annation).value());
                                }  else if (annation instanceof AppForground) {
                                    usoppData.setAppForground(true);
                                } else if (annation instanceof AppBackground) {
                                    usoppData.setAppBackground(true);
                                } else if (annation instanceof Before) {
                                    usoppData.setBefore(((Before) annation).value());
                                } else if (annation instanceof After) {
                                    usoppData.setAfter(((After) annation).value());
                                }
                               if (annation instanceof Thread) {
                                    usoppData.setThread(((Thread) annation).value());
                                }
                            }
                            dispatchData(usoppData);
                        }

                    }

                }
            }

        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            //Log.e(TAG, "UsoppCompiler catch classnofind,maybe no body use UsoppCompiler");
        }
        long timeEnd = System.currentTimeMillis();
        Log.d(TAG, "UsoppCompiler init cost time:" + (timeEnd - time));

    }


    private void clearData() {
        listUsoppData.clear();
        listApplication.clear();
        listFrameworkApplication.clear();
        listModuleApplication.clear();
        //listLauncher.clear();
        listBefore.clear();
        listBack.clear();
        listActivity.clear();
    }

    private void dispatchData(UsoppData usoppData) {
        listUsoppData.add(usoppData);
        if (usoppData.isApplication()) {
            listApplication.add(usoppData);
        }
        if (usoppData.isModuleApplication()) {
            listModuleApplication.add(usoppData);
        }
        if (usoppData.isFrameworkApplication()) {
            listFrameworkApplication.add(usoppData);
        }
       /* if (!TextUtils.isEmpty(usoppData.getLauncher())) {
            listLauncher.add(usoppData);
        }*/
        if (!TextUtils.isEmpty(usoppData.getActivity())) {
            listActivity.add(usoppData);
        }
        if (usoppData.isAppForground()) {
            listBefore.add(usoppData);
        }
        if (usoppData.isAppBackground()) {
            listBack.add(usoppData);
        }
    }


    private void fire(List<UsoppData> listApplication) {

        List<UsoppData> listUI = new ArrayList<>();
        final List<UsoppData> listThread = new ArrayList<>();
        for (UsoppData data : listApplication) {
            if (data.isThread()) {
                listThread.add(data);
            } else {
                listUI.add(data);
            }
        }
        //ui
        for (UsoppData data : listUI) {
            Class clazz = getCacheClass(data.getClassName());
            try {
                long time = System.currentTimeMillis();
                Method method = clazz.getDeclaredMethod(data.getMethodName());
                method.setAccessible(true);
                method.invoke(getCacheObject(data.getClassName()));
                long timeEnd = System.currentTimeMillis();
                //Log.d(TAG, "cost time:" + (timeEnd - time) + "=>类" + data.getClassName() + "方法名:" + data.getMethodName());

                UsoppTimerCounter.putData(TimerData.newBuilder()
                        .withCost((timeEnd - time))
                        .withIsThread(false)
                        .withMethod(data.getClassName()+"#"+data.getMethodName()).build());
            } catch (Exception e) {
                //Log.e(TAG, e.getMessage());
                e.printStackTrace();
            }
        }
        //thread
        if (listThread.size() > 0) {
            new java.lang.Thread(new Runnable() {
                @Override
                public void run() {
                    for (UsoppData data : listThread) {
                        Class clazz = getCacheClass(data.getClassName());
                        try {
                            long time = System.currentTimeMillis();
                            Method method = clazz.getDeclaredMethod(data.getMethodName());
                            method.setAccessible(true);
                            method.invoke(getCacheObject(data.getClassName()));
                            long timeEnd = System.currentTimeMillis();
                            //Log.d(TAG, "thread cost time:" + (timeEnd - time) + "=>类" + data.getClassName() + "方法名:" + data.getMethodName());

                            UsoppTimerCounter.putData(TimerData.newBuilder()
                                    .withCost((timeEnd - time))
                                    .withIsThread(true)
                                    .withMethod(data.getClassName()+"#"+data.getMethodName()).build());

                        } catch (Exception e) {
                            //Log.e(TAG, e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }

    public void fireApplication() {
        fireFrameworkApplication();
        fireAppApplication();
        fireModuleApplication();
    }

    private void fireFrameworkApplication() {
        fire(listFrameworkApplication);
    }

    private void fireModuleApplication() {
        fire(listModuleApplication);
    }


    private void fireAppApplication() {
        fire(listApplication);
    }

    public void fireActivity(String activityFullName) {
        if (activityFullName == null)
            return;
        List<UsoppData> list = new ArrayList<>();
        for (UsoppData data : listActivity) {
            if (data.getActivity() != null && activityFullName.equals(data.getActivity())) {
                list.add(data);
            }
        }
        fire(list);
    }

    public void fireAppForground() {
        fire(listBefore);
    }

    public void fireAppBackground() {
        fire(listBack);
    }

    //cache
    private HashMap<String, Object> mHashMap = new HashMap<>();

    private Object getCacheObject(String className) {
        if (mHashMap.get(className) != null) {
            return mHashMap.get(className);
        }
        try {
            Class clazz = getCacheClass(className);
            Constructor constructor = clazz.getConstructor();
            if (constructor != null) {
                Object object = constructor.newInstance();
                mHashMap.put(className, object);
                return object;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private HashMap<String, Class> mHashMapClass = new HashMap<>();

    private Class getCacheClass(String className) {
        if (mHashMapClass.get(className) != null) {
            return mHashMapClass.get(className);
        }
        try {
            Class clazz = Class.forName(className);
            mHashMapClass.put(className, clazz);
            return clazz;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String printMethodCost(){
        return UsoppTimerCounter.printMethodCost();
    }

    public static String printMethodCost(boolean isIncludeThreadMethod){
        return UsoppTimerCounter.printMethodCost(isIncludeThreadMethod);
    }

}
