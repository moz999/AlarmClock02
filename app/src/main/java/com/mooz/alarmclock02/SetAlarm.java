package com.mooz.alarmclock02;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import com.mooz.alarmclock02.DataBase.AlarmDBAdapter;
import com.mooz.alarmclock02.DataBase.AlarmTableHelper;

import java.util.ArrayList;
import java.util.List;

public class SetAlarm extends Activity {

    //Context
    Context mContext;
    //Activity
    Activity mActivity;

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
    private String mSoundUrl;
    private int mSunday;
    private int mMonday;
    private int mTuesday;
    private int mWednesday;
    private int mThursday;
    private int mFriday;
    private int mSaturday;

    //ダイアログ用
    final String[] mChoiceTime = {"1分","2分","3分","4分","5分","6分","7分","8分","9分","10分"};
    DialogInterface.OnClickListener mItemListener;
    DialogInterface.OnClickListener mButtonListener;

    //Sound list
    ArrayList<String> soundList;
    ArrayList<String> soundSource;
    final Ringtone[] mRingtone = new Ringtone[1];

    @SuppressLint("ResourceAsColor")
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_fragment);

        //Get the Application Context
        mContext = getApplicationContext();
        mActivity = SetAlarm.this;

        //Find my parts on my Activity Graphic
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

        //スヌーズ用テキストをクリックしたとき
        txtSnoozeTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choiceSnoozeTime(mActivity, mContext);
            }
        });

        //Touch the sound name
        txtSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choiceSound(mActivity, mContext);
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
     * スヌーズタイムを設定するダイアログを表示
     * @param activity
     * @param context
     */
    private void choiceSnoozeTime(Activity activity, Context context){
        //Selected snooze time
        final String[] selectTime = new String[1];
        //initializing a new alert dialog
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        //Set the alert dialog title
        builder.setTitle("Select Snooze time");

        //initializing a new array adapter instance
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(
                mContext, //context
                R.layout.snooze_time_list, // Layout
                mChoiceTime //List
                );

        builder.setSingleChoiceItems(arrayAdapter, -1,
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, final int which) {
                // Get the alert dialog selected item
                Log.d("Alarm Clock 02 : ", "You checked the " + (which + 1) + " minutes.");
                selectTime[0] = String.valueOf(which + 1) + " 分";
                txtSnoozeTime.setText(selectTime[0]);

                dialog.dismiss();
            }
        });

        //Create the alert dialog
        AlertDialog dialog = builder.create();
        //Display the alert dialog
        dialog.show();
    }

    /**
     * Choice the alarm sound.
     * @param activity
     * @param context
     */
    private void choiceSound(Activity activity, final Context context){
        final RingtoneManager ringtoneManager = new RingtoneManager(context);
        final Uri uri = ringtoneManager.getRingtoneUri(ringtoneManager.TYPE_ALARM);
        Cursor cursor = ringtoneManager.getCursor();
        soundList = new ArrayList<>();
        soundSource = new ArrayList<>();

        while(cursor.moveToNext()){
            Log.d("Alarm Clock 02 : ", "Ringtone is " + cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX));
            soundList.add(cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX));
        }

        //Set up alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        //Set title to dialog window
        builder.setTitle("Select the sound");
        //Initializing a new array adapter instance
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(
                mContext,
                R.layout.snooze_time_list,  // Recycled layout
                soundList );    //List

        //Select a alarm sound from sound name list.
        builder.setSingleChoiceItems(arrayAdapter, -1,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Sound a music when click a list item
                        mRingtone[0] = ringtoneManager.getRingtone(which);
                        mRingtone[0].play();
                    }
                });

        //When you pushed a OK button
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String soundName = mRingtone[0].getTitle(mContext);
                txtSound.setText(soundName);
                dialog.dismiss();
                mRingtone[0].stop();
            }
        });

        //When you pushed a Cancel button
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mRingtone[0].stop();
            }
        });

        // Create a dialog
        AlertDialog dialog = builder.create();
        dialog.show();
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
        //get sound url type string
        mSoundUrl = "0";


        //データベースに書き込み
        db.openDB();
        db.saveDB(mEnable,mHour,mMinute,mSnooze,mSnoozeTime,mSound, mSoundUrl,
                mSunday,mMonday,mTuesday,mWednesday,mThursday,mFriday,mSaturday);
        db.closeDB();
    }
}
