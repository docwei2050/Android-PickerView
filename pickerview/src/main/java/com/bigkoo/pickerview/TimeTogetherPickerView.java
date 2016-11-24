package com.bigkoo.pickerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.view.BasePickerView;
import com.bigkoo.pickerview.view.WheelTimeOption;

import java.util.ArrayList;

/**
 * Created by tobo on 16/11/3.
 */

public class TimeTogetherPickerView<T>  extends BasePickerView implements View.OnClickListener {
    WheelTimeOption<T> WheelTimeOption;
    private View btnSubmit, btnCancel;
    private TextView tvTitle;
    private TimeTogetherPickerView.OnOptionsSelectListener optionsSelectListener;
    private static final String TAG_SUBMIT = "submit";
    private static final String TAG_CANCEL = "cancel";
    public TimeTogetherPickerView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.timetogetherpicker_view, contentContainer);
        // -----确定和取消按钮
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setTag(TAG_SUBMIT);
        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setTag(TAG_CANCEL);
        btnSubmit.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        //顶部标题
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        // ----转轮
        final View optionspicker = findViewById(R.id.optionspicker);
        WheelTimeOption = new WheelTimeOption(optionspicker);
    }
    public void setPicker(ArrayList<T> optionsItems) {
        WheelTimeOption.setPicker(optionsItems, null,false);
    }

    public void setPicker(ArrayList<T> options1Items,
                          ArrayList<ArrayList<T>> options2Items, boolean linkage) {
        WheelTimeOption.setPicker(options1Items, options2Items, linkage);
    }
    /**
     * 设置选中的item位置
     * @param option1 位置
     */
    public void setSelectOptions(int option1){
        WheelTimeOption.setCurrentItems(option1, 0);
    }
    /**
     * 设置选中的item位置
     * @param option1 位置
     * @param option2 位置
     */
    public void setSelectOptions(int option1, int option2){
        WheelTimeOption.setCurrentItems(option1, option2);
    }
    /**
     * 设置选项的单位
     * @param label1 单位
     */
    public void setLabels(String label1){
        WheelTimeOption.setLabels(label1, null, null);
    }
    /**
     * 设置选项的单位
     * @param label1 单位
     * @param label2 单位
     */
    public void setLabels(String label1,String label2){
        WheelTimeOption.setLabels(label1, label2, null);
    }
    /**
     * 设置是否循环滚动
     * @param cyclic 是否循环
     */
    public void setCyclic(boolean cyclic){
        WheelTimeOption.setCyclic(cyclic);
    }
    public void setCyclic(boolean cyclic1,boolean cyclic2) {
        WheelTimeOption.setCyclic(cyclic1,cyclic2);
    }


    @Override
    public void onClick(View v)
    {
        String tag=(String) v.getTag();
        if(tag.equals(TAG_CANCEL))
        {
            dismiss();
            return;
        }
        else
        {
            if(optionsSelectListener!=null)
            {
                int[] optionsCurrentItems=WheelTimeOption.getCurrentItems();
                optionsSelectListener.onOptionsSelect(optionsCurrentItems[0], optionsCurrentItems[1]);
            }
            dismiss();
            return;
        }
    }

    public interface OnOptionsSelectListener {
        void onOptionsSelect(int options1, int option2);
    }

    public void setOnoptionsSelectListener(
            TimeTogetherPickerView.OnOptionsSelectListener optionsSelectListener) {
        this.optionsSelectListener = optionsSelectListener;
    }

    public void setTitle(String title){
        tvTitle.setText(title);
    }
}
