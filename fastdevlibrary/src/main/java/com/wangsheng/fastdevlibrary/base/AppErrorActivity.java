package com.wangsheng.fastdevlibrary.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wangsheng.fastdevlibrary.R;
import com.wangsheng.fastdevlibrary.widget.titlebar.TitleBarHelper;
import com.wangsheng.fastdevlibrary.widget.titlebar.TopTitleBar;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;

public class AppErrorActivity extends FDLBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crash_error_layout);

        new TitleBarHelper.Builder(mActivity, (TopTitleBar) findViewById(R.id.titleBar)).setImmersive(true, false, R.color.color_6b7072).setBackgroundColr(R.color.color_6b7072).setTitle("Crash错误").setMainTitleColor(R.color.white).build();

        Button restartButton = (Button) findViewById(R.id.customactivityoncrash_error_activity_restart_button);

        final Class<? extends Activity> restartActivityClass = CustomActivityOnCrash.getRestartActivityClassFromIntent(getIntent());
        final CustomActivityOnCrash.EventListener eventListener = CustomActivityOnCrash.getEventListenerFromIntent(getIntent());

        if (restartActivityClass != null) {
            restartButton.setText(R.string.customactivityoncrash_error_activity_restart_app);
            restartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AppErrorActivity.this, restartActivityClass);
                    CustomActivityOnCrash.restartApplicationWithIntent(AppErrorActivity.this, intent, eventListener);
                }
            });
        } else {
            restartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CustomActivityOnCrash.closeApplication(AppErrorActivity.this, eventListener);
                }
            });
        }

        Button moreInfoButton = (Button) findViewById(R.id.customactivityoncrash_error_activity_more_info_button);

        if (CustomActivityOnCrash.isShowErrorDetailsFromIntent(getIntent())) {

            moreInfoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog dialog = new AlertDialog.Builder(mActivity)
                            .setTitle(cat.ereza.customactivityoncrash.R.string.customactivityoncrash_error_activity_error_details_title)
                            .setMessage(CustomActivityOnCrash.getAllErrorDetailsFromIntent(mActivity, getIntent()))
                            .setPositiveButton(cat.ereza.customactivityoncrash.R.string.customactivityoncrash_error_activity_error_details_close, null)
                            .setNeutralButton(cat.ereza.customactivityoncrash.R.string.customactivityoncrash_error_activity_error_details_copy,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            copyErrorToClipboard();
                                            Toast.makeText(mActivity, cat.ereza.customactivityoncrash.R.string.customactivityoncrash_error_activity_error_details_copied, Toast.LENGTH_SHORT).show();
                                        }
                                    })
                            .show();
                    TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(cat.ereza.customactivityoncrash.R.dimen.customactivityoncrash_error_activity_error_details_text_size));


                }
            });
        } else {
            moreInfoButton.setVisibility(View.GONE);
        }

        int defaultErrorActivityDrawableId = CustomActivityOnCrash.getDefaultErrorActivityDrawableIdFromIntent(getIntent());
        ImageView errorImageView = ((ImageView) findViewById(R.id.customactivityoncrash_error_activity_image));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            errorImageView.setImageDrawable(getResources().getDrawable(defaultErrorActivityDrawableId, getTheme()));
        } else {
            //noinspection deprecation
            errorImageView.setImageDrawable(getResources().getDrawable(defaultErrorActivityDrawableId));
        }
    }

    private void copyErrorToClipboard() {
        String errorInformation =
                CustomActivityOnCrash.getAllErrorDetailsFromIntent(AppErrorActivity.this, getIntent());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText(getString(R.string.customactivityoncrash_error_activity_error_details_clipboard_label), errorInformation);
            clipboard.setPrimaryClip(clip);
        } else {
            //noinspection deprecation
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            clipboard.setText(errorInformation);
        }
    }
}