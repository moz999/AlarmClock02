package com.mooz.alarmclock02;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.mooz.alarmclock02.DataBase.AlarmDBAdapter;

import java.util.ArrayList;

public class AlarmFragment extends Fragment implements OnRecyclerListener {

    private Context mContext = null;
    private View mView;
    private RecyclerFragmentListener mFragmentListener = null;
    private CardView cardViewList;

    //RecyclerViewとAdapter
    private RecyclerView mRecyclerView = null;
    private RecyclerAdapter mAdapter = null;

    //Database
    private AlarmDBAdapter dbAdapter;
    private ArrayList<AlarmGetterSetter> items;
    protected AlarmGetterSetter listItem;
    private String[] columns = null;


    public interface RecyclerFragmentListener{
        void onRecyclerEvenet();
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        if(!(context instanceof MainActivity)){
            throw new UnsupportedOperationException(
                    "Listener is not Implementation.");
        }else{
            mContext = context;
            //mFragmentListener = (RecyclerFragmentListener) context;
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        mView = inflater.inflate(R.layout.alarm_fragment, container, false);

        //配列を用意
        items = new ArrayList<>();
        items.clear(); //リストの更新のため、ここでクリアしとく

        //DataBaseから値を取得してカプセル化
        loadMyList();

        //RecyclerViewの参照を取得
        mRecyclerView = (RecyclerView)mView.findViewById(R.id.alarmList);
        //コンテンツの変更によってRecyclerViewのレイアウトサイズが変更されないことがわかっている場合は、
        //この設定を使用してパフォーマンスを向上させる
        mRecyclerView.setHasFixedSize(true);
        //レイアウトマネージャを設定
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        //区切り線をセットする
        DividerItemDecoration itemDecoration = new
                DividerItemDecoration(mContext, new LinearLayoutManager(mContext).getOrientation());
        mRecyclerView.addItemDecoration(itemDecoration);
        //区切り線の色を変更する
        itemDecoration.setDrawable(ContextCompat.getDrawable(mContext,R.drawable.divider));

        //Adapterをセットする
        mAdapter = new RecyclerAdapter(mContext, items, this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged(); //更新する

        // Item click ( Long ) on list item
        mAdapter.setOnItemLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                Log.d("Alarm Clock 02", String.valueOf(items.get(v.getId())));
                final int position = v.getId();

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.MyAlertDialog);
                builder.setTitle("Delete");
                builder.setMessage("Would you like to delete it ?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //get ID
                        String listId = String.valueOf(listItem.getId());

                        dbAdapter = new AlarmDBAdapter(mContext);
                        dbAdapter.openDB();
                        dbAdapter.selectDelete(listId);
                        dbAdapter.closeDB();

                        items.clear();
                        loadMyList();
                        mAdapter.notifyItemRemoved(position);
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                //Create dialog
                AlertDialog dialog = builder.create();
                dialog.show();

                return false;
            }
        });

        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * オリジナルのクリック処理
     * @param v
     * @param position
     */
    @Override
    public void onRecyclerClicked(View v, int position) {

    }

    /**
     * DataBaseから値を取得してカプセル化
     */
    public void loadMyList(){
        //DBに接続
        dbAdapter = new AlarmDBAdapter(mContext);
        dbAdapter.openDB();

        //DBカラムを取得する
        Cursor c = dbAdapter.getDB(columns);
        if(c.moveToFirst()){
            do{
                listItem = new AlarmGetterSetter(
                        c.getInt(0),
                        c.getInt(1),
                        c.getInt(2),
                        c.getInt(3),
                        c.getInt(4),
                        c.getInt(5),
                        c.getString(6),
                        c.getString(7),
                        c.getInt(8),
                        c.getInt(9),
                        c.getInt(10),
                        c.getInt(11),
                        c.getInt(12),
                        c.getInt(13),
                        c.getInt(14));

                        items.add(listItem);
            }while(c.moveToNext());
        }

        c.close();
        dbAdapter.closeDB();
    }
}
