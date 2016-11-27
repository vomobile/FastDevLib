package com.wangsheng.fastdevlibrary.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import com.wangsheng.fastdevlibrary.R;

/**
 * Created by wangsheng on 16/11/10.
 *
 * @description 设置通过的条件然后进入回调  应用场景:用于一个页面多个EditText满足一定的输入条件然后进入回调，比如按钮的暗灰...
 *              可以自定义表单验证条件 建议后期做成构建者模式 未完成//TO-DO
 *              gitHub未找到相关的,所以自己实现了,应用场景还是有限，目的是减少代码量，梳理逻辑。若有问题或者你有更好的实现请联系我ChaserSheng@gmail.com 谢谢
 *
 * @注明 表单验证条件，内部现在提供如下几种，需要的话可自行扩展(项目需求较多的场景，可以这个类中添加，场景较少的使用构建者模式即可)：
 *      大于:>(不为空>0)、小于:<、等于:==、符合手机号:=ph、符合身份证=id、符合邮箱:=em、符合银行卡=bc
 */

public class FormEditText extends EditText {

    //表单验证的相关条件集合
    private String[] formStr;
    //表单验证不通过时显示错误信息
    private String errorMsg;
    //验证不通过是否获取焦点
    private boolean errorFocus;


    private FormListener mFormListener;
    private FormEditText mFormEditText;

    public interface FormListener{
        void OnFormSuccess(View v);
    }

    public void addFormListener(FormListener listener){
        this.mFormListener = listener;
    }

    public FormEditText(Context context) {
        super(context);
    }

    public FormEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FormEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        addTextChangedListener(new FormTextWatcher());
        mFormEditText = this;

        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.FormEditText);
        String verifyTexts = a.getString(R.styleable.FormEditText_verify_texts);
        formStr = verifyTexts.split("\\|");
        errorFocus = a.getBoolean(R.styleable.FormEditText_error_focus,false);
        errorMsg = a.getString(R.styleable.FormEditText_error_msg);
        a.recycle();
    }

    class FormTextWatcher implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mFormListener.OnFormSuccess(mFormEditText);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

}
