package com.mooz.alarmclock02;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.mooz.alarmclock02.DataBase.AlarmDBAdapter;

import java.util.ArrayList;

import static android.widget.LinearLayout.VERTICAL;

public class AlarmFragment extends Fragment implements OnRecyclerListener {

    private Context mContext = null;
    private View mView;
    private RecyclerFragmentListener mFragmentListener = null;

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

        //Adapterをセットする
        mAdapter = new RecyclerAdapter(mContext, items, this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged(); //更新する

        //区切り線をセットする
        DividerItemDecoration itemDecoration = new
                DividerItemDecoration(mContext, new LinearLayoutManager(mContext).getOrientation());
        mRecyclerView.addItemDecoration(itemDecoration);
        //区切り線の色を変更する
        itemDecoration.setDrawable(ContextCompat.getDrawable(mContext,R.drawable.divider));

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
    private void loadMyList(){
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
                        c.getInt(7),
                        c.getInt(8),
                        c.getInt(9),
                        c.getInt(10),
                        c.getInt(11),
                        c.getInt(12),
                        c.getInt(13));

                        items.add(listItem);
            }while(c.moveToNext());
        }

        c.close();
        dbAdapter.closeDB();
    }
}
