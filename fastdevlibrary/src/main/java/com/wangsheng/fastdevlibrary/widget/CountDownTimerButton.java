package com.wangsheng.fastdevlibrary.widget;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * 短信验证码倒计时控件
 */
public class CountDownTimerButton extends Button {
    private CountDownTimerUtils mCountDownTimerUtils;
    private String mStartText="秒";
    private String mReStartText="重新获取";

    public CountDownTimerButton(Context context) {
        super(context);
        init();
    }

    public CountDownTimerButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CountDownTimerButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){

    }

    public void setStartText(String startText){
        this.mStartText = startText;
    }

    public void reStartText(String reStartText){
        this.mReStartText = reStartText;
    }

    public void startTimer(){
        if(mCountDownTimerUtils==null) {
            mCountDownTimerUtils = new CountDownTimerUtils(60 * 1000, 1000);
        }
        mCountDownTimerUtils.start();
    }

    public void cancleTimer(){
        mCountDownTimerUtils.cancel();
    }

    class CountDownTimerUtils extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receiver
         *                          {@link #onTick(long)} callbacks.
         */
        public CountDownTimerUtils(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            setClickable(false); //设置不可点击
            setText(millisUntilFinished / 1000 + mStartText);  //设置倒计时时间
            setEnabled(false);
        }

        @Override
        public void onFinish() {
            setText(mReStartText);
            setClickable(true);//重新获得点击
            setEnabled(true);
        }

    }

}
