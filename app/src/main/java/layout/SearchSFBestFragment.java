package layout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.nuc.hikeplus.DBTool;
import com.nuc.hikeplus.OlderInfoActivity;
import com.nuc.hikeplus.R;
import com.nuc.hikeplus.Tools;

/**
 * Created by weeznn on 2016/9/2.
 */
public class SearchSFBestFragment extends Fragment {

    private final static String TAG = "SearchSFBestFragment";

    private FloatingActionButton fab1 = null, fab2 = null, fab3 = null;
    private ListView listView = null;
    private EditText searchEditText = null;
    private SimpleCursorAdapter adapter = null;
    private DBTool dbTool = null;
    private Cursor cursor = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbTool = new DBTool(getContext());
        cursor = dbTool.searchDB();
        Log.i(TAG, "oncreat");

        adapter = new SimpleCursorAdapter(getContext(), R.layout.list_item, cursor,
                new String[]{"receiverName", "receiverTel", "olderNo"},
                new int[]{R.id.name, R.id.tel, R.id.logistic});
        Log.i(TAG,"adapter new");
        //根据订单状态设置name颜色
        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if (view instanceof TextView && R.id.name == view.getId()) {
                    ((TextView) view).setText(cursor.getString(2));
                    if (0 != cursor.getInt(10)) {
                        //未完成订单
                        TextPaint tp = ((TextView) view).getPaint();
                        tp.setFakeBoldText(true);
                        ((TextView) view).setTextColor(getResources().getColor(R.color.order_list_name_ing_color));
                    } else {
                        //已完成订单
                        ((TextView) view).setTextColor(getResources().getColor(R.color.order_list_name_finish_color));
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });
        Log.i(TAG,"adapter bind");

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                //把蓝色去掉
//                listView.getChildAt(position).setBackgroundColor(Color.WHITE);
//
//                Log.i(TAG,position+"");
//
//                cursor.moveToPosition(position);
//                String orderNo = cursor.getString(0);
//                String logistic = cursor.getString(8);
//                Intent intent = new Intent(getActivity(), OlderInfoActivity.class);
//                intent.putExtra("orderNo", orderNo);
//                intent.putExtra("logistic", logistic);
//                startActivity(intent);
//            }
//        });

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_search_sfbest,container,false);
        Log.i(TAG,"oncreatView");
        listView = (ListView) view.findViewById(R.id.fgs_sfbest_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //把蓝色去掉
                listView.getChildAt(position).setBackgroundColor(Color.WHITE);

                cursor.moveToPosition(position);
                String orderNo = cursor.getString(0);
                String logistic = cursor.getString(8);
                Intent intent = new Intent(getContext(), OlderInfoActivity.class);
                intent.putExtra("orderNo", orderNo);
                intent.putExtra("logistic", logistic);

                Log.i(TAG,orderNo+logistic);
                startActivity(intent);
            }
        });

        fab1 = (FloatingActionButton) view.findViewById(R.id.search_fab_1);
        fab2 = (FloatingActionButton) view.findViewById(R.id.search_fab_2);
        fab3 = (FloatingActionButton) view.findViewById(R.id.search_fab_3);
        //查名字
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogGetInfo("搜索姓名", R.drawable.user,2);
                //searchInfo(2, searchEditText.getText().toString());
//                String name=searchEditText.getText().toString();
//                cursor.moveToFirst();
//                while (cursor.moveToNext()){
//                    if(name==cursor.getString(2)){
//                        listView.setSelection(cursor.getPosition());
//                        adapter.notifyDataSetChanged();
//                        break;
//                    }
//                }
//                //没有找到
//                if(!cursor.moveToNext()){
//                    Tools.showDialog(SearchActivity.this,
//                            getWindowManager(),
//                            "没有找到该数据！",
//                            null,
//                            null,
//                            "确定",
//                            null,
//                            null,
//                            null
//                            );
//                }
            }
        });

        //查手机号
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogGetInfo("搜索电话", R.drawable.tel,3);
                //searchInfo(3, searchEditText.getText().toString());
//                String tel=searchEditText.getText().toString();
//                cursor.moveToFirst();
//                while (cursor.moveToNext()){
//                    if(tel==cursor.getString(3)){
//                        listView.setSelection(cursor.getPosition());
//                        adapter.notifyDataSetChanged();
//                        break;
//                    }
//                }
//                //没有找到
//                if(!cursor.moveToNext()){
//                    Tools.showDialog(SearchActivity.this,
//                            getWindowManager(),
//                            "没有找到该数据！",
//                            null,
//                            null,
//                            "确定",
//                            null,
//                            null,
//                            null
//                    );
//                }
            }
        });

        //查快递单号
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogGetInfo("搜索订单编号", R.drawable.post,8);
                //searchInfo(8, searchEditText.getText().toString());
//                String postno=searchEditText.getText().toString();
            }
        });
        return view;
    }


    /**
     * 弹出消息框，输入搜索内容
     *
     * @param title  消息框title
     * @param iconID 消息框图标
     */
    private void dialogGetInfo(String title, int iconID, final int infoPosition) {
        final int infoId=infoPosition;
        //获取查找信息
        searchEditText = new EditText(getContext());
        searchEditText.setTextColor(getResources().getColor(R.color.black_semi_transparent));
        new AlertDialog.Builder(getContext()).setTitle(title)
                .setIcon(iconID)
                .setView(searchEditText)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String input = searchEditText.getText().toString();
                        if ("".equals(input)) {
                            Toast.makeText(getActivity(), "搜索内容不能为空！", Toast.LENGTH_SHORT).show();
                        }else{
                            //查找
                            if(null!=cursor){
                                if(cursor.moveToFirst()){

                                    Log.i(TAG,searchEditText.getText().toString());

                                    while (cursor.moveToNext()) {

                                        Log.i(TAG,cursor.getString(infoId));

                                        if (searchEditText.getText().toString().equals( cursor.getString(infoId))) {

                                            Log.i(TAG,"position"+cursor.getPosition());

                                            listView.smoothScrollByOffset(cursor.getPosition());//滚动到指定位置
                                            listView.getChildAt(cursor.getPosition()).setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                                            break;
                                        }
                                    }
                                }
                            }


                            //没有找到
                            if (!cursor.moveToNext()) {
                                Tools.showDialog(getActivity(),
                                        getActivity().getWindowManager(),
                                        "没有找到该数据！",
                                        null,
                                        null,
                                        "确定",
                                        null,
                                        null,
                                        null
                                );
                            }
                        }
                    }
                })
                .setNegativeButton("取消", null).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        cursor.close();
        dbTool.close();
    }
}
