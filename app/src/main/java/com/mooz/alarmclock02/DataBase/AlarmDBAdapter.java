package com.mooz.alarmclock02.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AlarmDBAdapter {

    private SQLiteDatabase db;
    private Context mContext;
    private AlarmTableHelper DBHelper;

    /**
     * コンストラクタ
     * @param context
     */
    public AlarmDBAdapter(Context context){
        this.mContext = context;
        DBHelper = new AlarmTableHelper(mContext);
    }

    /**
     * DBの読み書き
     * @return 自身のオブジェクト
     */
    public AlarmDBAdapter openDB(){
        db = DBHelper.getWritableDatabase();
        return this;
    }

    /**
     * DBの読み込み
     * @return 自身のオブジェクト
     */
    public AlarmDBAdapter readDB(){
        db = DBHelper.getReadableDatabase();
        return this;
    }

    /**
     * DBを閉じる
     */
    public void closeDB(){
        db.close();
        db = null;
    }

    /**
     * DBの保存
     * @param enable
     * @param hour
     * @param minute
     * @param snooze
     * @param snoozeTime
     * @param sound
     * @param sunday
     * @param monday
     * @param tuesday
     * @param wednesday
     * @param thursday
     * @param friday
     * @param saturday
     */
    public void saveDB(int enable, int hour, int minute, int snooze, int snoozeTime, String sound,
                       String soundUrl, int sunday, int monday, int tuesday, int wednesday,
                       int thursday, int friday, int saturday){
        //トランザクション開始
        db.beginTransaction();

        try{
            ContentValues values = new ContentValues();

            values.put(DBHelper.COL_ENABLE, enable);
            values.put(DBHelper.COL_HOUR, hour);
            values.put(DBHelper.COL_MINUTE, minute);
            values.put(DBHelper.COL_SNOOZE, snooze);
            values.put(DBHelper.COL_SNZTIME, snoozeTime);
            values.put(DBHelper.COL_SOUND, sound);
            values.put(DBHelper.COL_SOUND_URI, soundUrl);
            values.put(DBHelper.COL_SUNDAY, sunday);
            values.put(DBHelper.COL_MONDAY, monday);
            values.put(DBHelper.COL_TUESDAY, tuesday);
            values.put(DBHelper.COL_WEDNESDAY, wednesday);
            values.put(DBHelper.COL_THURSDAY, thursday);
            values.put(DBHelper.COL_FRIDAY, friday);
            values.put(DBHelper.COL_SATURDAY, saturday);

            db.insert(DBHelper.DB_TABLE, null, values);

            //Transactionの成功は必ず伝える
            db.setTransactionSuccessful();

        }catch (Exception e){
            e.printStackTrace();

        }finally {
            db.endTransaction();
        }
    }

    /**
     * DBの情報を取得する
     * @param columns
     * @return
     */
    public Cursor getDB(String[] columns){
        //queryメソッド
        // 1 : テーブル名
        // 2 : 取得するカラム名
        // 3 : WHERE句
        // 4 : 3で？を使用した場合に使用
        // 5 : GROUP BY
        // 6 : HAVING
        // 7 : ORDER BY
        return db.query(DBHelper.DB_TABLE, columns,
                null, null, null, null, null);
    }

    /**
     * データベースから１行削除する
     * @param position
     */
    public void selectDelete(String position){
        db.beginTransaction();

        try{
            db.delete(AlarmTableHelper.DB_TABLE, DBHelper.COL_ID + " = ?", new String[]{position});
            db.setTransactionSuccessful();

        }catch(Exception e){
            e.printStackTrace();

        }finally{
            db.endTransaction();
        }
    }

    /**
     * Enable switch
     * @param position
     * @param value
     */
    public void updateEnable(String position, String value){
        db.beginTransaction();

        try{
            ContentValues cv = new ContentValues();
            cv.put("Enable", value);
            db.update(AlarmTableHelper.DB_TABLE, cv, DBHelper.COL_ID + " = ?", new String[]{position});

            db.setTransactionSuccessful();

        }catch(Exception e){
            e.printStackTrace();
        }finally {
            db.endTransaction();
        }

    }

}
