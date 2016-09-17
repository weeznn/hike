package layout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.nuc.hikeplus.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClockFragment extends Fragment implements AMapLocationListener {
    private static final String TAG = "ClockFragment";
    private Context context;
    //高德地图部分
    public AMapLocationClient aMapLocationClient = null;
    StringBuilder loaRes = new StringBuilder();
    private Double loaLatitude = 0d, loaLongitude = 0d;
    private String dateAndTime = null;

    //bing壁纸部分
    private static String bingImagePath = null;
    private static String bingImageTitle = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();


        //        StringBuilder loaRes = new StringBuilder();
        //
        //        if (bundle.getBoolean("islocation")) {
        //            loaRes.append("您3当前的位置是：");
        //            loaRes.append(bundle.getString("loaCity"));
        //            loaRes.append(bundle.getString("loaDistrict"));
        //            loaRes.append(bundle.getString("loaStreet"));
        //            loaRes.append(bundle.getString("loaStreetNum"));
        //            loaLatitude = bundle.getDouble("loaLatitude");
        //            loaLongitude = bundle.getDouble("loaLongitude");
        //
        //        } else {
        //            loaRes.append("定位失败，错误代码：");
        //            loaRes.append(bundle.getString("errCode") + "。 ");
        //            loaRes.append("错误信息：");
        //            loaRes.append(bundle.getString("errInfo") + "。");
        //        }
        //
        //        address = loaRes.toString();
        //        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //        Date date = new Date();
        //        dateAndTime = dateFormat.format(date);


        //bing
        Bundle image = getArguments();
        if (null != image) {
            bingImagePath = image.getString("imagePath");
            bingImageTitle = image.getString("imageTitle");

            Log.i(TAG, bingImageTitle);
            Log.i(TAG, bingImagePath);

        }

        //        imagebitmap= BitmapFactory.decodeByteArray(image.getByteArray("image"),0,image.getByteArray("image").length);


        //        String imagename=image.getString("name");
        //        String imagepath=image.getString("path");
        //        imagebitmap= BitmapFactory.decodeFile(imagepath+imagename+".PNG");
        //        try {
        //            File file=new File(imagepath+imagename+".PNG");
        //            FileInputStream fis=new FileInputStream(file);
        //            imagebitmap=BitmapFactory.decodeStream(fis);
        //        } catch (FileNotFoundException e) {
        //            e.printStackTrace();
        //        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_clock, container, false);

        final ImageButton ibt = (ImageButton) view.findViewById(R.id.fgc_imagebtn);

        //bingWallPaper
        final Handler mhander = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                Bitmap bitmap = BitmapFactory.decodeByteArray(bundle.getByteArray("imageByte"), 0, bundle.getByteArray("imageByte").length);
                ibt.setImageBitmap(bitmap);
            }
        };

        //location
        aMapLocationClient = new AMapLocationClient(getContext());
        aMapLocationClient.setLocationOption(loacation());
        aMapLocationClient.setLocationListener(this);
        aMapLocationClient.startLocation();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(bingImagePath);
                    InputStream is = url.openStream();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    int len = 0;
                    byte[] bytes = new byte[1024];
                    while ((len = is.read(bytes)) != -1) {
                        bos.write(bytes, 0, len);
                    }
                    bos.flush();
                    Bundle bundle = new Bundle();
                    bundle.putByteArray("imageByte", bos.toByteArray());
                    is.close();
                    bos.close();
                    Message message = new Message();
                    message.setData(bundle);
                    mhander.sendMessage(message);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

//        //location
//        aMapLocationClient = new AMapLocationClient(getContext());
//        aMapLocationClient.setLocationOption(loacation());
//        aMapLocationClient.setLocationListener(this);
//        aMapLocationClient.startLocation();

        TextView title = (TextView) view.findViewById(R.id.imageinfotextView);
        title.setText(bingImageTitle);
        TextView textView = (TextView) view.findViewById(R.id.text);
        textView.setText("时间：" + dateAndTime + "\n地点：" + loaRes.toString() + "\n经度：" + loaLatitude + "维度：" + loaLongitude);


        //点击打卡判断
        ibt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(context)
                        .setMessage("今天上班加油")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //结束本fragment
                                FragmentManager fg = getFragmentManager();
                                fg.popBackStack();
                            }
                        }).create();
                Window window = alertDialog.getWindow();
                window.setGravity(Gravity.BOTTOM | Gravity.LEFT);
                alertDialog.show();

            }
        });

        return view;
    }

    /**
     * 定位服务
     *
     * @return
     */
    private AMapLocationClientOption loacation() {

        Log.i(TAG, "location");

        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(true);

        if (mLocationOption.isOnceLocationLatest()) {
            mLocationOption.setOnceLocationLatest(true);
            //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。
            //如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会。
        }

        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        return mLocationOption;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {

        Log.i(TAG, "locationchange");

        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                //                        aMapLocation.getCityCode();//城市编码
                //                        aMapLocation.getAdCode();//地区编码
                //                        aMapLocation.getAoiName();//获取当前定位点的AOI信息
                //                bundle.putBoolean("islocation", true);
                //                bundle.putString("loaCity", aMapLocation.getCity());
                //                bundle.putString("loaDistrict", aMapLocation.getDistrict());
                //                bundle.putString("loaStreet", aMapLocation.getStreet());
                //                bundle.putString("loaStreetNum", aMapLocation.getStreetNum());
                //                bundle.putDouble("loaLatitude", aMapLocation.getLatitude());
                //                bundle.putDouble("loaLongitude", aMapLocation.getLongitude());
                loaRes.append(aMapLocation.getCity());
                loaRes.append(aMapLocation.getDistrict());
                loaRes.append(aMapLocation.getStreet());
                loaRes.append(aMapLocation.getStreetNum());
                loaLongitude = aMapLocation.getLongitude();
                loaLatitude = aMapLocation.getLatitude();

                Log.i(TAG, loaRes.toString() + "  " + loaLatitude + "  " + loaLongitude);

            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                //                bundle.putBoolean("isLocation", false);
                //                bundle.putInt("errCode", aMapLocation.getErrorCode());
                //                bundle.putString("errInfo", aMapLocation.getErrorInfo());
                loaRes.append("定位失败");
                loaRes.append("错误代码：" + aMapLocation.getErrorCode());
                loaRes.append("错误信息" + aMapLocation.getErrorInfo());

                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date(aMapLocation.getTime());
            dateAndTime = dateFormat.format(date);

            Log.i(TAG, dateAndTime);
        }
    }

    /**
     * 读取bing壁纸
     *
     * @param Path
     * @return
     */
    private Bitmap readBingImage(String Path) {

        URL imageUrl = null;
        final Bitmap[] image = {null};
        try {
            imageUrl = new URL(Path);
        } catch (MalformedURLException e) {
            Log.i(TAG, "no url");

            e.printStackTrace();
        }

        final URL finalImagrUrl = imageUrl;

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "run");
                try {
                    HttpURLConnection connection = (HttpURLConnection) finalImagrUrl.openConnection();
                    connection.setDoOutput(true);
                    connection.connect();

                    InputStream ins = connection.getInputStream();
                    image[0] = BitmapFactory.decodeStream(ins);
                    ins.close();

                    Log.i(TAG, image[0].toString());

                } catch (IOException e) {

                    Log.i(TAG, "http connection");
                    e.printStackTrace();
                }


            }
        }).start();

        //        File file=new File(Path);
        //        byte[] image=new byte[]{};
        //        try {
        //            FileInputStream fis=new FileInputStream(file);
        //            byte[] bytes=new byte[1024];
        //            ByteArrayOutputStream bos=new ByteArrayOutputStream();
        //            int len=0;
        //            while ((len=fis.read(bytes))!=-1){
        //                bos.write(bytes);
        //            }
        //            bos.flush();
        //            image=bos.toByteArray();
        //            bos.close();
        //            fis.close();
        //        } catch (FileNotFoundException e) {
        //
        //            Log.i(TAG,"cant find image");
        //            e.printStackTrace();
        //        } catch (IOException e) {
        //            Log.i(TAG,"cant read image");
        //            e.printStackTrace();
        //        }
        return image[0];
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        aMapLocationClient.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        aMapLocationClient.stopLocation();

    }

    //保存
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (null != bingImageTitle) {
            outState.putString("imageTitle", bingImageTitle);
        }
        if (null != bingImagePath) {
            outState.putString("imagePath", bingImagePath);
        }
    }

    //恢复
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (null != savedInstanceState) {
            bingImagePath = savedInstanceState.getString("imagePath");
            bingImageTitle = savedInstanceState.getString("imageTitle");
        }
    }

//    /**
//     * 计算定位位置与目的位置的距离
//     * @param la
//     * @param lo
//     * @param here
//     * @return
//     */
//    private boolean ishere(Double la,Double lo,String here){
//        boolean inHere=false;
//
//        //经纬度转换
//        int[]lalist=new int[3];
//        int[]lolist=new int[3];
//
//        lalist[0]= (int) (la/1);
//        Double a= la/1;
//        lalist[1]= (int) (a*60/1);
//        Double b=(a*60)%1;
//        lalist[2]= (int) (b*60/1);
//
//        Log.i(TAG,"j"+lalist[0]+"  "+lalist[1]+"  "+lalist[2]);
//
//        lolist[0]= (int) (lo/1);
//        Double c= lo/1;
//        lolist[1]= (int) (c*60/1);
//        Double d=(c*60)%1;
//        lolist[2]= (int) (d*60/1);
//
//        Log.i(TAG,"w"+lolist[0]+"  "+lolist[1]+"  "+lolist[2]);
//
//
//
//        return inHere;
//    }
}


