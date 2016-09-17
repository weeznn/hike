package com.nuc.hikeplus;

import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class OlderInfoActivity extends AppCompatActivity {
    static final private String TAG = "OlderInfoActivity";

    private DBTool dbTool = null;
    private Cursor cursorOrder = null;
    private Cursor cursorGoods = null;

    private JSONutils jsoNutils = null;

    private String logistic = null;
    private String orderNo = null;
    private String goodsName = null;
    private TextView nameView = null;
    private TextView telView = null;
    private TextView addressView = null;
    private TextView orderNoView = null;
    private ListView listView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "oncreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_older_info);


        nameView = (TextView) findViewById(R.id.name);
        telView = (TextView) findViewById(R.id.tel);
        addressView = (TextView) findViewById(R.id.address);
        orderNoView = (TextView) findViewById(R.id.order_no);
        listView = (ListView) findViewById(R.id.orderList);

        orderNo = getIntent().getStringExtra("orderNo");
        logistic = getIntent().getStringExtra("logistic");
        //数据库工具
        dbTool = new DBTool(this);
        //悬浮按钮
        init();
        //物流追踪
        track();

    }

    private void init() {
        //订单取消按钮
        findViewById(R.id.olderInfo_fab_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tools.showDialog(OlderInfoActivity.this,
                        getWindowManager(),
                        "确定取消该订单？",
                        null,
                        null,
                        "确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dbTool.deleteItem(orderNo);
                                dbTool.close();
                            }
                        }, "取消", null);
            }
        });


        //订单交易成功按钮
        findViewById(R.id.olderInfo_fab_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Tools.showDialog(OlderInfoActivity.this,
                        getWindowManager(),
                        "确认用户已拿到货物？",
                        null,
                        null,
                        "确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dbTool.updataOlderTable(orderNo);
                                dbTool.close();
                            }
                        }, "取消", null);
            }
        });

        // 搜索详细信息
        cursorOrder = dbTool.searchDBFromLogistic(logistic);
        if (cursorOrder != null) {
            if (cursorOrder.moveToFirst()) {
                orderNoView.setText("  " + "订单编号：" + cursorOrder.getString(0));
                nameView.setText(" " + "姓 名：" + cursorOrder.getString(2));
                telView.setText("  " + "联系方式：" + cursorOrder.getString(3));
                addressView.setText("  " + "收货地址：" + cursorOrder.getString(4) + cursorOrder.getString(5) + cursorOrder.getString(6));
                goodsName = cursorOrder.getString(7);

                Log.i(TAG, orderNo + "  " + cursorOrder.getString(3) + "  " + cursorOrder.getString(4) + "  " + cursorOrder.getString(5) + cursorOrder.getString(6) + cursorOrder.getString(7) + " " + cursorOrder.getString(8));

                cursorGoods = dbTool.searchGoodsFromName(goodsName);
                if (cursorGoods != null) {
                    if (cursorGoods.moveToFirst()) {
                        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.list_goods, cursorGoods,
                                new String[]{"goodsName", "info", "goodsPrice", "image"}, new int[]{R.id.goods_name, R.id.goods_prace, R.id.goods_image});

                        //试图绑定图片
                        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
                            @Override
                            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                                if (view instanceof ImageView) {
                                    if (cursor.moveToFirst()) {
                                        byte[] image_blob = cursor.getBlob(8);
                                        Bitmap image_bitmap = BitmapFactory.decodeByteArray(image_blob, 0, image_blob.length);
                                        ((ImageView) view).setImageBitmap(image_bitmap);
                                    }
                                    return true;
                                } else if (R.id.name == view.getId()) {
                                    ((TextView) view).setText(cursor.getString(0) + "       " + cursor.getString(9));
                                    return true;
                                } else {
                                    return false;
                                }
                            }
                        });
                        listView.setAdapter(adapter);
                    }
                } else {
                    Toast.makeText(this, "数据查询失败！", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    //物流追踪
    private void track() {
        jsoNutils = new JSONutils(orderNo, logistic);
        final Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bundle resoult = msg.getData();

                Log.i(TAG, "ui get " + resoult.toString());

                if (resoult.getBoolean("isSuccess")) {
                    TextView textView = new TextView(OlderInfoActivity.this);
                    textView.setPadding(10, 30, 0, 20);
                    textView.setText("物流追踪");
                    textView.setTextSize(20);

                    LinearLayout linearLayout = (LinearLayout) findViewById(R.id.content_orderinfo_linear_layout);
                    linearLayout.addView(textView);
                    for (int i = 0; i < resoult.getInt("traceSize"); i++) {
                        TextView text = new TextView(OlderInfoActivity.this);
//                        text.setTextSize(getResources().getDimension(R.dimen.second_class_title));
                        //该string[]为{时间，地点，remark}
                        text.setText(resoult.getStringArray("str" + i)[0].toString() + "\n" + resoult.getStringArray("str" + i)[1].toString() + "\n" + resoult.getStringArray("str" + i)[2].toString());
                        linearLayout.addView(text);
                    }
                } else {
                    TextView textView = new TextView(OlderInfoActivity.this);
                    textView.setText("物流信息暂无");
                    textView.setTextSize(getResources().getDimension(R.dimen.first_class_title));
                    textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
                    LinearLayout linearLayout = (LinearLayout) findViewById(R.id.content_orderinfo_linear_layout);
                    linearLayout.addView(textView);
                }
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "new  thread");
                try {
                    Bundle resoult = jsoNutils.jsonSearch();

                    Log.i(TAG, "thread get " + resoult.toString());

                    Message message = mHandler.obtainMessage();
                    message.setData(resoult);
                    mHandler.sendMessage(message);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        cursorGoods.close();
        cursorOrder.close();
        dbTool.close();
    }
}
