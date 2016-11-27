package com.wangsheng.qrcode;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.wangsheng.qrcode.Util.PhotoParams;
import com.wangsheng.qrcode.Util.PhotoUtils;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.QRCodeDecoder;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

public class TestScanActivity extends AppCompatActivity implements QRCodeView.Delegate {
    private static final String TAG = TestScanActivity.class.getSimpleName();
    private static final int REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY = 666;

    private QRCodeView mQRCodeView;
    private PhotoUtils photoUtils;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_scan);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        mQRCodeView = (ZXingView) findViewById(R.id.zxingview);
        mQRCodeView.setDelegate(this);
        photoUtils = new PhotoUtils(TestScanActivity.this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mQRCodeView.startCamera();
//        mQRCodeView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
    }

    @Override
    protected void onStop() {
        mQRCodeView.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mQRCodeView.onDestroy();
        super.onDestroy();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        Log.i(TAG, "result:" + result);
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        vibrate();
        mQRCodeView.startSpot();
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Log.e(TAG, "打开相机出错");
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.start_spot) {
            mQRCodeView.startSpot();

            PhotoParams photoParams = new PhotoParams();
            photoParams.crop = String.valueOf(true);
            photoUtils.takePicture(TestScanActivity.this, photoParams);
            photoUtils.setOnPhotoResultListener(new PhotoUtils.OnPhotoResultListener() {
                @Override
                public void onPhotoResult(Uri uri) {
                    Toast.makeText(TestScanActivity.this,uri.toString(),Toast.LENGTH_LONG).show();
                }

                @Override
                public void onPhotoCancel() {

                }
            });
        } else if (i == R.id.stop_spot) {
            mQRCodeView.stopSpot();

        } else if (i == R.id.start_spot_showrect) {
            mQRCodeView.startSpotAndShowRect();

        } else if (i == R.id.stop_spot_hiddenrect) {
            mQRCodeView.stopSpotAndHiddenRect();

        } else if (i == R.id.show_rect) {
            mQRCodeView.showScanRect();

        } else if (i == R.id.hidden_rect) {
            mQRCodeView.hiddenScanRect();

        } else if (i == R.id.start_preview) {
            mQRCodeView.startCamera();

        } else if (i == R.id.stop_preview) {
            mQRCodeView.stopCamera();

        } else if (i == R.id.open_flashlight) {
            mQRCodeView.openFlashlight();

        } else if (i == R.id.close_flashlight) {
            mQRCodeView.closeFlashlight();

        } else if (i == R.id.scan_barcode) {
            mQRCodeView.changeToScanBarcodeStyle();

        } else if (i == R.id.scan_qrcode) {
            mQRCodeView.changeToScanQRCodeStyle();

        } else if (i == R.id.choose_qrcde_from_gallery) {
            /*
                从相册选取二维码图片，这里为了方便演示，使用的是
                https://github.com/bingoogolapple/BGAPhotoPicker-Android
                这个库来从图库中选择二维码图片，这个库不是必须的，你也可以通过自己的方式从图库中选择图片
                 */
//            startActivityForResult(BGAPhotoPickerActivity.newIntent(this, null, 1, null, false), REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY);
            PhotoParams photoParams = new PhotoParams();
            photoParams.crop = String.valueOf(true);
            photoUtils.selectPicture(TestScanActivity.this, photoParams);
            photoUtils.setOnPhotoResultListener(new PhotoUtils.OnPhotoResultListener() {
                @Override
                public void onPhotoResult(Uri uri) {
                    final String picturePath = uri.getPath();
                    /**
                     这里为了偷懒，就没有处理匿名 AsyncTask 内部类导致 Activity 泄漏的问题
                     请开发在使用时自行处理匿名内部类导致Activity内存泄漏的问题，处理方式可参考 https://github.com/GeniusVJR/LearningNotes/blob/master/Part1/Android/Android%E5%86%85%E5%AD%98%E6%B3%84%E6%BC%8F%E6%80%BB%E7%BB%93.md
                     */
                    new AsyncTask<Void, Void, String>() {
                        @Override
                        protected String doInBackground(Void... params) {
                            return QRCodeDecoder.syncDecodeQRCode(picturePath);
                        }

                        @Override
                        protected void onPostExecute(String result) {
                            if (TextUtils.isEmpty(result)) {
                                Toast.makeText(TestScanActivity.this, "未发现二维码", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(TestScanActivity.this, result, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }.execute();
                }

                @Override
                public void onPhotoCancel() {

                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        photoUtils.onActivityResult(requestCode, resultCode, data);
        mQRCodeView.showScanRect();
    }


}