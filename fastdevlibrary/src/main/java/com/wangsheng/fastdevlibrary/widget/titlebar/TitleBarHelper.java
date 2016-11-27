package com.wangsheng.fastdevlibrary.widget.titlebar;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;

public class TitleBarHelper {

    private TopTitleBar mTopTitleBar;

    public TitleBarHelper() {

    }

    public TitleBarHelper(Builder builder){
        this.mTopTitleBar = builder.mTopTitleBar;
    }

    public static class Builder {

        private TopTitleBar mTopTitleBar;

        public Builder(Context context,TopTitleBar topTitleBar) {
            this.mTopTitleBar = topTitleBar;
            mTopTitleBar.setContext(context);
        }

        public Builder setLeftVisible(boolean flag){
            mTopTitleBar.setLeftVisible(flag);
            return this;
        }

        public Builder setLeftText(String str){
            mTopTitleBar.setLeftText(str);
            return this;
        }

        public Builder setLeftText(int resId){
            mTopTitleBar.setLeft(resId);
            return this;
        }

        public Builder setLeftTextColor(int resId){
            mTopTitleBar.setLeftTextColor(resId);
            return this;
        }

        public Builder setLeftTextColor(boolean flag){
            mTopTitleBar.setLeftVisible(flag);
            return this;
        }

        public Builder setLeftTextSize(float size){
            mTopTitleBar.setLeftTextSize(size);
            return this;
        }

        public Builder setLeftTextPadding(int left,int top,int right,int bottom){
            mTopTitleBar.setLeftTextPadding(left,top,right,bottom);
            return this;
        }

        public Builder setLeftImageResource(int resId){
            mTopTitleBar.setLeftImageResource(resId);
            return this;
        }

        public Builder setLeftClickListener(View.OnClickListener listener){
            mTopTitleBar.setLeftClickListener(listener);
            return this;
        }

        public Builder setCenterViewVisible(int flag){
            mTopTitleBar.setCenterViewVisible(flag);
            return this;
        }

        public Builder setCenterClickListener(View.OnClickListener listener){
            mTopTitleBar.setCenterClickListener(listener);
            return this;
        }

        public Builder setCustomTitleView(View view){
            mTopTitleBar.setCustomTitleView(view);
            return this;
        }

        public Builder setContentLayout(View view){
            mTopTitleBar.setContentLayout(view);
            return this;
        }

        public Builder setDivider(int resId){
            mTopTitleBar.setDivider(resId);
            return this;
        }

        public Builder setDividerColor(int color){
            mTopTitleBar.setDividerColor(color);
            return this;
        }

        public Builder setDividerHeight(int height){
            mTopTitleBar.setDividerHeight(height);
            return this;
        }

        public Builder setDividerVisible(boolean visible){
            mTopTitleBar.setDividerVisible(visible);
            return this;
        }

        public Builder setHeight(int height){
            mTopTitleBar.setHeight(height);
            return this;
        }

        public Builder setIsCenterAlways(boolean flag){
            mTopTitleBar.setIsCenterAlways(flag);
            return this;
        }

        public Builder setMainTitleBackground(int resId){
            mTopTitleBar.setMainTitleBackground(resId);
            return this;
        }

        public Builder setMainTitleColor(int color){
            mTopTitleBar.setMainTitleColor(color);
            return this;
        }

        public Builder setMainTitleSize(float size){
            mTopTitleBar.setMainTitleSize(size);
            return this;
        }

        public Builder setMainTitleVisible(boolean visible){
            mTopTitleBar.setMainTitleVisible(visible);
            return this;
        }

        public Builder setRightImage(Drawable drawable){
            mTopTitleBar.setRightImage(drawable);
            return this;
        }

        public Builder setRightImage(int index,Drawable drawable){
            mTopTitleBar.setRightImage(index,drawable);
            return this;
        }

        public Builder setSubTitleBackground(int resId){
            mTopTitleBar.setSubTitleBackground(resId);
            return this;
        }

        public Builder setSubTitleColor(int color){
            mTopTitleBar.setSubTitleColor(color);
            return this;
        }

        public Builder setSubTitleSize(float size){
            mTopTitleBar.setSubTitleSize(size);
            return this;
        }

        public Builder setSubTitleVisible(boolean visible){
            mTopTitleBar.setSubTitleVisible(visible);
            return this;
        }

        public Builder setTitle(CharSequence charSequence){
            mTopTitleBar.setTitle(charSequence);
            return this;
        }

        public Builder setTitle(int resId){
            mTopTitleBar.setTitle(resId);
            return this;
        }

        public Builder setTitle(CharSequence title,boolean need){
            mTopTitleBar.setTitle(title,need);
            return this;
        }

        public Builder addAction(TopTitleBar.Action action){
            mTopTitleBar.addAction(action);
            return this;
        }

        public Builder addAction(TopTitleBar.Action action, int index){
            mTopTitleBar.addAction(action,index);
            return this;
        }

        public Builder addActions(TopTitleBar.ActionList action){
            mTopTitleBar.addActions(action);
            return this;
        }

        public Builder setBackgroundImageResource(int resId){
            mTopTitleBar.setBackgroundImageResource(resId);
            return this;
        }

        public Builder setBackgroundColr(int resId){
            mTopTitleBar.setBackgroundColr(resId);
            return this;
        }

        //如果要设置图片  要么在xml中设置背景  isColor==false
        public Builder setImmersive(boolean vis,boolean isColor,int color){
            mTopTitleBar.setImmersive(vis,isColor,color);
            return this;
        }

        public TitleBarHelper build(){
            return new TitleBarHelper(this);
        }
    }
}
