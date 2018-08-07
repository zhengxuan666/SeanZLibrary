package com.seanz.library.base;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.seanz.library.R;
import com.seanz.library.manager.ActivityStackManager;
import com.seanz.library.manager.ScreenManager;
import com.seanz.library.utils.DialogUtils;
import com.seanz.library.view.view.progress.ProgressBarLayout;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportActivity;

/**
 * 功能：SupportActivity基类
 *
 * @author sam
 * @Version : 1.0
 * @date : 2018.5.8
 */
public abstract class BaseActivity extends SupportActivity implements OnClickListener {
    private static final String TAG = "BaseActivity";
    private RelativeLayout relTitleBar;// 顶部导航栏
    private TextView moduleTextView;
    private ImageView topLeftButton;
    private TextView topRightText;
    private ImageView topRightImg;
    private FrameLayout mFraLayoutContent;
    private FrameLayout mFraLayoutHeadView;
    private RelativeLayout mRelLayoutBase;
    private ProgressBarLayout mLoadingBar;
    private RelativeLayout errorLayout;
    private RelativeLayout emptyLayout;
    private TextView tvEmtyHit;
    private TextView mResetButton;
    private Dialog mProgressDialog;//不可取消框
    private Dialog mProgressDialogCancle;//可取消加载框
    private int titlebarResId = R.layout.top_titlebar_base;
    Unbinder unbinder;
    private ScreenManager screenManager;
    protected boolean isStatusBar = true;//是否沉浸状态栏
    protected boolean isFullScreen = false;//是否允许全屏
    protected boolean isScreenPortrait = true;//是否禁止旋转屏幕
    private TextView ivTitlebarRight_Tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStackManager.getActivityStackManager().pushActivity(this);
        setScreenManager();
        screenManager = ScreenManager.getInstance();
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
                    ViewGroup parent = findViewById(android.R.id.content);
                    parent.setPadding(0, BarUtils.getStatusBarHeight(), 0, 0);
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

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "--->onRestart()");
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

    protected abstract void setScreenManager();

    protected abstract int getActivityLayoutId();////布局中Fragment的ID

    //    protected abstract void initView();//初始化界面
    //    protected abstract void registerListener();//绑定事件
//    protected abstract void initData();// 初始化数据,请求网络数据等

    protected void onClickedTopLeftButtton(View view) {
        finish();
    }

    protected void onClickFailureResetButton(View view) {
    }

    protected void onClickTitlebarRightText(View view) {
    }

    protected void onClickTitlebarRight(View view) {
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.activity_base);
        mRelLayoutBase = (RelativeLayout) findViewById(R.id.relLayoutBase);
        mFraLayoutContent = (FrameLayout) findViewById(R.id.fraLayoutContent);
        mFraLayoutHeadView = (FrameLayout) findViewById(R.id.fraLayoutHeadView);
        LayoutInflater.from(this).inflate(titlebarResId, mFraLayoutHeadView, true);
        LayoutInflater.from(this).inflate(layoutResID, mFraLayoutContent, true);
        mLoadingBar = (ProgressBarLayout) findViewById(R.id.load_bar_layout);
        errorLayout = (RelativeLayout) findViewById(R.id.errorLayout);
        emptyLayout = (RelativeLayout) findViewById(R.id.emptyLayout);
        mResetButton = (TextView) findViewById(R.id.reset_button);
        mResetButton.setOnClickListener(this);
        ivTitlebarRight_Tv = findViewById(R.id.ivTitlebarRight_Tv);
        if (ivTitlebarRight_Tv != null) ivTitlebarRight_Tv.setOnClickListener(this);
        //如果有使用黄油刀，请在这边加入即可
        unbinder = ButterKnife.bind(this);
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
        super.setContentView(view, params);
        //如果有使用黄油刀，请在这边加入即可
        unbinder = ButterKnife.bind(this);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        //如果有使用黄油刀，请在这边加入即可
        unbinder = ButterKnife.bind(this);
    }

    /**
     * 功能：如果自定义headview,一定要在setContentView前调用,否则无效
     *
     * @param layoutResID
     * @author:
     */
    protected void setHeadView(int layoutResID) {
        this.titlebarResId = layoutResID;
    }

    //隐藏头部
    protected void hideHeadView() {
        mFraLayoutHeadView.setVisibility(View.GONE);
    }

    //显示头部
    protected void showHeadView() {
        mFraLayoutHeadView.setVisibility(View.VISIBLE);
    }

    //显示加载布局
    protected void showLoadingBar() {
        showLoadingBar(false);
    }

    //隐藏加载布局
    public void hideLoadingBar() {
        mLoadingBar.hide();
    }

    public void showLoadingBar(boolean transparent) {
        mLoadingBar.setBackgroundColor(transparent ? Color.TRANSPARENT : getResources().getColor(R.color.main_bg));
        mLoadingBar.show();
    }

    //加载布局是否显示
    public boolean isLoadingBarShow() {
        return mLoadingBar.getVisibility() == View.VISIBLE;
    }

    //显示无网络布局
    protected void showErrorLayout() {
        errorLayout.setVisibility(View.VISIBLE);
    }

    //隐藏无网络布局
    protected void hideErrorLayout() {
        errorLayout.setVisibility(View.GONE);
    }

    //显示空页面
    public void showEmptyLayout(String emptyHit) {
        emptyLayout.setVisibility(View.VISIBLE);
        if (tvEmtyHit == null) {
            tvEmtyHit = (TextView) findViewById(R.id.tvEmtyHit);
        }
        tvEmtyHit.setText(emptyHit);
    }

    //隐藏空页面
    public void hideEmptyLayout() {
        emptyLayout.setVisibility(View.GONE);
    }

    protected RelativeLayout getErrorLayout() {
        return errorLayout;
    }

    //设置头部颜色
    protected void setTitleBarBackgroundColor(int color) {
        if (relTitleBar == null) {
            relTitleBar = findViewById(R.id.clTitlebar);
        }
        relTitleBar.setBackgroundColor(getResources().getColor(color));
    }

    //设置头部文字颜色
    protected void setModuleTitleColor(int resourceId) {
        if (moduleTextView == null) {
            moduleTextView = (TextView) findViewById(R.id.tvTitlebarTitle);
        }
        moduleTextView.setTextColor(getResources().getColor(resourceId));
    }

    //设置头部局部
    protected void setModuleTitle(int resourceId) {
        if (moduleTextView == null) {
            moduleTextView = (TextView) findViewById(R.id.tvTitlebarTitle);
        }
        moduleTextView.setText(resourceId);
    }

    //设置头部文字
    protected void setModuleTitle(String text) {
        if (moduleTextView == null) {
            moduleTextView = (TextView) findViewById(R.id.tvTitlebarTitle);
        }
        moduleTextView.setVisibility(View.VISIBLE);
        moduleTextView.setText(text);
    }

    protected String getModuleTitle() {
        if (moduleTextView != null) return moduleTextView.getText().toString();
        else
            return "";
    }

    //设置头部图片
    protected void setModuleTitleImg(int resId) {
        if (moduleTextView == null) {
            moduleTextView = (TextView) findViewById(R.id.tvTitlebarTitle);
        }
        moduleTextView.setVisibility(View.VISIBLE);
        moduleTextView.setText("");
        moduleTextView.setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0);
    }

    //隐藏头部文字
    protected void hideModuleTitle() {
        if (moduleTextView == null) {
            moduleTextView = (TextView) findViewById(R.id.tvTitlebarTitle);
        }
        moduleTextView.setVisibility(View.GONE);
    }

    //隐藏左上
    protected void hideTopLeftButton() {
        if (topLeftButton == null) {
            topLeftButton = (ImageView) findViewById(R.id.ivTitlebarLeft);
        }
        topLeftButton.setVisibility(View.GONE);
    }

    //显示左上，默认为箭头
    protected void showTopLeftButton() {
        showTopLeftButton("", R.drawable.arrow_left);
    }

    //显示左上，可添加文字 +箭头
    protected void showTopLeftButton(String text) {
        showTopLeftButton(text, R.drawable.arrow_left);
    }

    //显示左上，无箭头
    protected void showTopLeftText(String text) {
        showTopLeftButton(text, 0);
    }

    protected void showTopLeftButton(int resId) {
        showTopLeftButton(null, resId);
    }

    protected void showTopLeftButton(String text, int resId) {
        if (topLeftButton == null) {
            topLeftButton = (ImageView) findViewById(R.id.ivTitlebarLeft);
            topLeftButton.setOnClickListener(this);
            if (resId > 0)
                topLeftButton.setImageResource(resId);
        }
        topLeftButton.setVisibility(View.VISIBLE);
    }

    protected RelativeLayout getlayoutBase() {
        return mRelLayoutBase;
    }

    protected View getHeadView() {
        return mFraLayoutHeadView;
    }

    protected ImageView getTopLeftButton() {
        if (topLeftButton == null) {
            topLeftButton = (ImageView) findViewById(R.id.ivTitlebarLeft);
        }
        return topLeftButton;
    }


    //显示右上图片，只有图片 不含文字
    protected void showTopRightImg(int img) {
        if (topRightImg == null) {
            topRightImg = (ImageView) findViewById(R.id.ivTitlebarRight);
            topRightImg.setOnClickListener(this);
        }
        topRightImg.setVisibility(View.VISIBLE);
        topRightImg.setImageResource(img);
    }

    protected void setTopRightText(String string) {
        if (topRightText == null) {
            topRightText = findViewById(R.id.ivTitlebarRight_Tv);
        }
        topRightText.setVisibility(View.VISIBLE);
        topRightText.setText(string);
    }

    protected void setTopRightTextColor(int res){
        if (topRightText == null) {
            topRightText = findViewById(R.id.ivTitlebarRight_Tv);
        }
        topRightText.setTextColor(getResources().getColor(res));
    }


    //隐藏右上图片，只有图片 不含文字
    protected void hideTopRightImg() {
        if (topRightImg == null) {
            topRightImg = (ImageView) findViewById(R.id.ivTitlebarRight);
        }
        topRightImg.setVisibility(View.INVISIBLE);
    }

    protected void showLoadingDialogCancle() {
        if (mProgressDialogCancle == null) {
            mProgressDialogCancle = DialogUtils.createProgressDiaolg(this, "加载中...", true);
        }
        if (!mProgressDialogCancle.isShowing()) {
            mProgressDialogCancle.show();
        }
    }

    protected void hideLoadingDialogCancle() {
        if (mProgressDialogCancle == null) {
            mProgressDialogCancle = DialogUtils.createProgressDiaolg(this, "加载中...", true);
        }
        if (mProgressDialogCancle.isShowing()) {
            mProgressDialogCancle.dismiss();
        }
    }

    protected void showLoadingDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = DialogUtils.createProgressDiaolg(this, "加载中...", false);
        }
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    protected void hideLoadingDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = DialogUtils.createProgressDiaolg(this, "加载中...", false);
        }
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public View getmFraLayoutContent() {
        return mFraLayoutContent;
    }

    @Override
    public final void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ivTitlebarLeft) {
            onClickedTopLeftButtton(v);
        } else if (i == R.id.top_right_text) {
            onClickTitlebarRightText(v);
        } else if (i == R.id.ivTitlebarRight) {
            onClickTitlebarRight(v);
        } else if (i == R.id.reset_button) {
            onClickFailureResetButton(v);
            //如果使用黄油刀，请注释掉这里
        }
        if (i == R.id.ivTitlebarRight_Tv) {
            onClickTitleRight(v);
        } else {

        }
    }

    protected void onClickTitleRight(View v) {

    }

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

    //跳转页面
    public void goActivity(Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(BaseActivity.this, cls);
        startActivity(intent);
    }
    //添加fragment
   /* protected void addFragment(BaseFragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(getFragmentContentId(), fragment, fragment.getClass().getSimpleName()).addToBackStack(fragment.getClass().getSimpleName()).commitAllowingStateLoss();
        }
    }*/

    //移除fragment
    /*protected void removeFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }*/

    /**
     * 退出应用
     * called while exit app.
     */
    public void exitApp() {
        ActivityStackManager.getActivityStackManager().popAllActivity();//remove all activity.
        System.exit(0);//system exit.
    }

    private OnBooleanListener onPermissionListener;

    //DDV获取权限
    public void onPermissionRequests(String permission, OnBooleanListener onBooleanListener) {
        onPermissionListener = onBooleanListener;
        Log.d("MainActivity", "0");
        if (ContextCompat.checkSelfPermission(this,
                permission)
                == PackageManager.PERMISSION_GRANTED) {
            onPermissionListener.onClick(true);
            Log.d("MainActivity", "2" + ContextCompat.checkSelfPermission(this,
                    permission));
        } else {
            // Should we show an explanation?
            Log.d("MainActivity", "1");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {
                //权限已有
                onPermissionListener.onClick(true);
            } else {
                //没有权限，申请一下
                ActivityCompat.requestPermissions(this,
                        new String[]{permission},
                        1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //权限通过
                if (onPermissionListener != null) {
                    onPermissionListener.onClick(true);
                }
            } else {
                //权限拒绝
                if (onPermissionListener != null) {
                    onPermissionListener.onClick(false);
                }
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public interface OnBooleanListener {
        void onClick(boolean bln);
    }
}
