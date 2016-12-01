package com.wangsheng.fastdevapp;

import android.content.Intent;
import android.graphics.BlurMaskFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.wangsheng.fastdevlibrary.base.FDLBaseActivity;
import com.wangsheng.fastdevlibrary.commonutils.DisplayUtil;
import com.wangsheng.fastdevlibrary.updateplugin.UpdateBuilder;
import com.wangsheng.fastdevlibrary.updateplugin.model.Update;
import com.wangsheng.fastdevlibrary.updateplugin.strategy.UpdateStrategy;
import com.wangsheng.fastdevlibrary.widget.CountDownTimerButton;
import com.wangsheng.fastdevlibrary.widget.spannabletextview.SpannableTextView;
import com.wangsheng.fastdevlibrary.widget.spannabletextview.utils.SwClickableSpan;

import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends FDLBaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
//        UpdateBuilder.create().check(mActivity);
        UpdateBuilder.create()
                .strategy(new UpdateStrategy() {
                    @Override
                    public boolean isShowUpdateDialog(Update update) {
                        // 有新更新直接展示
                        return true;
                    }

                    @Override
                    public boolean isAutoInstall() {
                        return false;
                    }

                    @Override
                    public boolean isShowDownloadDialog() {
                        // 展示下载进度
                        return false;
                    }
                })
                .check(MainActivity.this);

        OkGo.post("https://mbl.jnbank.cc/pweb/SessionInit.do")//
                .tag(this)//
                .execute(new StrCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {

                    }
                });
//        throw new RuntimeException("boom!!!");
    }

    private void initView(){
        setSwipeBackEnabled(true);

        findViewById(R.id.textView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SecondActivity.class));
            }
        });

        final CountDownTimerButton countDownTimerButton = (CountDownTimerButton) findViewById(R.id.btn);
        countDownTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimerButton.startTimer();
                mActivity.startActivity(new Intent(mActivity,SecondActivity.class));
            }
        });

        SpannableTextView stv1 = (SpannableTextView) findViewById(R.id.stv_1);

        stv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        // Span인스턴스를 따로 만들고 관리 하는 방법.
        final SpannableTextView.Span span = new SpannableTextView.Span("Hello World!!! ")
                .textColor(Color.GRAY)
                .bold()
                .textSizeSP(26)
                .build();
        stv1.addSpan(span);

        // 바로 Span인스턴스를 생성하고 항목을 설정 하는 방법
        stv1.addSpan(
                new SpannableTextView.Span("\n반가워요!! ")
                        .textColorRes(R.color.colorAccent)
                        .italic()
                        .textSizePX(DisplayUtil.dip2px(14))
                        .build()
        );
        stv1.addSpan(
                new SpannableTextView.Span(" :)  ")
                        .textColor(Color.rgb(100, 100, 100))
                        .build()
        );

        // 클릭 이벤트의 구현
        stv1.addSpan(
                new SpannableTextView.Span("(Click Link)")
                        .click(
                                new SwClickableSpan() {
                                    @Override
                                    public void onClick(View widget) {
                                        Toast.makeText(MainActivity.this, "Touched link one.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                        )
                        .linkTextColorRes(R.color.colorPrimary)
                        .build()
        );

        stv1.addSpan(
                new SpannableTextView.Span(" // ")
                        .build()
        );

        stv1.addSpan(
                new SpannableTextView.Span("(Touch this)")
                        .click(
                                new SwClickableSpan() {
                                    @Override
                                    public void onClick(View widget) {
                                        Toast.makeText(MainActivity.this, "Touched link two.", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void updateDrawState(TextPaint ds) {
                                        super.updateDrawState(ds);

                                        // hide link under line
                                        ds.setUnderlineText(false);
                                    }
                                }
                        )
                        .linkTextColorRes(R.color.colorPrimaryDark)
                        .build()
        );

        stv1.addSpan(
                new SpannableTextView.Span("\nscale X(2.0f)")
                        .textSizeSP(16)
                        .scaleX(2.0f)
                        .build()
        );

        //stv1.clearSpans();

        // 텍스트에 블러 마스크 추가
        SpannableTextView stv2 = (SpannableTextView) findViewById(R.id.stv_2);
        stv2.addSpan(
                new SpannableTextView.Span("Blurred Text (Normal)")
                        .textSizeSP(20)
                        .blurMaskFilter(5)
                        .build()
        );
        SpannableTextView stv22 = (SpannableTextView) findViewById(R.id.stv_2_2);
        stv22.addSpan(
                new SpannableTextView.Span("Blurred Text (Outer)")
                        .textSizeSP(30)
                        .blurMaskFilter(8, BlurMaskFilter.Blur.OUTER)
                        .build()
        );

        SpannableTextView stv3 = (SpannableTextView) findViewById(R.id.stv_3);
        stv3.addSpan(
                new SpannableTextView.Span(R.string.rtext1)
                        .textSizeSP(16)
                        .textColor(Color.BLACK)
                        .bold()
                        .build()
        );

        SpannableTextView stv4 = (SpannableTextView) findViewById(R.id.stv_4);
        stv4.addSpan(
                new SpannableTextView.Span(R.string.rtext2)
                        .textSizeSP(12)
                        .textColor(Color.GRAY)
                        .findSharpTags(
                                Color.RED
                        )
                        .findAtTags(
                                // normal text color, pressed text color
                                new SwClickableSpan(Color.rgb(39, 174, 96), Color.rgb(211, 84, 0)) {
                                    @Override
                                    public void onClick(View widget) {
                                        Toast.makeText(MainActivity.this, "@ Tag clicked..", Toast.LENGTH_SHORT).show();
                                    }
                                }
                        )
                        .findURLstrings(
                                // hide underline
                                new SwClickableSpan(false) {
                                    @Override
                                    public void onClick(View widget) {
                                        Toast.makeText(MainActivity.this, "URL string clicked..", Toast.LENGTH_SHORT).show();
                                    }
                                }
                        )
                        .build()
        );

    }

}
