package com.mooz.alarmclock02;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.mooz.alarmclock02.DataBase.AlarmDBAdapter;

import java.util.ArrayList;
import java.util.Locale;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{

    private static final int lDisable = 0;
    private static final int lEnable = 1;
    private static final String disColor = "#b0c4de";
    private static final String enaColor = "#00fa9a";

    private LayoutInflater mInflater;
    private ArrayList<AlarmGetterSetter> mData;
    private AlarmGetterSetter listItem;
    private AlarmGetterSetter listEnable;
    private Context mContext;
    private OnRecyclerListener mListener;

    private AlarmDBAdapter dbAdapter;
    private AlarmFragment alarmFragment;
    private View.OnLongClickListener listener;

    /**
     * コンストラクタ
     * @param context
     * @param data
     * @param listener
     */
    public RecyclerAdapter(Context context, ArrayList<AlarmGetterSetter> data, OnRecyclerListener listener){
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mData = data;
        mListener = listener;
    }

    //===========================================================================================
    // implements of RecyclerView.Adapter
    //===========================================================================================
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //表示するレイアウトを設定
        return new ViewHolder(mInflater.inflate(R.layout.alarm_list, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder viewHolder, final int position) {

        //データを取得
        listItem = mData.get(position);

        //String for time
        String mHour;
        String mMinute;

        //データ表示
        if(mData != null && mData.size() > position && mData.get(position) != null){
            mHour = String.format(Locale.JAPAN, "%02d", listItem.getHour());    //Format time "0" -> "00"
            mMinute = String.format(Locale.JAPAN, "%02d", listItem.getMinute());
            viewHolder.pTxtHour.setText(mHour);
            viewHolder.pTxtMinute.setText(mMinute);

            if(listItem.getSunday() == lDisable){
                viewHolder.pTxtSunday.setTextColor(Color.parseColor(disColor));
            }else{
                viewHolder.pTxtSunday.setTextColor(Color.parseColor(enaColor));
            }

            if(listItem.getMonday() == lDisable){
                viewHolder.pTxtMonday.setTextColor(Color.parseColor(disColor));
            }else{
                viewHolder.pTxtMonday.setTextColor(Color.parseColor(enaColor));
            }

            if(listItem.getTuesday() == lDisable){
                viewHolder.pTxtTuesday.setTextColor(Color.parseColor(disColor));
            }else{
                viewHolder.pTxtTuesday.setTextColor(Color.parseColor(enaColor));
            }

            if(listItem.getWednesday() == lDisable){
                viewHolder.pTxtWednesday.setTextColor(Color.parseColor(disColor));
            }else{
                viewHolder.pTxtWednesday.setTextColor(Color.parseColor(enaColor));
            }

            if(listItem.getThursday() == lDisable){
                viewHolder.pTxtThursday.setTextColor(Color.parseColor(disColor));
            }else{
                viewHolder.pTxtThursday.setTextColor(Color.parseColor(enaColor));
            }

            if(listItem.getFriday() == lDisable){
                viewHolder.pTxtFriday.setTextColor(Color.parseColor(disColor));
            }else{
                viewHolder.pTxtFriday.setTextColor(Color.parseColor(enaColor));
            }

            if(listItem.getSaturday() == lDisable){
                viewHolder.pTxtSaturday.setTextColor(Color.parseColor(disColor));
            }else{
                viewHolder.pTxtSaturday.setTextColor(Color.parseColor(enaColor));
            }

            viewHolder.pTxtSoundName.setText(listItem.getSound());

            //Check enable switch is OFF or ON
            if(listItem.getEnable() == lEnable){
                viewHolder.pEnable.setChecked(true);
            }if(listItem.getEnable() == lDisable){
                viewHolder.pEnable.setChecked(false);
            }
        }

        //クリック処理
        viewHolder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mListener.onRecyclerClicked(v, position);
            }});

        viewHolder.itemView.setId(viewHolder.getAdapterPosition());
        //Long click listener
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                listener.onLongClick(v);
                return false;
            }
        });

        //Enable Switch button is swiped!
        listEnable = mData.get(position);
        final int enableId = listEnable.getId();
        viewHolder.pEnable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("AlarmClock02", "listItem number is " + String.valueOf(listItem.getId()) + " is Clicked!!!!!!");
                Log.d("AlarmClock02", "enableId number is " + String.valueOf(enableId) + " is Clicked!!!!!!");

                dbAdapter = new AlarmDBAdapter(mContext);

                if(isChecked){
                    dbAdapter.openDB();
                    dbAdapter.updateEnable(String.valueOf(enableId), "1");
                    dbAdapter.closeDB();
                }else{
                    dbAdapter.openDB();
                    dbAdapter.updateEnable(String.valueOf(enableId),"0");
                    dbAdapter.closeDB();
                }
            }
        });

        //margin is given to the last position
        int lastLine = getItemCount() - 1;
        if (position == lastLine){
            Log.d("AlarmClock02",lastLine + " is last position");
            ViewGroup.LayoutParams lp = viewHolder.pCardView.getLayoutParams();
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams)lp;
            mlp.setMargins(mlp.leftMargin, mlp.topMargin, mlp.rightMargin, 300);
            viewHolder.pCardView.setLayoutParams(mlp);
        }

    }

    @Override
    public int getItemCount() {
        //データのサイズを返す
        if(mData != null){
            return mData.size();
        }else{
            return 0;
        }
    }

    /**
     *
     * @param listener
     */
    public void setOnItemLongClickListener(View.OnLongClickListener listener){
        this.listener = listener;
    }


    //===========================================================================================

    /**
     * 内部クラス
     */
    public class ViewHolder extends RecyclerView.ViewHolder{

        //alarm_list.xmlの情報
        public TextView pTxtHour;
        public TextView pTxtMinute;
        public SwitchCompat pEnable;
        public TextView pTxtSunday;
        public TextView pTxtMonday;
        public TextView pTxtTuesday;
        public TextView pTxtWednesday;
        public TextView pTxtThursday;
        public TextView pTxtFriday;
        public TextView pTxtSaturday;
        public TextView pTxtSoundName;
        public CardView pCardView;

        public ViewHolder(View itemView){
            super(itemView);

            pTxtHour = itemView.findViewById(R.id.txtHour);
            pTxtMinute = itemView.findViewById(R.id.txtMinute);
            pEnable = itemView.findViewById(R.id.enable_switch);
            pTxtSunday = itemView.findViewById(R.id.txtSunday);
            pTxtMonday = itemView.findViewById(R.id.txtMonday);
            pTxtTuesday = itemView.findViewById(R.id.txtTuesday);
            pTxtWednesday = itemView.findViewById(R.id.txtWednesday);
            pTxtThursday = itemView.findViewById(R.id.txtThursday);
            pTxtFriday = itemView.findViewById(R.id.txtFriday);
            pTxtSaturday = itemView.findViewById(R.id.txtSaturday);
            pTxtSoundName= itemView.findViewById(R.id.soundName);
            pCardView = itemView.findViewById(R.id.cardViewItem);
        }
    }
}
