package com.wangsheng.fastdevlibrary.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.lzy.okgo.OkGo;
import com.wangsheng.fastdevlibrary.R;
import com.wangsheng.fastdevlibrary.commonutils.DeviceUtil;
import com.wangsheng.fastdevlibrary.commonutils.KeyBordUtil;
import com.wangsheng.fastdevlibrary.commonutils.SystemBarTintManager;
import com.wangsheng.fastdevlibrary.widget.dialog.LoadDialog;

import java.util.List;

import me.yokeyword.swipebackfragment.SwipeBackActivity;
import me.yokeyword.swipebackfragment.SwipeBackLayout;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Activity父类
 * @基础功能包括
 * 1. activity的入栈、出栈管理 {@link ActivityManager##onCreate(Bundle)##finish()}
 * 2. 敏感权限申请 build.gradle中targetSdkVersion设置为23以上时敏感权限需要申请才能使用 {@link EasyPermissions}
 * 3. activity之间的的各种方式跳转{@link #skipActivity ...}
 * 4. 页面关闭会同时关闭键盘 {@link #finish()}
 * 5. 滑动返回的效果 {@link #setSwipeBackEnabled(boolean)} 需要设置activity的style
 * 6. 沉浸状态栏效果 {@link #showCJ(boolean)}  一般使用{@link com.wangsheng.fastdevlibrary.widget.titlebar.TitleBarHelper}即可
 */
public class FDLBaseActivity extends SwipeBackActivity implements EasyPermissions.PermissionCallbacks {
    protected Activity mActivity;
    //成功回调
    public ForResultCallBack forResultCallBack;
    private final static int ForCode = 101;
    public View view;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        ActivityManager.getAppManager().addActivity(this);
        //获取根布局
        view = getWindow().getDecorView().findViewById(android.R.id.content);
        //初始化相关
        initOption();
        LoadDialog.show(mActivity);
    }

    private void initOption() {
        // 设置滑动方向
        getSwipeBackLayout().setEdgeOrientation(SwipeBackLayout.EDGE_ALL); // EDGE_LEFT(默认),EDGE_ALL
    }

    /**
     * 请先明了 此效果在4.4以上起效
     *
     * 是否展示沉浸状态栏 请在onCreate()中调用
     *
     * @param isStatusBarCol true 表示状态栏以颜色填充 false 以图片填充
     */
    public void showCJ(boolean isStatusBarCol) {
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setStatusBarTintResource(getResources().getColor(R.color.title_bar_color));
        if (DeviceUtil.getInstance(mActivity).hasKitKat() && !DeviceUtil.getInstance(mActivity).hasLollipop()) {
            // 透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        } else if (DeviceUtil.getInstance(mActivity).hasLollipop()) {
            if (isStatusBarCol) {
                Window window = getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS |
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.title_bar_color));
            } else {
                //背景图片的话图片需要设置状态栏透明
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        }
    }

    /** 是否起用滑动返回*/
    public void setSwipeBackEnabled(boolean isSwipeBackEnabled) {
    }

    @Override public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override public void onPermissionsGranted(int requestCode, List<String> perms) { }
    @Override public void onPermissionsDenied(int requestCode, List<String> perms) { }

    public void skipActivity(Activity aty, Class<?> cls) {
        startActivity(aty, cls);
        aty.finish();
    }


    public void skipActivity(Activity aty, Intent it) {
        startActivity(aty, it);
        aty.finish();
    }


    public void skipActivity(Activity aty, Class<?> cls, Bundle extras) {
        startActivity(aty, cls, extras);
        aty.finish();
    }


    public void startActivity(Activity aty, Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(aty, cls);
        aty.startActivity(intent);
    }


    public void startActivity(Activity aty, Intent it) {
        aty.startActivity(it);
    }


    public void startActivity(Activity aty, Class<?> cls, Bundle extras) {
        Intent intent = new Intent();
        intent.putExtras(extras);
        intent.setClass(aty, cls);
        aty.startActivity(intent);
    }

    /**
     * 需要成功回调的启动
     * @param cls
     * @param bundle
     * @param forResultCallBack
     */
    public void startActivityForResult(Class<?> cls, Bundle bundle, ForResultCallBack forResultCallBack) {
        this.forResultCallBack = forResultCallBack;
        Intent intent = new Intent(mActivity, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, ForCode);
    }

    public interface ForResultCallBack {
        void forResult();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK == resultCode) {
            switch (requestCode) {
                case ForCode:
                    forResultCallBack.forResult();
                    break;
                default:
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override public void finish() {
        KeyBordUtil.hideSoftKeyboard(getWindow().getDecorView());
        super.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //终止请求
        OkGo.getInstance().cancelTag(mActivity);
        ActivityManager.getAppManager().finishActivity(this);
    }

}
