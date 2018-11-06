package com.mooz.alarmclock02.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AlarmTableHelper extends SQLiteOpenHelper{

    public static final String DB_NAME = "AlarmTable.db";
    public static final String DB_TABLE = "alarmTable";
    public static final int DB_VERSION = 1;

    public static final String COL_ID = "_id";
    public static final String COL_ENABLE = "enable";
    public static final String COL_HOUR = "hour";
    public static final String COL_MINUTE = "minute";
    public static final String COL_SNOOZE = "snooze";
    public static final String COL_SNZTIME = "snoozeTime";
    public static final String COL_SOUND = "sound";
    public static final String COL_SUNDAY = "sunday";
    public static final String COL_MONDAY = "monday";
    public static final String COL_TUESDAY = "tuesday";
    public static final String COL_WEDNESDAY = "wednesday";
    public static final String COL_THURSDAY = "thursday";
    public static final String COL_FRIDAY = "friday";
    public static final String COL_SATURDAY = "saturday";

    public AlarmTableHelper(Context context){
        // 1 : Context
        // 2 : DB Name
        // 3 : factory -> Null でOK
        // 4 : DB Version
        super(context, DB_NAME, null, DB_VERSION);
    }

    public void onCreate(SQLiteDatabase db){
        //Create Table
        String createTable = "CREATE TABLE " + DB_TABLE + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_ENABLE + " INTEGER NOT NULL,"
                + COL_HOUR + " INTEGER NOT NULL,"
                + COL_MINUTE + " INTEGER NOT NULL,"
                + COL_SNOOZE + " INTEGER NOT NULL,"
                + COL_SNZTIME + " INTEGER,"
                + COL_SOUND + " TEXT NOT NULL,"
                + COL_SUNDAY + " INTEGER NOT NULL,"
                + COL_MONDAY + " INTEGER NOT NULL,"
                + COL_TUESDAY + " INTEGER NOT NULL,"
                + COL_WEDNESDAY + " INTEGER NOT NULL,"
                + COL_THURSDAY + " INTEGER NOT NULL,"
                + COL_FRIDAY + " INTEGER NOT NULL,"
                + COL_SATURDAY + " INTEGER NOT NULL"
                + ");";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
