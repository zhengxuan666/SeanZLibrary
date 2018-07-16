package com.seanz.library.widget.edittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

import com.seanz.library.R;

import java.util.ArrayList;

/**
 * Created by Catch on 2017/9/20.
 */

public class PhoneNumberEditText extends EditText {
    private static final String TAG = "RongDivisionEditText";

    private static final int PHONE_TYPE = 1;

    private static final int BANK_TYPE = 2;

    private static final int DEFAULT_BANK_CARD_LENGTH = 16;

    private static final int DEFAULT_PHONE_NUMBER_LENGTH = 11;

    private static final int DEFAULT_DIVIDE_LENGTH = 4;

    private static final String DEFAULT_DIVIDE_SYMBOL = "-";

    private String mSperator;
    // 存放需分隔处文本窗独
    private ArrayList<Integer> groupCoords = new ArrayList<Integer>();
    // 记录上次增减文本的长度，判断本次添加还是删除
    private int mLastLength = 0;
    // xml文件设置的输入长度
    private int mTotalLength;
    // 记录加上分隔符后总长度
    private int mLength;
    // 当前输入类型
    private int mType;

    public PhoneNumberEditText(Context context) {
        super(context);
    }

    public PhoneNumberEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setInputType(InputType.TYPE_CLASS_NUMBER);
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.RongDivisionEditText);
        if (typedArray != null) {
            mTotalLength = typedArray.getInteger(
                    R.styleable.RongDivisionEditText_rongTotalLength, DEFAULT_BANK_CARD_LENGTH);
            mSperator = typedArray.getString(R.styleable.RongDivisionEditText_sperator);
            if (TextUtils.isEmpty(mSperator)) {
                mSperator = DEFAULT_DIVIDE_SYMBOL;
            }
            mType = typedArray.getInt(R.styleable.RongDivisionEditText_type, BANK_TYPE);
            if (mType == BANK_TYPE) {
                // 计算需要几个分隔符及每个对应的长度
                int mode = mTotalLength % DEFAULT_BANK_CARD_LENGTH;
                int divider = mTotalLength / DEFAULT_DIVIDE_LENGTH;
                int groupCoordsLength = mode == 0 ? divider - 1 : divider;
                this.mLength = mTotalLength + groupCoordsLength;
                for (int i = 1; i < groupCoordsLength + 1; i++) {
                    groupCoords.add(i * (DEFAULT_DIVIDE_LENGTH + 1) - 1);
                }
                typedArray.recycle();
            } else {
                initPhoneGroupCoords();
                this.mLength = DEFAULT_PHONE_NUMBER_LENGTH + groupCoords.size();
            }

            setMaxWidth(mLength);
        }
        addTextChangedListener(new DivisionTextWatcher());
    }

    private void initPhoneGroupCoords() {
        //这里偷懒了，不计算了，手机号默认这么分了。。。。懒。。。
        groupCoords.add(3);
        groupCoords.add(8);
    }

    public PhoneNumberEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 获取文本框输入的内容，自动替换掉分隔符
     *
     * @return
     */
    public String getContent() {
        return getText().toString().trim().replace(mSperator, "");
    }

    private class DivisionTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() <= mLength) {
                if (mLastLength == 0) {
                    mLastLength = s.length();
                }
                if (groupCoords.contains(s.length() - 1)) {
                    if (s.length() < mLastLength) {
                        // delete
                        s = s.toString().substring(0, s.length() - 1);
                        setText(s.toString());
                    } else if (s.length() > mLastLength) {
                        // add
                        s = s.toString().substring(0, s.length() - 1) + mSperator + s.toString().substring(s.length() - 1);
                        setText(s.toString());
                    }
                }
            } else {
                s = s.toString().substring(0, s.length() - 1);
                setText(s.toString());
            }
            setSelection(s.length());
            mLastLength = s.length();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
