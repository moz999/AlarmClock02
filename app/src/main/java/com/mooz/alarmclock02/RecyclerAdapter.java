package com.mooz.alarmclock02;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{

    private static final int lDisable = 0;
    private static final int lEnable = 1;
    private static final String disColor = "#b0c4de";
    private static final String enaColor = "#00fa9a";

    private LayoutInflater mInflater;
    private ArrayList<AlarmGetterSetter> mData;
    private AlarmGetterSetter listItem;
    private Context mContext;
    private OnRecyclerListener mListener;

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

        //データ表示
        if(mData != null && mData.size() > position && mData.get(position) != null){
            viewHolder.pTxtHour.setText(toString().valueOf(listItem.getHour()));
            viewHolder.pTxtMinute.setText(toString().valueOf(listItem.getMinute()));

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
        }

        //クリック処理
        viewHolder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mListener.onRecyclerClicked(v, position);
            }});
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
        }
    }
}
