package com.nuc.hikeplus;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * 下单
 * 可由unityactivity调用用于下单
 */
public class OlderActivity extends AppCompatActivity {
    private static final String TAG = "OlderActivity";
    private static int DBid = 0;
    //ui组件
    private FloatingActionButton fab1 = null, fab2 = null, fab3 = null;
    private TextView nameView = null;
    private TextView telView = null;
    private TextView addressView = null;
    private TextView orderNoView = null;
    private ListView listView = null;
    private TextView payMoney = null;
    private ProgressDialog progressDialog = null;
    private ImageView older_framlayout_image = null;
    private RelativeLayout older_relayout = null;
    //用户与产品信息
    private String receiverName = null;
    private String receiverTel = null;
    private String receiverProvince = null;
    private String receiverCity = null;
    private String receiverAddress = null;
    private String goodsName = null;
    private String orderNo = null;
    private String orderDate = null;
    private Boolean isPay = false;

    //货物信息
    private String goodsWeight = null;
    private String sendName = null;
    private String sendTel = null;
    private String sendProvince = null;
    private String sendCity = null;
    private String sendAddress = null;
    private String logistic = null;
    private float AllMoney = 0;


    //数据库
    private DBTool dbTool = null;
    private Cursor cursor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "oncreat");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_older);

        dbTool = new DBTool(this);


        //从unity获取联系人信息并settext
        Intent intent = getIntent();

        if (null != savedInstanceState) {
            receiverName = savedInstanceState.getString("name");
            receiverTel = savedInstanceState.getString("tel");
            receiverProvince = savedInstanceState.getString("province");
            receiverCity = savedInstanceState.getString("city");
            receiverAddress = savedInstanceState.getString("address");
            goodsName = savedInstanceState.getString("good");
            orderDate = savedInstanceState.getString("orderDate");
            orderNo = savedInstanceState.getString("orderNo");
        } else {
            receiverName = intent.getStringExtra("receiverName");
            receiverTel = intent.getStringExtra("receiverTel");
            receiverProvince = intent.getStringExtra("receiverProvince");
            receiverCity = intent.getStringExtra("receiverCity");
            receiverAddress = intent.getStringExtra("receiverAddress");
            goodsName = intent.getStringExtra("goodsName");
            orderNo = getordercode();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
            orderDate = simpleDateFormat.format(new Date(System.currentTimeMillis()));
        }

        nameView = (TextView) findViewById(R.id.name);
        nameView.setText("  " + "姓 名 ：" + receiverName);

        telView = (TextView) findViewById(R.id.tel);
        telView.setText("  " + "联系方式 ：" + receiverTel);

        addressView = (TextView) findViewById(R.id.address);
        addressView.setText("  " + "收货地址 ：" + receiverProvince + receiverCity + receiverAddress);

        payMoney = (TextView) findViewById(R.id.order_money_pay);

        //从数据库获取货物信息
        getGoodsInfoFromDb();

        //生成订单信息

        orderNoView = (TextView) findViewById(R.id.order_no);
        orderNoView.setText("  " + "订单编号：" + orderNo);

        init();
    }


    private void init() {
        Log.i(TAG, "init");
        fab1 = (FloatingActionButton) findViewById(R.id.older_fab_1);
        fab2 = (FloatingActionButton) findViewById(R.id.older_fab_2);
        fab3 = (FloatingActionButton) findViewById(R.id.older_fab_3);
        //        older_relayout = (RelativeLayout) findViewById(R.id.content_order_linner_layout);
        //        older_framlayout_image = (ImageView) findViewById(R.id.older_image);

        //支付宝支付
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView imageView = new ImageView(getApplicationContext());
                imageView.setImageResource(R.drawable.zhifubao);
                //dialog
                Tools.showDialog(OlderActivity.this,
                        getWindowManager(),
                        null,
                        "使用支付宝扫一扫",
                        imageView,
                        "确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                isPay = true;
                                addItem();

                            }
                        }, "取消", null);

            }
        });

        //微信支付
        fab2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ImageView view = new ImageView(getApplicationContext());
                                        view.setImageResource(R.drawable.weichar);

                                        Tools.showDialog(OlderActivity.this,
                                                getWindowManager(),
                                                null,
                                                "使用微信支付扫一扫",
                                                view,
                                                "确定",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        isPay = true;
                                                        addItem();


                                                    }
                                                }, "取消", null);
                                    }
                                }
        );

        //现金支付
        fab3.setOnClickListener(new View.OnClickListener()

                                {
                                    @Override
                                    public void onClick(View v) {
                                        final EditText view = new EditText(getApplicationContext());
                                        Tools.showDialog(OlderActivity.this,
                                                getWindowManager(),
                                                null,
                                                "请联系服务人员输入密码",
                                                view,
                                                "确定",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        isPay = true;
                                                        addItem();
                                                    }
                                                }, "取消", null);
                                    }
                                }

        );

    }

    /**
     * 增加olderTable中的条目
     */

    private void addItem() {
        //final Bundle[] bundle = {new Bundle()};

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                Bundle bundle = msg.getData();

                progressDialog.dismiss();
                //                older_relayout.setVisibility(View.VISIBLE);
                //                older_framlayout_image.setVisibility(View.GONE);


                Log.i(TAG, " message" + bundle.getString("success") + bundle.getString("reason") + bundle.getString("logistic") + "  ");


                if (bundle.getBoolean("success")) {

                    Log.i(TAG, "下单成功!快递单号为：" + bundle.getString("logistic"));


                    sendSMS(OlderActivity.this, receiverTel, bundle.getString("logistic"));

                    //                    Tools.showDialog(OlderActivity.this,
                    //                            getWindowManager(),
                    //                            "下单成功!快递单号为：" + bundle[0].getString("logistic"),
                    //                            null,
                    //                            null,
                    //                            "确定",
                    //                            null,
                    //                            null,
                    //                            null);
                    TextView view = new TextView(getApplicationContext());
                    view.setTextSize(20);
                    view.setTextColor(Color.BLACK);
                    view.setText("下单成功!快递单号为：" + bundle.getString("logistic"));

                    new AlertDialog.Builder(OlderActivity.this).setView(view).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dbTool.addItem(orderNo, orderDate, receiverName, receiverTel, receiverProvince, receiverCity, receiverAddress, goodsName, logistic, isPay, DBid++);
                            finish();
                        }
                    }).show();

                } else {
                    Log.i(TAG, "下单失败" + bundle.getString("reason") + "错误代码：");

                    //                    Tools.showDialog(OlderActivity.this,
                    //                            getWindowManager(),
                    //                            "下单失败!失败原因为：" + bundle[0].getString("reason"),
                    //                            null,
                    //                            null,
                    //                            "确定",
                    //                            null,
                    //                            null,
                    //                            null);
                    TextView view = new TextView(getApplicationContext());
                    view.setTextSize(20);
                    view.setTextColor(Color.RED);
                    view.setText("下单失败!失败原因为：" + bundle.getString("reason"));
                    new AlertDialog.Builder(OlderActivity.this).setView(view).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();
                }
            }
        };

        progressDialog = new ProgressDialog(OlderActivity.this, ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("正在下单。。。");
        ImageView imageView = new ImageView(OlderActivity.this);
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.box));
        progressDialog.setView(imageView);
        progressDialog.show();
        //        older_relayout.setVisibility(View.GONE);
        //        older_framlayout_image.setVisibility(View.VISIBLE);

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    JSONutils json = new JSONutils(orderNo, orderDate, receiverName, receiverTel, receiverProvince, receiverCity, receiverAddress,
                            goodsName, goodsWeight, sendName, sendTel, sendProvince, sendCity, sendAddress);
                    Bundle bundle = json.JsonOnlineOlderSend();

                    Log.i(TAG, "返回信息" + bundle.getBoolean("success") + bundle.getString("reason") + bundle.getString("logistic") + "  ");

                    Message message = handler.obtainMessage();
                    message.setData(bundle);
                    handler.sendMessage(message);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    /**
     * 生成订单号
     *
     * @return
     */
    private String getordercode() {
        Calendar c = Calendar.getInstance();
        String year = String.valueOf(c.get(Calendar.YEAR));
        String month = String.valueOf(c.get(Calendar.MONTH));
        String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));


        Random random = new Random();
        int x = random.nextInt(100) + 10;
        int y = random.nextInt(100) + 10;
        int z = random.nextInt(100) + 10;

        StringBuilder ordercode = new StringBuilder();
        ordercode.append(year);
        ordercode.append(month);
        ordercode.append(day);
        ordercode.append(x);
        ordercode.append(y);
        ordercode.append(z);

        Log.i(TAG, ordercode.toString());

        return ordercode.toString();
    }

    /**
     * 从数据库获取产品信息
     */
    private void getGoodsInfoFromDb() {
        goodsName.replace("|", "");

        Log.i(TAG, goodsName.toString());
        cursor = dbTool.searchGoodsFromName(goodsName);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                goodsWeight = cursor.getString(1);
                AllMoney += cursor.getFloat(2);
                sendName = cursor.getString(3);
                sendTel = cursor.getString(4);
                sendProvince = cursor.getString(5);
                sendCity = cursor.getString(6);
                sendAddress = cursor.getString(7);


                SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.list_goods, cursor,
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
                            ((TextView) view).setText(cursor.getString(0) + "          " + cursor.getString(9));
                            return true;
                        } else {
                            return false;
                        }
                    }
                });
                listView = (ListView) findViewById(R.id.orderList);
                listView.setAdapter(adapter);

                payMoney.setText("共计：￥" + AllMoney);

            }

        } else {
            Toast.makeText(this, "数据查询失败！", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
        dbTool.close();
    }


    /**
     * 发送短信
     *
     * @param context
     * @param tel
     * @param logis
     */
    private void sendSMS(Context context, String tel, String logis) {
        StringBuilder mes = new StringBuilder();
        mes.append("您好，您在嘿客馆的订单已提交，订单编号为 ");
        mes.append(logis);
        mes.append("请注意查收");

        Log.i(TAG, mes.toString());

        String SENT_SMS_ACTION = "SENT_SMS_ACTION";
        Intent sendIntent = new Intent(SENT_SMS_ACTION);
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT_SMS_ACTION),
                PendingIntent.FLAG_CANCEL_CURRENT);
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case HikeUnityActivity.RESULT_OK:
                        Toast.makeText(context, "短信发送成功！", Toast.LENGTH_SHORT);
                        break;
                    default:
                        Toast.makeText(context, "短信发送失败！", Toast.LENGTH_SHORT);
                        break;
                }
            }
        }, new IntentFilter(SENT_SMS_ACTION));
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(tel, null, mes.toString(), null, null);
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Log.i(TAG, "onsaveInstanceState");
        outState.putString("name", receiverName);
        outState.putString("tel", receiverTel);
        outState.putString("province", receiverProvince);
        outState.putString("city", receiverCity);
        outState.putString("address", receiverAddress);
        outState.putString("good", goodsName);
        outState.putString("orderNo", orderNo);
        outState.putString("orderData", orderDate);
    }


}
