package com.meiyou.usopp.data;

/**
 * Author: lwh
 * Date: 17/8/18 08:43.
 */

public class UsoppData {
    private boolean isApplication;
    private boolean isFrameworkApplication;
    private boolean isModuleApplication;
    private String launcher;
    private String activity;
    private boolean isThread;
    private String className;
    private String methodName;
    private boolean isAppBackground;
    private boolean isAppForground;
    private String before;
    private String after;
    private int priority;

    public boolean isAppBackground() {
        return isAppBackground;
    }

    public void setAppBackground(boolean appBackground) {
        isAppBackground = appBackground;
    }

    public boolean isAppForground() {
        return isAppForground;
    }

    public void setAppForground(boolean appForground) {
        isAppForground = appForground;
    }

    public boolean isApplication() {
        return isApplication;
    }

    public void setApplication(boolean application) {
        isApplication = application;
    }

    public String getLauncher() {
        return launcher;
    }

    public void setLauncher(String launcher) {
        this.launcher = launcher;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activityfullname) {
        this.activity = activityfullname;
    }

    public boolean isThread() {
        return isThread;
    }

    public void setThread(boolean thread) {
        isThread = thread;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public boolean isFrameworkApplication() {
        return isFrameworkApplication;
    }

    public void setFrameworkApplication(boolean frameworkApplication) {
        isFrameworkApplication = frameworkApplication;
    }

    public boolean isModuleApplication() {
        return isModuleApplication;
    }

    public void setModuleApplication(boolean moduleApplication) {
        isModuleApplication = moduleApplication;
    }

    public String getBefore() {
        return before;
    }

    public void setBefore(String before) {
        this.before = before;
    }

    public String getAfter() {
        return after;
    }

    public void setAfter(String after) {
        this.after = after;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
