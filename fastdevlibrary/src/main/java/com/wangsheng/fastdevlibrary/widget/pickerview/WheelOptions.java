package com.wangsheng.fastdevlibrary.widget.pickerview;

import android.view.View;

import com.wangsheng.fastdevlibrary.R;
import com.wangsheng.fastdevlibrary.commonutils.KeyValue;

import java.util.ArrayList;
import java.util.List;

/**
 * 滚筒选择器配置
 */
final class WheelOptions {
    private final CharacterPickerView view;
    private LoopView wv_option1;
    private LoopView wv_option2;
    private LoopView wv_option3;

    private List<String> mOptions1Items;
    private List<List<String>> mOptions2Items;
    private List<List<List<String>>> mOptions3Items;
    private List<KeyValue> mOptions1KV;
    private List<KeyValue> mOptions2KV;
    private List<KeyValue> mOptions3KV;
    private OnOptionChangedListener mOnOptionChangedListener;

    public OnWheelItemChangeListener mOnWheelItemChangeListener;//滚筒滑动监听 暴露给外部 多级联动时可能会调用

    public void setOnItemListener(OnWheelItemChangeListener listener){
        this.mOnWheelItemChangeListener = listener;
    }

    public View getView() {
        return view;
    }

    public WheelOptions(CharacterPickerView view) {
        super();
        this.view = view;
    }

    /**
     * 请注意如果设置的是一级级的更新数据 {@link #setPickerOne(List)} 此回调不会调用 该接口调用方式{@link #setPicker(List, List, List)}
      * @param listener
     */
    public void setOnOptionChangedListener(OnOptionChangedListener listener) {
        this.mOnOptionChangedListener = listener;
    }

    public void setPicker(ArrayList<String> optionsItems) {
        setPicker(optionsItems, null, null);
    }

    public void setPicker(List<String> options1Items,
                          List<List<String>> options2Items) {
        setPicker(options1Items, options2Items, null);
    }

    public void setPickerKV(List<KeyValue> options1List, List<KeyValue> options2List, List<KeyValue> options3List) {
        setPickerOne(options1List);
        setPickerTwo(options2List);
        setPickerThree(options3List);

        if (mOptions2KV.isEmpty())
            view.findViewById(R.id.j_layout2).setVisibility(View.GONE);
        if (mOptions3KV.isEmpty())
            view.findViewById(R.id.j_layout3).setVisibility(View.GONE);

        setCurrentItems(0, 0, 0);
    }

    public void setPickerOne(List<KeyValue> keyValues){
        this.mOptions1KV = keyValues == null ? new ArrayList<KeyValue>() : keyValues;
        // 选项1
        wv_option1 = (LoopView) view.findViewById(R.id.j_options1);
        wv_option1.setItems(getStringList(mOptions1KV));// 设置显示数据
        wv_option1.setCurrentItem(0);// 初始化时显示的数据
        //设置是否循环播放
        wv_option1.setNotLoop();

        //滚动监听
        wv_option1.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                if (index == -1) {
                    return;
                }

                if (null!=mOnWheelItemChangeListener) {
                    mOnWheelItemChangeListener.onItemSelectedOne(mOptions1KV.get(index).getValueStr(),index);
                }

            }
        });
    }

    public void setPickerTwo(List<KeyValue> keyValues){
        this.mOptions2KV = keyValues == null ? new ArrayList<KeyValue>() : keyValues;
        // 选项2
        wv_option2 = (LoopView) view.findViewById(R.id.j_options2);
        if (!mOptions2KV.isEmpty()) {
            wv_option2.setItems(getStringList(mOptions2KV));// 设置显示数据
            wv_option2.setCurrentItem(0);// 初始化时显示的数据
            //设置是否循环播放
            wv_option2.setNotLoop();
            //滚动监听
            wv_option2.setListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(int index) {
                    if (index == -1) {
                        return;
                    }

                    if (null!=mOnWheelItemChangeListener) {
                        mOnWheelItemChangeListener.onItemSelectedTwo(mOptions2KV.get(index).getValueStr(), index);
                    }

                }
            });
        }
    }

    public void setPickerThree(List<KeyValue> keyValues){
        this.mOptions3KV = keyValues == null ? new ArrayList<KeyValue>() : keyValues;
        // 选项3
        wv_option3 = (LoopView) view.findViewById(R.id.j_options3);
        if (!mOptions3KV.isEmpty()) {
            wv_option3.setItems(getStringList(mOptions3KV));// 设置显示数据
            wv_option3.setCurrentItem(0);// 初始化时显示的数据
            //设置是否循环播放
            wv_option3.setNotLoop();
            //滚动监听
            wv_option3.setListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(int index) {
                    if (null!=mOnWheelItemChangeListener) {
                        mOnWheelItemChangeListener.onItemSelectedThree(mOptions3KV.get(index).getValueStr(), index);
                    }
                }
            });
        }
    }

    public void setPicker(List<String> options1Items,
                          List<List<String>> options2Items,
                          List<List<List<String>>> options3Items) {
        this.mOptions1Items = options1Items == null ? new ArrayList<String>() : options1Items;
        this.mOptions2Items = options2Items == null ? new ArrayList<List<String>>() : options2Items;
        this.mOptions3Items = options3Items == null ? new ArrayList<List<List<String>>>() : options3Items;
        // 选项1
        wv_option1 = (LoopView) view.findViewById(R.id.j_options1);
        wv_option1.setItems(mOptions1Items);// 设置显示数据
        wv_option1.setCurrentItem(0);// 初始化时显示的数据
        //设置是否循环播放
        wv_option1.setNotLoop();

        //滚动监听
        wv_option1.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                if (index == -1) {
                    return;
                }
                if (mOptions2Items.isEmpty()) {
                    doItemChange();
                    return;
                }

                wv_option2.setItems(mOptions2Items.get(index));
                wv_option2.setCurrentItem(0);

                if (mOptions3Items.isEmpty()) {
                    doItemChange();
                    return;
                }

                wv_option3.setItems(mOptions3Items.get(index).get(0));
                wv_option3.setCurrentItem(0);
            }
        });

        // 选项2
        wv_option2 = (LoopView) view.findViewById(R.id.j_options2);
        if (!mOptions2Items.isEmpty()) {
            wv_option2.setItems(mOptions2Items.get(0));// 设置显示数据
            wv_option2.setCurrentItem(0);// 初始化时显示的数据
            //设置是否循环播放
            wv_option2.setNotLoop();
            //滚动监听
            wv_option2.setListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(int index) {
                    if (index == -1) {
                        return;
                    }
                    if (mOptions3Items.isEmpty()) {
                        doItemChange();
                        return;
                    }

                    if (wv_option1.getSelectedItem() < mOptions3Items.size()) {
                        List<List<String>> allItems3 = mOptions3Items.get(wv_option1.getSelectedItem());
                        if (index >= allItems3.size()) {
                            index = 0;
                        }
                        wv_option3.setItems(allItems3.get(index));
                        wv_option3.setCurrentItem(0);
                    }

                }
            });
        }

        // 选项3
        wv_option3 = (LoopView) view.findViewById(R.id.j_options3);
        if (!mOptions3Items.isEmpty()) {
            wv_option3.setItems(mOptions3Items.get(0).get(0));// 设置显示数据
            wv_option3.setCurrentItem(0);// 初始化时显示的数据
            //设置是否循环播放
            wv_option3.setNotLoop();
            //滚动监听
            wv_option3.setListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(int index) {
                    doItemChange();
                }
            });
        }

        if (mOptions2Items.isEmpty())
            view.findViewById(R.id.j_layout2).setVisibility(View.GONE);
        if (mOptions3Items.isEmpty())
            view.findViewById(R.id.j_layout3).setVisibility(View.GONE);

        setCurrentItems(0, 0, 0);
    }

    /**
     * 选中项改变
     */
    private void doItemChange() {
        if (mOnOptionChangedListener != null) {
            int option1 = wv_option1.getSelectedItem();
            int option2 = wv_option2.getSelectedItem();
            int option3 = wv_option3.getSelectedItem();
            mOnOptionChangedListener.onOptionChanged(option1, option2, option3);
        }
    }

    /**
     * 设置是否循环滚动
     *
     * @param cyclic
     */
    public void setCyclic(boolean cyclic) {
        wv_option1.setLoop(cyclic);
        wv_option2.setLoop(cyclic);
        wv_option3.setLoop(cyclic);
    }

    /**
     * 返回当前选中的结果对应的位置数组 因为支持三级联动效果，分三个级别索引，0，1，2
     *
     * @return
     */
    public int[] getCurrentItems() {
        int[] currentItems = new int[3];
        currentItems[0] = wv_option1.getSelectedItem();
        currentItems[1] = wv_option2.getSelectedItem();
        currentItems[2] = wv_option3.getSelectedItem();
        return currentItems;
    }

    public void setCurrentItems(int option1, int option2, int option3) {
        wv_option1.setCurrentItem(option1);
        wv_option2.setCurrentItem(option2);
        wv_option3.setCurrentItem(option3);
    }

    public List<String> getStringList(List<KeyValue> list){
        List<String> stringList = new ArrayList<>();
        for(int i=0;i<list.size();i++){
            stringList.add(list.get(i).getValueStr());
        }
        return stringList;
    }
}
