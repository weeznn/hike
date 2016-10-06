package com.nuc.hikeplus;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.baofeng.mojing.unity.MojingActivity;
import com.unity3d.player.UnityPlayer;

import java.util.ArrayList;
import java.util.List;

public class HikeUnityActivity extends MojingActivity {
    private static final String TAG = "HikeUnityActivity";

    //用户信息
    private String receiverName = null;
    private String receiverTel = null;
    private String receiverHeight = null;
    private String userAddress = null;
    private String receiverProvince = null;
    private String receiverCity = null;
    private String receiverAddress = null;
    private UnityPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "oncreat  unityview");
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        receiverHeight = intent.getStringExtra("receiverHeight");
        receiverName = intent.getStringExtra("receiverName");
        receiverTel = intent.getStringExtra("receiverTel");
        receiverProvince = intent.getStringExtra("receiverProvince");
        receiverCity = intent.getStringExtra("receiverCity");
        receiverAddress = intent.getStringExtra("receiverAddress");
        player = getUnityPlayer();
        setContentView(player.getView());
        Log.i(TAG, "unitystart" +
                "haah");
    }

    //向unity发送数据
    public void userInfo() {
        Log.i(TAG, "userinfo");

        player.UnitySendMessage("MojingFirstCharacterController", "userInfo", receiverHeight);
    }

    //获取unity数据
    public void orderInfo(String goodsName) {
        Log.i(TAG, "orderinfo" + goodsName);
        //goodsName =getStringName(goodsName);
        goodsName=getStringName(goodsName);
        Log.i(TAG, goodsName);
        Intent intent = new Intent(HikeUnityActivity.this, OlderActivity.class);
        intent.putExtra("goodsName", goodsName);
        intent.putExtra("receiverName", receiverName);
        intent.putExtra("receiverTel", receiverTel);
        intent.putExtra("receiverProvince", receiverProvince);
        intent.putExtra("receiverCity", receiverCity);
        intent.putExtra("receiverAddress", receiverAddress);
        startActivity(intent);
        //finish();
    }

    //直接返回主页面
    public void info() {
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        player.pause();

    }

    /**
     * 去掉返回字符中的分割符
     * @param name
     * @return
     */
    private String getStringName(String name) {
        if(name.isEmpty()){
            return null;
        }else {
            List<String> stringList = new ArrayList<String>();
//            int i = -1,j=0;
//            do {
//                i = name.indexOf("|", j);
//                j=name.indexOf("|",i);
//                stringList.add(name.substring(i,name.length()));
//                j=i;
//            } while (0==i);
            int i=name.indexOf("#");
            stringList.add(name.substring(i+1,name.length()));

            Log.i(TAG,stringList.toString());
            Log.i(TAG,stringList.get(0));
            return stringList.get(0);
        }

    }

}
