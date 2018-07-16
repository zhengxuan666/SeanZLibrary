package com.seanz.library.widget.edittext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.View;

import com.seanz.library.BaseApplication;
import com.seanz.library.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Catch on 2018/1/3.
 */

public class TimeButton extends AppCompatButton implements View.OnClickListener {
    private long lenght = 60 * 1000;// 倒计时长度,这里给了默认60秒
    private String textafter = " S";
    private String textbefore = "获取验证码";
    private final String TIME = "time";
    private final String CTIME = "ctime";
    private View.OnClickListener mOnclickListener;
    private Timer t;
    private TimerTask tt;
    private long time;

    private int normal_background, timing_background;

    Map<String, Long> map = new HashMap<String, Long>();

    public TimeButton(Context context) {
        super(context);
        init(context, null);

    }

    public TimeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);

    }

    private void init(Context context, AttributeSet attrs) {
        setOnClickListener(this);
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TimeButton);
            normal_background = typedArray.getResourceId(R.styleable.TimeButton_normal_background, R.drawable.red_gradient_coner);
            timing_background = typedArray.getResourceId(R.styleable.TimeButton_timing_background, R.drawable.red_gradient_coner);
        } else {
            normal_background = R.drawable.red_gradient_coner;
            timing_background = R.drawable.red_gradient_coner;
        }
    }

    @SuppressLint("HandlerLeak")
    Handler han = new Handler() {
        public void handleMessage(android.os.Message msg) {
            TimeButton.this.setText(time / 1000 + textafter);
            time -= 1000;
            if (time < 0) {
                TimeButton.this.setEnabled(true);
                TimeButton.this.setText(textbefore);
                TimeButton.this.setBackgroundResource(normal_background);
                clearTimer();
            }
        }
    };

    private void initTimer() {
        time = lenght;
        t = new Timer();
        tt = new TimerTask() {

            @Override
            public void run() {
                //  Log.e("yung", time / 1000 + "");
                han.sendEmptyMessage(0x01);
            }
        };
    }

    private void clearTimer() {
        if (tt != null) {
            tt.cancel();
            tt = null;
        }
        if (t != null)
            t.cancel();
        t = null;
    }

    @Override
    public void setOnClickListener(View.OnClickListener l) {
        if (l instanceof TimeButton) {
            super.setOnClickListener(l);
        } else
            this.mOnclickListener = l;
    }

    private Boolean isflag = true;

    public void setIsflag(Boolean isflag) {
        this.isflag = isflag;
    }


    @Override
    public void onClick(View v) {
        if (mOnclickListener != null)
            mOnclickListener.onClick(v);
    }

    public void startTime() {
        initTimer();
        this.setText(time / 1000 + textafter);
        this.setEnabled(false);
        setBackgroundResource(timing_background);
        t.schedule(tt, 0, 1000);
    }

    /**
     * 和activity的onDestroy()方法同步
     */
    public void onDestroy() {
        if (BaseApplication.map == null)
            BaseApplication.map = new HashMap<String, Long>();
        BaseApplication.map.put(TIME, time);
        BaseApplication.map.put(CTIME, System.currentTimeMillis());
        clearTimer();
        // Log.e("yung", "onDestroy");
    }

    /**
     * 和activity的onCreate()方法同步
     */
    public void onCreate() {
        // Log.e("yung", BaseApplication.map + "");
        if (BaseApplication.map == null)
            return;
        if (BaseApplication.map.size() <= 0)// 这里表示没有上次未完成的计时
            return;
        long time = System.currentTimeMillis() - BaseApplication.map.get(CTIME)
                - BaseApplication.map.get(TIME);
        BaseApplication.map.clear();
        if (time > 0)
            return;
        else {
            initTimer();
            this.time = Math.abs(time);
            t.schedule(tt, 0, 1000);
            this.setText(time + textafter);
            this.setEnabled(false);
        }
    }

    /**
     * 设置计时时候显示的文本
     */
    public TimeButton setTextAfter(String text1) {
        this.textafter = text1;
        return this;
    }

    /**
     * 设置点击之前的文本
     */
    public TimeButton setTextBefore(String text0) {
        this.textbefore = text0;
        this.setText(textbefore);
        return this;
    }

    /**
     * 设置到计时长度
     *
     * @param lenght 时间 默认毫秒
     * @return
     */
    public TimeButton setLenght(long lenght) {
        this.lenght = lenght;
        return this;
    }
    /*

     *
     */

    public interface RequesListener {
        void requestResult();
    }
}
