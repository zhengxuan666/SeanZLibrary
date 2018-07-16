package com.seanz.library.utils;

import android.app.Activity;
import android.util.Log;

import java.util.Stack;

/**
 * Created by Catch on 2017/11/19.
 */

public class ScreenManager {
    private static Stack<Activity> activityStack;

    private static ScreenManager instance;

    private ScreenManager() {
        Log.v("mumayi", " ***** create ScreenManager ******");
    }

    public static ScreenManager getScreenManager() {
        if (instance == null) {
            instance = new ScreenManager();
            if (activityStack == null) {
                activityStack = new Stack<Activity>();
            }
        }
        return instance;
    }

    public void popActivity() {
        Activity activity = activityStack.lastElement();
        if (activity != null) {
            activity.finish();
            activity = null;
        }
    }

    public void popActivity(Activity activity) {
        if (activity != null) {
            activity.finish();
            activityStack.remove(activity);
            activity = null;
        }
    }

    // /** 清理部分的 activity **/
    // public void cancelActivity(){
    // if(CommonUtil.screenManager.getActivitySize()>3){
    // System.out.println(" =========== "+CommonUtil.screenManager.currentActivity().getClass()+" ====== ");
    // popActivity(CommonUtil.screenManager.currentActivity());
    // }
    // }

    public int getActivitySize() {

        return activityStack.size();
    }

    public Activity currentActivity() {
        Activity activity;
        try {
            activity = activityStack.lastElement();
        } catch (Exception e) {
            Log.e("ddv", "Has come to the end ");

            return null;
        }

        Log.e("ddv", "Remove stack item " + activity.getClass());
        return activity;
    }

    public void pushActivity(Activity activity) {

        activityStack.add(activity);
        Log.e("ddv", "push stack item " + activity.getClass());
    }

    public void popAllActivityExceptOne(Class cls) {
        while (true) {
            Activity activity = currentActivity();
            if (activity == null) {
                break;
            }
            if (activity.getClass().equals(cls)) {
                break;
            } else {
                popActivity(activity);
            }
        }
    }

    public void popAllActivityExceptOne() {
        while (true) {
            Activity activity = currentActivity();
            if (activity == null) {
                break;
            }
            popActivity(activity);
        }
    }
}
