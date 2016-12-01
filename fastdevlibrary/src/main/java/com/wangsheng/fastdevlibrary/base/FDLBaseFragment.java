package com.wangsheng.fastdevlibrary.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * Fragment父类
 * 1.处理了api23以上请求敏感权限问题
 * 2.fragment由于被系统回收重建时的状态恢复问题
 */
public abstract class FDLBaseFragment extends Fragment implements EasyPermissions.PermissionCallbacks {
    private static final String STATE_IS_HIDDEN = "isHidden";

    protected Activity mActivity;

    @Override public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();
    }

    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_IS_HIDDEN, isHidden());
    }

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(savedInstanceState != null){
            boolean isHidden = savedInstanceState.getBoolean(STATE_IS_HIDDEN);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            if(isHidden){
                transaction.hide(this);
            } else {
                transaction.show(this);
            }
            transaction.commit();
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override public void onPermissionsGranted(int requestCode, List<String> perms) { }
    @Override public void onPermissionsDenied(int requestCode, List<String> perms) { }
}
