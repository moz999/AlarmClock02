package com.mooz.alarmclock02;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button mBtnAlarmSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnAlarmSet = findViewById(R.id.btnStartSet);

        if(savedInstanceState == null) {
            AlarmFragment fragment = new AlarmFragment();
            //Fragmentの追加・削除などはTransactionを使用する
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            //追加. 1つだけなので、念のためreplace使用
            //transaction.replace(R.id.container, fragment);
            transaction.add(R.id.container, fragment);
            transaction.commit();
        }

        mBtnAlarmSet.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if(v == mBtnAlarmSet){
                    Log.d("AlarmClock", "onClickが呼び出されました");

                    Intent intent = new Intent(MainActivity.this, SetAlarm.class);
                    startActivity(intent);
                }
            }
        });

    }
}
