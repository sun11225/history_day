package com.example.history_day.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.history_day.adapter.HistoryAdapter;
import com.example.history_day.bean.Historybean;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import com.example.history_day.R;

import com.example.history_day.adapter.HistoryAdapter;
import com.example.history_day.bean.Historybean;

public class HistoryActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView emptyTv;
    private ListView historyLv;
    private ImageView backIv;
    List<Historybean.ResultBean> mDatas;
    private HistoryAdapter adapter;
    Historybean historybean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        emptyTv = findViewById(R.id.history_tv);
        historyLv = findViewById(R.id.history_lv);
        backIv = findViewById(R.id.history_iv_back);
        backIv.setOnClickListener(this);

        mDatas=new ArrayList<>();
        adapter=new HistoryAdapter(this,mDatas);
        historyLv.setAdapter(adapter);

        //获取传递的数据
        try{
            Intent intent=getIntent();
            Bundle bundle = intent.getExtras();
            assert bundle != null;
            historybean = (Historybean) bundle.getSerializable("history");
            assert historybean != null;
            List<Historybean.ResultBean> result = historybean.getResult();
            mDatas.addAll(result);
            adapter.notifyDataSetChanged();

        }catch (Exception e){
            //出现异常显示文字
            emptyTv.setVisibility(View.VISIBLE);
        }


        //子项点击事件
        historyLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(HistoryActivity.this,HistoryDescActivity.class);
                //获取详细事件的id传递
                Historybean.ResultBean resultBean = mDatas.get(position);
                String resultBean_id = resultBean.get_id();
                intent.putExtra("historyId",resultBean_id);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.history_iv_back:
                finish();
                break;
        }
    }
}
