package com.nuc.hikeplus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;

import layout.SearchPerFragment;
import layout.SearchSFBestFragment;

/**
 * 查询订单
 */
public class SearchActivity extends AppCompatActivity {
    private final static String TAG = "SearchActivity";

//    private FloatingActionButton fab1 = null, fab2 = null, fab3 = null;
//    private ListView listView = null;
//    private EditText searchEditText = null;
//    private SimpleCursorAdapter adapter = null;
//    private DBTool dbTool = null;
//    private Cursor cursor = null;

    private FrameLayout layout=null;
    private boolean isPer=false;
    private SearchPerFragment perFragment=null;
    private SearchSFBestFragment sfBestFragment=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "oncreat");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent intent=getIntent();
        isPer=intent.getBooleanExtra("isPer",false);

        Log.i(TAG,isPer+"    得到");

        layout= (FrameLayout) findViewById(R.id.search_frameLayout);

        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();

        if(isPer){
            //geren
            perFragment=new SearchPerFragment();
            transaction.add(R.id.search_frameLayout,perFragment);
            transaction.commit();
        }else{
            //sf
            sfBestFragment=new SearchSFBestFragment();
            transaction.add(R.id.search_frameLayout,sfBestFragment);
            transaction.commit();
        }
//        Toolbar toolbar = (Toolbar) findViewById(R.id.search_toolbar);
//        setSupportActionBar(toolbar);

//        dbTool = new DBTool(this);
//        cursor = dbTool.searchDB();
//        Log.i(TAG, "view");
//
//        adapter = new SimpleCursorAdapter(this, R.layout.list_item, cursor,
//                new String[]{"receiverName", "receiverTel", "olderNo"},
//                new int[]{R.id.name, R.id.tel, R.id.logistic});
//
//        //根据订单状态设置name颜色
//        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
//            @Override
//            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
//                if (view instanceof TextView && R.id.name == view.getId()) {
//                    ((TextView) view).setText(cursor.getString(2));
//                    if (0 != cursor.getInt(10)) {
//                        //未完成订单
//                        TextPaint tp = ((TextView) view).getPaint();
//                        tp.setFakeBoldText(true);
//                        ((TextView) view).setTextColor(getResources().getColor(R.color.order_list_name_ing_color));
//                    } else {
//                        //已完成订单
//                        ((TextView) view).setTextColor(getResources().getColor(R.color.order_list_name_finish_color));
//                    }
//                    return true;
//                } else {
//                    return false;
//                }
//            }
//        });
//
//        init();
//
//        //填充listview  设置item单击listener
//        listView = (ListView) findViewById(R.id.list);
//        listView.setAdapter(adapter);
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                //把蓝色去掉
//                listView.getChildAt(position).setBackgroundColor(Color.WHITE);
//
//                cursor.moveToPosition(position);
//                String orderNo = cursor.getString(0);
//                String logistic = cursor.getString(8);
//                Intent intent = new Intent(SearchActivity.this, OlderInfoActivity.class);
//                intent.putExtra("orderNo", orderNo);
//                intent.putExtra("logistic", logistic);
//                startActivity(intent);
//            }
//        });

    }

//    private void init() {
//        fab1 = (FloatingActionButton) findViewById(R.id.search_fab_1);
//        fab2 = (FloatingActionButton) findViewById(R.id.search_fab_2);
//        fab3 = (FloatingActionButton) findViewById(R.id.search_fab_3);
//        //查名字
//        fab1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialogGetInfo("搜索姓名", R.drawable.user,2);
//                //searchInfo(2, searchEditText.getText().toString());
////                String name=searchEditText.getText().toString();
////                cursor.moveToFirst();
////                while (cursor.moveToNext()){
////                    if(name==cursor.getString(2)){
////                        listView.setSelection(cursor.getPosition());
////                        adapter.notifyDataSetChanged();
////                        break;
////                    }
////                }
////                //没有找到
////                if(!cursor.moveToNext()){
////                    Tools.showDialog(SearchActivity.this,
////                            getWindowManager(),
////                            "没有找到该数据！",
////                            null,
////                            null,
////                            "确定",
////                            null,
////                            null,
////                            null
////                            );
////                }
//            }
//        });
//
//        //查手机号
//        fab2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialogGetInfo("搜索电话", R.drawable.tel,3);
//                //searchInfo(3, searchEditText.getText().toString());
////                String tel=searchEditText.getText().toString();
////                cursor.moveToFirst();
////                while (cursor.moveToNext()){
////                    if(tel==cursor.getString(3)){
////                        listView.setSelection(cursor.getPosition());
////                        adapter.notifyDataSetChanged();
////                        break;
////                    }
////                }
////                //没有找到
////                if(!cursor.moveToNext()){
////                    Tools.showDialog(SearchActivity.this,
////                            getWindowManager(),
////                            "没有找到该数据！",
////                            null,
////                            null,
////                            "确定",
////                            null,
////                            null,
////                            null
////                    );
////                }
//            }
//        });
//
//        //查快递单号
//        fab3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialogGetInfo("搜索订单编号", R.drawable.post,8);
//                //searchInfo(8, searchEditText.getText().toString());
////                String postno=searchEditText.getText().toString();
//            }
//        });
//    }


//    /**
//     * 弹出消息框，输入搜索内容
//     *
//     * @param title  消息框title
//     * @param iconID 消息框图标
//     */
//    private void dialogGetInfo(String title, int iconID, final int infoPosition) {
//        final int infoId=infoPosition;
//        //获取查找信息
//        searchEditText = new EditText(SearchActivity.this);
//        searchEditText.setTextColor(getResources().getColor(R.color.black_semi_transparent));
//        new AlertDialog.Builder(SearchActivity.this).setTitle(title)
//                .setIcon(iconID)
//                .setView(searchEditText)
//                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        String input = searchEditText.getText().toString();
//                        if ("".equals(input)) {
//                            Toast.makeText(getApplicationContext(), "搜索内容不能为空！", Toast.LENGTH_SHORT).show();
//                        }else{
//                            //查找
//                            if(null!=cursor){
//                                if(cursor.moveToFirst()){
//
//                                    Log.i(TAG,searchEditText.getText().toString());
//
//                                    while (cursor.moveToNext()) {
//
//                                        Log.i(TAG,cursor.getString(infoId));
//
//                                        if (searchEditText.getText().toString().equals( cursor.getString(infoId))) {
//
//                                            Log.i(TAG,"position"+cursor.getPosition());
//
//                                            listView.smoothScrollByOffset(cursor.getPosition());//滚动到指定位置
//                                            listView.getChildAt(cursor.getPosition()).setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
//                                            break;
//                                        }
//                                    }
//                                }
//                            }
//
//
//                            //没有找到
//                            if (!cursor.moveToNext()) {
//                                Tools.showDialog(SearchActivity.this,
//                                        getWindowManager(),
//                                        "没有找到该数据！",
//                                        null,
//                                        null,
//                                        "确定",
//                                        null,
//                                        null,
//                                        null
//                                );
//                            }
//                        }
//                    }
//                })
//                .setNegativeButton("取消", null).show();
//    }
//
//
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//        cursor.close();
//        dbTool.close();
//    }
//

}
