package com.bigkoo.pickerviewdemo;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.listener.SelectListener;
import com.bigkoo.pickerview.view.WheelTime;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static java.lang.Integer.parseInt;


public class MainActivity extends Activity {
    private TextView tvOptions;
    OptionsPickerView pvOptions;
    View vMasker;
    String selectedDate="2016-11-03";
    private EditText mEditText;
    private TextView mDate_tv;
   //这个日期选择控件被修改过，加入了一个接口回调
    //以预约时间决定日期，预约截止时间过了，日期选择从第二天开始，否则从当前时间开始
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vMasker = findViewById(R.id.vMasker);
        mEditText = (EditText) findViewById(R.id.et);
        tvOptions = (TextView) findViewById(R.id.twoOptions);
        mDate_tv = (TextView) findViewById(R.id.date);

        //选项选择器
        pvOptions = new OptionsPickerView(this);

        tvOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPickerDialog(mEditText.getEditableText().toString(), "选择时间","");
            }
        });
    }
    //小时分钟联动选择器
    private void showPickerDialog(String timeRange, String title,final String callback) {
        final ArrayList<String> options1Items = new ArrayList<>();
        final ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
        //选项选择器
        OptionsPickerView pvOptions = new OptionsPickerView(this);
        //正则不匹配，直接返回默认值
        if(TextUtils.isEmpty(timeRange)||!timeRange.matches("\\d{2}:\\d{2}-\\d{2}:\\d{2}")){
            timeRange="08:00-16:00";
        }

        String[] var=timeRange.split("-");
        String[] firstAppoint=var[0].split(":");
        String[] secondAppoint=var[1].split(":");

        int hourMin= parseInt(firstAppoint[0]);
        int hourMax= parseInt(secondAppoint[0]);

        int firstMin= parseInt(firstAppoint[1]);
        int secondMin= parseInt(secondAppoint[1]);

        if(!TextUtils.isEmpty(selectedDate)){
            Date date=new Date();
            String dateStr=new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
            String[] times=dateStr.split("\\s");
            if(selectedDate.equals(times[0])){
                //如果日期为当天时间，那么默认时间应该从此刻开始
                String[] hour_minStr=times[1].split(":");
                int nowHour=Integer.parseInt(hour_minStr[0]);
                int nowMin=Integer.parseInt(hour_minStr[1]);
                if(nowHour>hourMax||(nowHour==hourMax&&nowMin>=secondMin)){
                    Toast.makeText(MainActivity.this,"今天已经没有可预约时间了，请重新选择日期",Toast.LENGTH_SHORT).show();
                    return;
                }else{

                    hourMin=nowHour;
                    firstMin=nowMin;
                    System.out.println("hourMin"+hourMin);
                    System.out.println("firstMin"+firstMin);

                }
            }
        }

        //传入的小时数据不对，给提示
        if(hourMin>hourMax){
            showTips(MainActivity.this,"预约时间错误，请稍后再试");
        }
        //传入的分钟数据超过或者等于60分
        if(firstMin>=60) {
            firstMin=0;
            hourMin=hourMin+1;
        }
        if(secondMin>=60) {
            secondMin = 0;
            hourMax=hourMax+1;
        }
        if(firstMin>50&&firstMin<60&&hourMin!=hourMax){
            hourMin=hourMin+1;
            firstMin=0;
        }
        if(firstMin%10!=0) firstMin =(firstMin/10+1)*10;
        if(secondMin%10!=0) secondMin=(secondMin/10)*10;

        for(int i=hourMin;i<=hourMax;i++) {
            options1Items.add(String.valueOf(i));
            ArrayList<String> arr=new ArrayList<String>();
            if(i==hourMin) {
                for (int j = firstMin; j < 60; j += 10) {
                    if (j == 0) {
                        arr.add("00");
                    } else {
                        arr.add(String.valueOf(j));
                    }
                }
            }else if(i==hourMax){
                for(int j=0;j<=secondMin;j+=10){
                    if(j==0){
                        arr.add("00");
                    }else{
                        arr.add(String.valueOf(j));
                    }
                }
            }else{
                for (int k = 0; k < 60; k += 10) {
                    if (k == 0) {
                        arr.add("00");
                    } else {
                        arr.add(k + "");
                    }
                }
            }
            options2Items.add(arr);
        }
        // 二级联动效果
        pvOptions.setPicker(options1Items, options2Items, null, true);
        pvOptions.setTitle(title);
        pvOptions.setCyclic(true, true, false);
        //监听确定选择按钮
        pvOptions.setSelectOptions(0, 0);
        pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1)+":"
                        + options2Items.get(options1).get(option2);
                tvOptions.setText("您选择的时间为-->"+tx);
                vMasker.setVisibility(View.GONE);
            }
        });
        pvOptions.setCancelable(false);
        pvOptions.show();
        //取消按钮时间不重写会挂掉
        pvOptions.setSelectListener(new SelectListener() {
            @Override
            public void onCancel() {
            }
        });
    }
  public void dateclick(View view){
      Date date = new Date(2016 - WheelTime.DEFULT_START_YEAR, 10, 4);
      showTimePicker(TimePickerView.Type.YEAR_MONTH_DAY,date,"选择日期","1",mEditText.getEditableText().toString(),"");
  }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(pvOptions.isShowing()){
                pvOptions.dismiss();
                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    /**
     * 弹出日期选择器
     *@param outDate  0代表可以选择之前的时间
     * @param type 选择器模式
     */
    private void showTimePicker(final TimePickerView.Type type, Date date, String title,String outDate,String timeRange,final String callback) {

        //Sun Jun 18 00:00:00 GMT+08:00 2017
        final TimePickerView timePickerView = new TimePickerView(MainActivity.this, type);
        timePickerView.setCancelable(false);
        timePickerView.setTitle(title);
        Calendar endCalendar = new GregorianCalendar(WheelTime.DEFULT_END_YEAR, 11, 31);
        if ("0".equals(outDate)) {
            Calendar startCalendar = new GregorianCalendar(WheelTime.DEFULT_START_YEAR, 11, 31);
            timePickerView.setTimeRange(startCalendar.getTimeInMillis(), endCalendar.getTimeInMillis());
            Date nowDate = new Date();
            if (date != null) {
                timePickerView.setTime(date);
            } else {
                timePickerView.setTime(nowDate);
            }
        } else if ("1".equals(outDate)) {
            //不能选择之前的时间
            Calendar startCalendar = Calendar.getInstance();
            /*if (!TextUtils.isEmpty(timeRange) && timeRange.matches("\\d{2}:\\d{2}-\\d{2}:\\d{2}")) {
                //解析timeRange的截止时间
                String[] var = timeRange.split("-");
                String[] secondAppoint = var[1].split(":");
                int hourMax = parseInt(secondAppoint[0]);
                int secondMin = parseInt(secondAppoint[1]);
                //获取此刻时间
                int nowHour =startCalendar.get(Calendar.HOUR);
                int nowMin=startCalendar.get(Calendar.MINUTE);
                //比较此刻与预约截止时间
                if (nowHour > hourMax || (nowHour == hourMax && nowMin >= secondMin)) {
                    startCalendar.add(Calendar.DATE, 1);
                    timePickerView.setTimeRange(startCalendar.getTimeInMillis(), endCalendar.getTimeInMillis());
                    if (date != null) {
                        if (date.before(startCalendar.getTime())) {
                            timePickerView.setTime(startCalendar);
                        } else {
                            timePickerView.setTime(date);
                        }
                    } else {
                        timePickerView.setTime(startCalendar);
                    }

                } else {*/

            timePickerView.setTimeRange(startCalendar.getTimeInMillis(), endCalendar.getTimeInMillis());
            if (date != null) {
                if (date.before(startCalendar.getTime())) {
                    System.out.println(startCalendar.get(Calendar.DATE)+"--"+startCalendar.get(Calendar.MONTH));
                    timePickerView.setTime(startCalendar);
                } else {
                    timePickerView.setTime(date);
                }
            } else {
                timePickerView.setTime(startCalendar);
            }
            //           }
        }

        timePickerView.show();
        timePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                Calendar calendaraa = Calendar.getInstance();
                if (date == null) {
                    calendaraa.setTimeInMillis(System.currentTimeMillis());
                } else {
                    calendaraa.setTime(date);
                }
                JSONObject jsonObject = new JSONObject();
//                int index=((NumericWheelAdapter)timePickerView.getWheelTime().getWv_year().getAdapter()).indexOf(2016);
//                timePickerView.getWheelTime().getWv_year().setCurrentItem(index);

                if (TimePickerView.Type.YEAR_MONTH_DAY.equals(type)) {
                    int year = calendaraa.get(Calendar.YEAR);
                    int month = calendaraa.get(Calendar.MONTH);
                    int day = calendaraa.get(Calendar.DAY_OF_MONTH);
//                    Toast.makeText(activity, year + "年" + (month + 1) + "月" + day + "日", Toast.LENGTH_SHORT).show();
                    try {

                        String monthStr=(month+1)>9?String.valueOf(month+1):"0"+String.valueOf(month+1);
                        String dayStr=day>9?String.valueOf(day):"0"+String.valueOf(day);

                        jsonObject.put("c1", String.valueOf(year));
                        jsonObject.put("c2", monthStr);
                        jsonObject.put("c3", dayStr);
                        selectedDate = year + "-" + monthStr + "-" + dayStr;


                        //保存选择的日期
                            /*String monthStr, dayStr;
                            int sdMonth = month + 1;
                            if (sdMonth > 0 && sdMonth < 10) {
                                monthStr = "0" + sdMonth;
                            } else {
                                monthStr = String.valueOf(sdMonth);
                            }
                            if (day > 0 && day < 9) {
                                dayStr = "0" + day;
                            } else {
                                dayStr = String.valueOf(day);
                            }*/
                            selectedDate = year + "-" + monthStr + "-" + dayStr;
                        mDate_tv.setText(selectedDate);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        timePickerView.setSelectListener(new SelectListener() {
            @Override
            public void onCancel() {
            }
        });
    }
    public void showTips(final Activity activity, final String msg){

        if(activity!=null&&!activity.isFinishing()){
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity,"预约时间错误，请稍后再试",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
