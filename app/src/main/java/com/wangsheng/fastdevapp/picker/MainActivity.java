package com.wangsheng.fastdevapp.picker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.wangsheng.fastdevlibrary.commonutils.KeyValue;
import com.wangsheng.fastdevlibrary.widget.pickerview.CharacterPickerWindow;
import com.wangsheng.fastdevlibrary.widget.pickerview.OnOptionChangedListener;
import com.wangsheng.fastdevlibrary.widget.pickerview.OnWheelItemChangeListener;

import java.util.ArrayList;
import java.util.List;

import static com.wangsheng.fastdevapp.picker.OptionsWindowHelper.setPickerData;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 用View的方式实现
//        showView();
        // 用PopupWindow的方式实现
        showWindow();

    }

    private void showView() {
        RelativeLayout layout = new RelativeLayout(MainActivity.this);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);

        CharacterPickerWindow characterPickerWindow = new CharacterPickerWindow(MainActivity.this);

        //初始化选项数据
        setPickerData(characterPickerWindow.getPickerView());

        //设置监听事件
        characterPickerWindow.getPickerView().setOnOptionChangedListener(new OnOptionChangedListener() {
            @Override
            public void onOptionChanged(int option1, int option2, int option3) {
                Log.e("test", option1 + "," + option2 + "," + option3);
            }
        });
        layout.addView(layout);

    }

    private void showWindow() {
        Button button = new Button(MainActivity.this);
        button.setText("点击弹窗");
        setContentView(button);

        List<KeyValue> list1 = new ArrayList<>();
        List<KeyValue> list2 = new ArrayList<>();
        List<KeyValue> list3 = new ArrayList<>();
        for(int i=0;i<10;i++){
            list1.add(new KeyValue("1","--"+i));
            list2.add(new KeyValue("1","--"+i));
            list3.add(new KeyValue("1","--"+i));
        }
        //选项选择器
        final CharacterPickerWindow window = new CharacterPickerWindow(MainActivity.this);
        //初始化选项数据
        //三级联动效果
        window.setPickerKV(list1, list2, list3);
        //设置默认选中的三级项目
        window.setSelectOptions(0, 0, 0);

        window.getPickerView().setOnWheelItemChangeListener(new OnWheelItemChangeListener() {
            @Override
            public void onItemSelectedOne(String str, int position) {
                List<KeyValue> list11 = new ArrayList<>();
                for(int i=0;i<10;i++){
                    list11.add(new KeyValue(String.valueOf(i),"选择二:"+i));
                }
                window.getPickerView().setPickerTwo(list11);
            }

            @Override
            public void onItemSelectedTwo(String str, int position) {
                List<KeyValue> list11 = new ArrayList<>();
                for(int i=0;i<10;i++){
                    list11.add(new KeyValue(String.valueOf(i),"选择三:"+i));
                }
                window.getPickerView().setPickerThree(list11);
            }

            @Override
            public void onItemSelectedThree(String str, int position) {

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 弹出
                window.showAtLocation(v, Gravity.BOTTOM, 0, 0);
            }
        });

    }

}
