package com.wangsheng.fastdevapp;

import android.os.Bundle;

import com.wangsheng.fastdevlibrary.base.FDLBaseActivity;
import com.wangsheng.fastdevlibrary.widget.titlebar.TopTitleBar;


public class SecondActivity extends FDLBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        setSwipeBackEnabled(true);

        new SampleTitleHelper(mActivity,(TopTitleBar)findViewById(R.id.titleBar), SampleTitleHelper.TITLE_STYLE.Normal,"第二页");
    }
}
