package com.wangsheng.fastdevapp;

import android.os.Bundle;

import com.wangsheng.fastdevlibrary.base.FDLBaseActivity;
import com.wangsheng.fastdevlibrary.widget.titlebar.TitleBarHelper;
import com.wangsheng.fastdevlibrary.widget.titlebar.TopTitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SecondActivity extends FDLBaseActivity {

    @BindView(R.id.titleBar)
    TopTitleBar mTitleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        ButterKnife.bind(this);
        setSwipeBackEnabled(true);
        new TitleBarHelper.Builder(mActivity, mTitleBar).setImmersive(true, false, R.color.color_6b7072).setLeftVisible(false).setDividerVisible(false).setBackgroundColr(R.color.color_6b7072).setContentLayout(view).build();
//        new SampleTitleHelper(mActivity,(TopTitleBar)findViewById(R.id.titleBar), SampleTitleHelper.TITLE_STYLE.Normal,"第二页");
    }
}
