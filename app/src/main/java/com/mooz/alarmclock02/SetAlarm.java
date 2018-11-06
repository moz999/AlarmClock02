package com.mooz.alarmclock02;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import com.mooz.alarmclock02.DataBase.AlarmDBAdapter;
import com.mooz.alarmclock02.DataBase.AlarmTableHelper;

public class SetAlarm extends Activity {

    //SQL
    AlarmDBAdapter db;

    //画面上のウィジェット
    private TimePicker timePicker;
    private ToggleButton sunday;
    private ToggleButton monday;
    private ToggleButton tuesday;
    private ToggleButton wednesday;
    private ToggleButton thursday;
    private ToggleButton friday;
    private ToggleButton saturday;
    private TextView txtSnoozeTime;
    private SwitchCompat snoozeSwitch;
    private TextView txtSound;
    private Button btnSetAlarm;

    //実際の値が入る変数
    private int mEnable;
    private int mHour;
    private int mMinute;
    private int mSnooze;
    private int mSnoozeTime;
    private String mSound;
    private int mSunday;
    private int mMonday;
    private int mTuesday;
    private int mWednesday;
    private int mThursday;
    private int mFriday;
    private int mSaturday;


    @SuppressLint("ResourceAsColor")
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_fragment);

        findViews();

        //TimePickerは24時間表示
        timePicker.setIs24HourView(true);

        //スヌーズ用スイッチボタンを押したとき
        snoozeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //オンのときはダイアログ表示
                if(isChecked){
                    Log.d("AlarmClock02", "スヌーズスイッチを ON にしました");
                }
                //オフにしたときの処理
                else{
                    Log.d("AlarmClock02", "スヌーズスイッチを OFF にしました");
                }
            }
        });

        //登録ボタンをクリックしたときの処理
        btnSetAlarm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                saveList(); //DBへの登録

                //画面遷移
                Intent intent = new Intent(SetAlarm.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 各ViewのIDを取得する
     */
    private void findViews(){
        timePicker = findViewById(R.id.timePicker);
        sunday = findViewById(R.id.select_sunday);
        monday = findViewById(R.id.select_monday);
        tuesday = findViewById(R.id.select_tuesday);
        wednesday = findViewById(R.id.select_wednesday);
        thursday = findViewById(R.id.select_thursday);
        friday = findViewById(R.id.select_friday);
        saturday = findViewById(R.id.select_saturday);
        txtSnoozeTime = findViewById(R.id.txtSnoozeTime);
        snoozeSwitch = findViewById(R.id.snoozeSwitch);
        txtSound = findViewById(R.id.txtSound);
        btnSetAlarm = findViewById(R.id.btnSetAlarm);
    }

    /**
     * DBに登録する
     */
    private void saveList(){
        db = new AlarmDBAdapter(this);

        mEnable = 1;
        //時間を取得
        mHour = timePicker.getHour();
        mMinute = timePicker.getMinute();
        //曜日のトグルボタンの値を取得
        if(sunday.isChecked()){
            mSunday = 1;
        }else{
            mSunday = 0;
        }
        if(monday.isChecked()){
            mMonday = 1;
        }else{
            mMonday = 0;
        }
        if(tuesday.isChecked()){
            mTuesday = 1;
        }else{
            mTuesday = 0;
        }
        if(wednesday.isChecked()){
            mWednesday = 1;
        }else{
            mWednesday = 0;
        }
        if(thursday.isChecked()){
            mThursday = 1;
        }else{
            mThursday = 0;
        }
        if(friday.isChecked()){
            mFriday = 1;
        }else{
            mFriday = 0;
        }
        if(saturday.isChecked()){
            mSaturday = 1;
        }else{
            mSaturday = 0;
        }
        //スヌーズ時間の取得
        if(snoozeSwitch.isChecked()){
            mSnooze = 1;
        }else{
            mSnooze = 0;
            mSnoozeTime = 0;
        }
        //音声タイトルを取得
        mSound = txtSound.getText().toString();


        //データベースに書き込み
        db.openDB();
        db.saveDB(mEnable,mHour,mMinute,mSnooze,mSnoozeTime,mSound,
                mSunday,mMonday,mTuesday,mWednesday,mThursday,mFriday,mSaturday);
        db.closeDB();
    }
}
