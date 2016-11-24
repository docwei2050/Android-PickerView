package com.bigkoo.pickerview.view;


import android.view.View;

import com.bigkoo.pickerview.R;
import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.bigkoo.pickerview.lib.WheelView;
import com.bigkoo.pickerview.listener.OnItemSelectedListener;

import java.util.ArrayList;

/**
 * Created by  on 16/11/3.
 * 二级联动
 */

public class WheelTimeOption<T> {
        private View view;
        private WheelView wv_option1;
        private WheelView wv_option2;


        private ArrayList<T> mOptions1Items;
        private ArrayList<ArrayList<T>> mOptions2Items;
        private boolean linkage = false;
        private OnItemSelectedListener wheelListener_option1;

        public View getView() {
            return view;
        }

        public void setView(View view) {
            this.view = view;
        }

        public WheelTimeOption(View view) {
            super();
            this.view = view;
            setView(view);
        }



        public void setPicker(ArrayList<T> options1Items,
                              ArrayList<ArrayList<T>> options2Items, boolean linkage) {

            this.linkage = linkage;
            this.mOptions1Items = options1Items;
            this.mOptions2Items = options2Items;
            int len = ArrayWheelAdapter.DEFAULT_LENGTH;
            if (this.mOptions2Items == null)
                len = 12;
            // 选项1
            wv_option1 = (WheelView) view.findViewById(R.id.options1);
            wv_option1.setAdapter(new ArrayWheelAdapter(mOptions1Items, len));// 设置显示数据
            wv_option1.setCurrentItem(0);// 初始化时显示的数据
            // 选项2
            wv_option2 = (WheelView) view.findViewById(R.id.options2);
            if (mOptions2Items != null)
                System.out.println("WheelTimeOption.setPicker-->"+mOptions2Items.get(0));
                wv_option2.setAdapter(new ArrayWheelAdapter(mOptions2Items.get(0)));// 设置显示数据
            wv_option2.setCurrentItem(wv_option1.getCurrentItem());// 初始化时显示的数据

            int textSize = 25;
            wv_option1.setTextSize(textSize);
            wv_option2.setTextSize(textSize);


            if (this.mOptions2Items == null)
                wv_option2.setVisibility(View.GONE);

            // 联动监听器
            wheelListener_option1 = new OnItemSelectedListener() {

                @Override
                public void onItemSelected(int index) {
                    int opt2Select = 0;
                    if (mOptions2Items != null) {
                        opt2Select = wv_option2.getCurrentItem();//上一个opt2的选中位置
                        //新opt2的位置，判断如果旧位置没有超过数据范围，则沿用旧位置，否则选中最后一项
                        opt2Select = opt2Select >= mOptions2Items.get(index).size() - 1 ? mOptions2Items.get(index).size() - 1 : opt2Select;

                        wv_option2.setAdapter(new ArrayWheelAdapter(mOptions2Items
                                .get(index)));
                        wv_option2.setCurrentItem(opt2Select);
                    }
                }
            };


//		// 添加联动监听
            if (options2Items != null && linkage)
                wv_option1.setOnItemSelectedListener(wheelListener_option1);
        }
        /**
         * 设置选项的单位
         * @param label1 单位
         * @param label2 单位
         * @param label3 单位
         */
        public void setLabels(String label1, String label2, String label3) {
            if (label1 != null)
                wv_option1.setLabel(label1);
            if (label2 != null)
                wv_option2.setLabel(label2);
        }

        /**
         * 设置是否循环滚动
         * @param cyclic 是否循环
         */
        public void setCyclic(boolean cyclic) {
            wv_option1.setCyclic(cyclic);
            wv_option2.setCyclic(cyclic);
        }

        /**
         * 分别设置第一二三级是否循环滚动
         * @param cyclic1,cyclic2,cyclic3 是否循环
         */
        public void setCyclic(boolean cyclic1,boolean cyclic2) {
            wv_option1.setCyclic(cyclic1);
            wv_option2.setCyclic(cyclic2);

        }
        /**
         * 设置第二级是否循环滚动
         * @param cyclic 是否循环
         */
        public void setOption2Cyclic(boolean cyclic) {
            wv_option2.setCyclic(cyclic);
        }

        /**
         * 设置第三级是否循环滚动
         * @param cyclic 是否循环
         */


        /**
         * 返回当前选中的结果对应的位置数组 因为支持三级联动效果，分三个级别索引，0，1，2
         * @return 索引数组
         */
        public int[] getCurrentItems() {
            int[] currentItems = new int[2];
            currentItems[0] = wv_option1.getCurrentItem();
            currentItems[1] = wv_option2.getCurrentItem();
            return currentItems;
        }

        public void setCurrentItems(int option1, int option2) {
            if(linkage){
                itemSelected(option1, option2);
            }
            wv_option1.setCurrentItem(option1);
            wv_option2.setCurrentItem(option2);

        }

        private void itemSelected(int opt1Select, int opt2Select) {
            if (mOptions2Items != null) {
                wv_option2.setAdapter(new ArrayWheelAdapter(mOptions2Items
                        .get(opt1Select)));
                wv_option2.setCurrentItem(opt2Select);
            }

        }


}
