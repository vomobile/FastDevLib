package com.wangsheng.fastdevapp;

import android.os.Bundle;

import com.wangsheng.fastdevlibrary.base.FDLBaseActivity;
import com.wangsheng.fastdevlibrary.widget.titlebar.TitleBarHelper;
import com.wangsheng.fastdevlibrary.widget.titlebar.TopTitleBar;

import butterknife.ButterKnife;


public class SecondActivity extends FDLBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        ButterKnife.bind(this);
        setSwipeBackEnabled(true);
        TopTitleBar topTitleBar = (TopTitleBar) findViewById(R.id.titleBar);
        new TitleBarHelper.Builder(mActivity, topTitleBar).setImmersive(true,false,R.color.colorPrimary).setLeftVisible(false).setDividerVisible(false).setBackgroundColr(R.color.color_6b7072).build();
//        new SampleTitleHelper(mActivity,(TopTitleBar)findViewById(R.id.titleBar), SampleTitleHelper.TITLE_STYLE.Normal,"第二页");
    }
}
