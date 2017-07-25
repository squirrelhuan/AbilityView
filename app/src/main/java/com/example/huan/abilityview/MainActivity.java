package com.example.huan.abilityview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.huan.abilityview.Model.AbilityModel;
import com.example.huan.abilityview.View.AbilityView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = (TextView) findViewById(R.id.tv_test);

        StringBuffer stringBuffer = new StringBuffer();
        for(int i=0;i<=360;i++){
            stringBuffer.append("角度："+i*10+",sin="+String.format("%.2f",Math.sin(i*10*Math.PI/180))+",cos="+String.format("%.2f",Math.cos(i*10*Math.PI/180))+"sss="+String.format("%.2f",Math.sin(Math.PI*30/180))+"/n/r");
        }
        //textView.setText(stringBuffer);

       AbilityView av_test = (AbilityView) findViewById(R.id.av_test);
        List<AbilityModel> models = new ArrayList<AbilityModel>();
        Random random = new Random();
        for (int i=0;i<9;i++){
            AbilityModel model = new AbilityModel("个人能力", (random.nextInt(5)+4)/10f);
            models.add(model);
        }
        av_test.setData(models,this);
    }
}
