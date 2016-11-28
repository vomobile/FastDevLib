package com.wangsheng.fastdevapp;


/**
 * Created by wangsheng on 16/11/17.
 *
 * @description 写了基本的一个头部类型 如果没有合适的  可根据需要自行扩展 在onCreate中调用
 * @See
 * @usage
 */

//public class SampleTitleHelper extends TitleBarHelper{
//    private Builder mBuilder;
//    private Context mContext;
//    private TopTitleBar mTopBar;
//    public SampleTitleHelper(Context context,TopTitleBar titleBar, TITLE_STYLE style, String title) {
//        super(titleBar);
//        this.mContext = context;
//        this.mTopBar = titleBar;
//        this.mBuilder = new Builder(context,titleBar);
//        setTitleBar(style,title);
//    }
//
//    enum TITLE_STYLE{
//        Normal,
//        HomeFragment,
//    }
//
//    private void setTitleBar(TITLE_STYLE style,String title){
//        if(TITLE_STYLE.Normal==style) {
//            final TopTitleBar.TextAction action = new TopTitleBar.TextAction("分享");
//            TopTitleBar.ImageAction imgAction = new TopTitleBar.ImageAction(R.drawable.ic_star_filled);
//
//            mTopBar = mBuilder
//                    .setImmersive(true)
//                    .setLeftVisible(true)
//                    .setLeftImageResource(R.drawable.btn_back_normal)
//                    .setLeftClickListener(defaultClickListener)
//                    .setBackgroundColr(R.color.titleBg)
//                    .setTitle(title)
//                    .setMainTitleVisible(true)
//                    .setMainTitleColor(R.color.toast_white)
//                    .setDividerVisible(false)
//                    .addAction(action)
//                    .addAction(imgAction)
//                    .build();
//
//            mTopBar.getViewByAction(action).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ToastUitl.showLong(action.getText());
//                }
//            });
//        }
//    }
//
//    View.OnClickListener defaultClickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            ((Activity) mContext).finish();
//        }
//    };
//
//    /**
//     * 请先明了 此效果在4.4以上起效
//     *
//     * 是否展示沉浸状态栏 请在onCreate()中调用
//     *
//     * @param isStatusBarCol true 表示状态栏以颜色填充 false 以图片填充
//     */
//    public void showCJ(boolean isStatusBarCol) {
//        SystemBarTintManager tintManager = new SystemBarTintManager((Activity) mContext);
//        tintManager.setStatusBarTintEnabled(true);
//        tintManager.setNavigationBarTintEnabled(true);
//        tintManager.setStatusBarTintResource(mContext.getResources().getColor(com.wangsheng.fastdevlibrary.R.color.title_bar_color));
//        Window window = ((Activity) mContext).getWindow();
//        if (DeviceUtil.getInstance(mContext).hasKitKat() && !DeviceUtil.getInstance(mContext).hasLollipop()) {
//            // 透明状态栏
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            // 透明导航栏
//            // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        } else if (DeviceUtil.getInstance(mContext).hasLollipop()) {
//            if (isStatusBarCol) {
//                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS |
//                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                        // | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//                window.setStatusBarColor(mContext.getResources().getColor(com.wangsheng.fastdevlibrary.R.color.title_bar_color));
//            } else {
//                //背景图片的话图片需要设置状态栏透明
//                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            }
//        }
//    }
//}
