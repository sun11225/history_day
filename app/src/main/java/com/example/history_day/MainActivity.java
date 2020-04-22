package com.example.history_day;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.example.history_day.adapter.HistoryAdapter;
import com.example.history_day.base.BaseActivity;
import com.example.history_day.base.ContentURL;
import com.example.history_day.bean.Historybean;
import com.example.history_day.bean.LaoHuangLi;
import com.example.history_day.bean.User;
import com.example.history_day.ui.HistoryActivity;
import com.example.history_day.ui.HistoryDescActivity;
import com.example.history_day.ui.LoginActivity;
import com.google.gson.Gson;

import com.example.history_day.R;
import com.google.gson.reflect.TypeToken;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private ListView listView;
    private ImageButton imageButton, ibBack;
    private List<Historybean.ResultBean> mDatas;
    private HistoryAdapter adapter;
    private Calendar calendar;
    private Date date;
    //    声明头布局当中的TextView
    TextView yinliTv, dayTv, weekTv, yangliTv, baijiTv, wuxingTv, chongshaTv, jishenTv, xiongshenTv, yiTv, jiTv;

    Historybean historybean;
    private int year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (Build.VERSION.SDK_INT>=21){
//            View view=getWindow().getDecorView();
//           //布局颜色显示到状态栏
//            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            //设置状态栏透明色
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.main_lv);
        imageButton = findViewById(R.id.main_imgbtn);
        imageButton.setOnClickListener(this);

        //退出登录
        ibBack = findViewById(R.id.main_imgbtn_back);
        ibBack.setOnClickListener(this);

        mDatas = new ArrayList<>();
        adapter = new HistoryAdapter(this, mDatas);
        listView.setAdapter(adapter);


        //获取日期
        calendar = Calendar.getInstance();
        date = new Date();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        addHeaderAndFootView();

        //加载历史的今日数据
        String tadayHistoryURL = ContentURL.getTadayHistoryURL("1.0", month, day);
        loadData(tadayHistoryURL);

        //设置子项点击事件
        // 因为ListView添加头布局了，所以position对应集合的位置发生变化，集合第0个数据，position为第1个数据，所以要减掉一个
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(MainActivity.this, HistoryDescActivity.class);
                //获取详细事件的id传递
                Historybean.ResultBean resultBean = mDatas.get(position - 1);
                String resultBean_id = resultBean.get_id();
                intent.putExtra("historyId", resultBean_id);
                startActivity(intent);
            }
        });

//        initHeaderView();
    }

    //给listview添加头尾布局
    private void addHeaderAndFootView() {
        View inflateHeader = LayoutInflater.from(this).inflate(R.layout.activity_header, null);
        initHeaderView(inflateHeader);
        listView.addHeaderView(inflateHeader, null, false);

        View inflateFooter = LayoutInflater.from(this).inflate(R.layout.activity_footer, null);
        inflateFooter.setTag("footer");
        //设置点击事件
        inflateFooter.setOnClickListener(this);
        listView.addFooterView(inflateFooter);
    }

    //初始化头布局
    private void initHeaderView(View inflaterHeader) {
        // 初始化ListView头布局当中的每一个控件
        yinliTv = inflaterHeader.findViewById(R.id.tv_header_nongli);
        dayTv = inflaterHeader.findViewById(R.id.tv_header_day);
        weekTv = inflaterHeader.findViewById(R.id.tv_header_week);
        yangliTv = inflaterHeader.findViewById(R.id.tv_header_yangli);
        baijiTv = inflaterHeader.findViewById(R.id.tv_header_baiji);
        wuxingTv = inflaterHeader.findViewById(R.id.tv_header_wuxing);
        chongshaTv = inflaterHeader.findViewById(R.id.tv_header_chongsha);
        jishenTv = inflaterHeader.findViewById(R.id.tv_header_jishen);
        xiongshenTv = inflaterHeader.findViewById(R.id.tv_header_xiongshen);
        yiTv = inflaterHeader.findViewById(R.id.tv_header_yi);
        jiTv = inflaterHeader.findViewById(R.id.tv_header_ji);

        //将日期对象转换为指定格式的字符串形式 传入指定时间对象格式获取老黄历数据
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
        String time = dateFormat.format(date);
        String laoHuangLiURL = ContentURL.getLaoHuangLiURL(time);

        loadLaoHuangLiData(laoHuangLiURL);

    }

    //获取老黄历接口的数据
    private void loadLaoHuangLiData(String laoHuangLiURL) {
        RequestParams params = new RequestParams(laoHuangLiURL);
        x.http().get(params, new CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                //解析数据
                LaoHuangLi laoHuangLi = new Gson().fromJson(result, LaoHuangLi.class);
                LaoHuangLi.ResultBean resultBean = laoHuangLi.getResult();
                yinliTv.setText("农历 " + resultBean.getYinli() + "(阴历)");

                //阳历格式 yyyy-mm-dd 自定义格式
                String[] split = resultBean.getYangli().split("-");

                //根据年月日获取星期几
                String week = getWeek(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));

                yangliTv.setText("公历 " + split[0] + "年" + split[1] + "月" + split[2] + "日" + week + "(阳历)");

                dayTv.setText(split[2]);
                weekTv.setText(week);
                baijiTv.setText("彭祖百忌: " + resultBean.getBaiji());
                wuxingTv.setText("五行: " + resultBean.getWuxing());
                chongshaTv.setText("冲煞: " + resultBean.getChongsha());
                jishenTv.setText("吉神宜趋: " + resultBean.getJishen());
                xiongshenTv.setText("凶神宜忌: " + resultBean.getXiongshen());
                yiTv.setText("宜: " + resultBean.getYi());
                jiTv.setText("忌: " + resultBean.getJi());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }

    private String getWeek(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        String weeks[] = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        int index = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (index < 0) {
            index = 0;
        }
        return weeks[index];
    }


    @Override
    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.main_imgbtn:
//                calenderDialog();
//                break;
//            case R.id.main_imgbtn_back:
//                User.logOut();
//                User user = BmobUser.getCurrentUser(User.class);
//                startActivity(new Intent(MainActivity.this, LoginActivity.class));
//                finish();
//                break;
//        }
        if (v.getId() == R.id.main_imgbtn) {
            calenderDialog();
            return;
        }
        if (v.getId() == R.id.main_imgbtn_back) {
            User.logOut();
            User user = BmobUser.getCurrentUser(User.class);
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            Toast.makeText(getApplicationContext(),"退出登录成功！",Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        String tag = (String) v.getTag();
        if (tag.equals("footer")) {
            Toast.makeText(MainActivity.this, "需要管理员权限！", Toast.LENGTH_LONG).show();
//            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
//
//            //传递历史数据到活动 bundle传值 intent传值
//            if (historybean != null) {
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("history", historybean);
//                intent.putExtras(bundle);
//            }
//            startActivity(intent);
        }
    }

    //弹出日历对话框
    private void calenderDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if (year < 2020) {
                    Toast.makeText(getApplicationContext(), "需要管理员权限！", Toast.LENGTH_LONG).show();
                } else {
                    //改变老黄历现实的内容
                    String time = year + "-" + (month + 1) + "-" + dayOfMonth;
                    String laoHuangLiURL = ContentURL.getLaoHuangLiURL(time);
                    loadLaoHuangLiData(laoHuangLiURL);

                    //改变历史今天数据内容
                    String historyURL = ContentURL.getTadayHistoryURL("1.0", (month + 1), dayOfMonth);
                    loadData(historyURL);
                }
            }

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        dialog.show();
    }

    @Override
    public void onSuccess(String result) {
        mDatas.clear();
//        List<Historybean.ResultBean> resultBeanList=new Gson().fromJson(result,new TypeToken<List<Historybean.ResultBean>>(){}.getType());
//        for (Historybean.ResultBean resultBean:resultBeanList){
//            year = resultBean.getYear();
//            for (int i=0;i<8;i++){
//                if (year>1700){
//                    mDatas.add(resultBeanList.get(i));
//                }
//            }
//        }
        historybean = new Gson().fromJson(result, Historybean.class);
        List<Historybean.ResultBean> list = historybean.getResult();

        for (int i = 0; i < 8; i++) {

            mDatas.add(list.get(i));

        }
        adapter.notifyDataSetChanged();
    }
}