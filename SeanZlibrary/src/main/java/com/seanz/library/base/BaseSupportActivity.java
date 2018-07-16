package com.seanz.library.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.seanz.library.manager.ActivityStackManager;
import com.seanz.library.manager.ScreenManagerSupportActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportActivity;

/**
 * @author sam
 * @version 1.0
 * @date 2018/5/4
 * https://blog.csdn.net/xx244488877/article/details/65937778
 */
public abstract class BaseSupportActivity extends SupportActivity {
    private static final String TAG = "BaseSupportActivity";
    protected LinearLayout badnetworkLayout, loadingLayout,baseactivityLayout;
    protected LayoutInflater inflater;
    protected boolean isStatusBar = false;//是否沉浸状态栏
    protected boolean isFullScreen = false;//是否允许全屏
    protected boolean isScreenPortrait = true;//是否禁止旋转屏幕
    protected Context ctx;//Context
    private boolean isDebug;// 是否输出日志信息
    protected abstract int getActivityLayoutId();////布局中Fragment的ID
    protected abstract void initView();//初始化界面
    protected abstract void registerListener();//绑定事件
    protected abstract void initData();// 初始化数据,请求网络数据等
    //布局中Fragment的ID
    protected abstract int getFragmentContentId();
    protected abstract void setScreenManager();
    private ScreenManagerSupportActivity screenManager;
    Unbinder unbinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStackManager.getActivityStackManager().pushActivity(this);
        initScreenManage();
        setContentView(getActivityLayoutId());
        inflater = LayoutInflater.from(this);
//        initView();
        unbinder = ButterKnife.bind(this);
        registerListener();
        initData();
        ctx = this;
    }


    private void initScreenManage(){
        setScreenManager();
        screenManager = ScreenManagerSupportActivity.getInstance();
        screenManager.setStatusBar(isStatusBar, this);
        screenManager.setScreenRoate(isScreenPortrait, this);
        screenManager.setFullScreen(isFullScreen, this);
        if (isStatusBar) {
            if (getStatusBarColor() != 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    setTranslucentStatus(true);
                    SystemBarTintManager tintManager = new SystemBarTintManager(this);
                    tintManager.setStatusBarTintEnabled(true);
                    tintManager.setStatusBarTintResource(getStatusBarColor());//通知栏所需颜色
                }
            }
        }
    }
    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
    /**
     * 是否需要沉浸式状态栏 不需要时返回0 需要时返回颜色
     *
     * @return StatusBarTintModle(boolean getStatusBarColor, int color);
     */
    protected abstract @ColorRes
    int getStatusBarColor();
    /**
     * 跳转Activity
     * skip Another Activity
     *
     * @param activity
     * @param cls
     */
    public static void skipAnotherActivity(Activity activity, Class<? extends Activity> cls) {
        Intent intent = new Intent(activity, cls);
        activity.startActivity(intent);
        activity.finish();
    }

    /**
     * 退出应用
     * called while exit app.
     */
    public void exitLogic() {
        ActivityStackManager.getActivityStackManager().popAllActivity();//remove all activity.
        System.exit(0);//system exit.
    }


    //添加fragment
    protected void addFragment(BaseSupportFragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(getFragmentContentId(),
                    fragment, fragment.getClass().getSimpleName())
                    .addToBackStack(fragment.getClass().getSimpleName())
                    .commitAllowingStateLoss();
        }
    }

    //移除fragment
    protected void removeFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }


    //返回键返回事件
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 防止快速点击
     * @return
     */
    private boolean fastClick() {
        long lastClick = 0;
        if (System.currentTimeMillis() - lastClick <= 1000) {
            return false;
        }
        lastClick = System.currentTimeMillis();
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "--->onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "--->onResume()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "--->onRestart()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "--->onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "--->onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "--->onDestroy()");
        ActivityStackManager.getActivityStackManager().popActivity(this);
        unbinder.unbind();
    }
}
