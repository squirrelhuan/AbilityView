package com.example.huan.abilityview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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

        String[] titles = {"补兵数", "杀人", "助攻", "死亡", "推塔","输出伤害","承受伤害"};

        AbilityView av_test = (AbilityView) findViewById(R.id.av_test);
        List<AbilityModel> models = new ArrayList<AbilityModel>();
        Random random = new Random();

        for (int i = 0; i < titles.length; i++) {
            AbilityModel model = null;
            model = new AbilityModel(titles[i], (random.nextInt(5) + 4) / 10f);
            models.add(model);
        }
        av_test.setTitleSize(16);//设置标题文字大小
        av_test.setValueSize(12);//设置数值文字大小
        av_test.setLayer_count(5);//设置圆环层数
        av_test.setData(models, this);
    }
}
