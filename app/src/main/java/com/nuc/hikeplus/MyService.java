//package com.nuc.hikeplus;
//
//import android.app.Service;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.Binder;
//import android.os.Bundle;
//import android.os.IBinder;
//import android.util.Log;
//
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//
//import java.io.BufferedReader;
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.util.Date;
//
//public class MyService extends Service {
//    private static final String TAG="MyService";
//
//    private MyBinder myBinder=null;
//
//    @Override
//    public void onCreate() {
//        Log.i(TAG,"onCreate");
//        super.onCreate();
//        myBinder=new MyBinder();
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.i(TAG,"start");
//
//
//        return super.onStartCommand(intent, flags, startId);
//    }
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        Log.i(TAG,"bind");
//        return myBinder;
//    }
//
//    @Override
//    public boolean onUnbind(Intent intent) {
//        Log.i(TAG,"unbind");
//        return super.onUnbind(intent);
//    }
//}
//
//class MyBinder extends Binder{
//    private static final String TAG="BingWallPeperbinder";
//    private String path=null;
//    private String title=null;
//    private boolean dowloading=false;
//    public void onStart(){
//        Log.i(TAG,"bindstart");
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//
//                    Bundle bundle=getBingWallPaper(getBingWallPaperInfo());
//                    path="/sdcard/hikeplus/"+bundle.getString("name")+".jpg";
//
//                    Log.i(TAG,path);
//
//                    title=bundle.getString("title");
//                    File file=new File(path);
//                    Bitmap bitmap= BitmapFactory.decodeByteArray(bundle.getByteArray("image"),0,bundle.getByteArray("image").length);
//
//                    if(!file.exists()){
//                        file.delete();
//                    }
//                    FileOutputStream fos=new FileOutputStream(file);
//                    bitmap.compress(Bitmap.CompressFormat.JPEG,70,fos);
//                    fos.flush();
//                    fos.close();
//                    dowloading=true;
//                    Log.i(TAG,"save image");
//
//                } catch (IOException e) {
//                    Log.i(TAG,"save image error");
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//
////        Bundle bundle=new Bundle();
////        bundle.putString("path",path);
////        bundle.putString("title",title);
////        return bundle;
//
//    }
//
//    public String getPath() {
//        return path;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public boolean isDowloading() {
//        return dowloading;
//    }

//    /**
//     * 获取bing壁纸的相关信息
//     *
//     * @return
//     */
//    private bingWallPeper getBingWallPaperInfo() throws IOException {
//        Date date=new Date();
//        Log.i(TAG,"开始"+date.toString());
//        BufferedReader in = null;
//        StringBuilder resoult = new StringBuilder();
//        try {
//            String bingJsonRequst = "http://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1";
//
//            Log.i(TAG, bingJsonRequst);
//
//            URL url = new URL(bingJsonRequst);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("GET");//请求方式
//            conn.setReadTimeout(5 * 1000);//超时时间
//            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
//            String line;
//            while (null != (line = in.readLine())) {
//                resoult.append(line);
//            }
//
//            in.close();
//        } catch (MalformedURLException e) {
//
//            Log.e(TAG, "连接出错");
//            e.printStackTrace();
//        } catch (IOException e) {
//            if (null != in) {
//                in.close();
//            }
//            Log.i(TAG, "readcucuo ");
//            e.printStackTrace();
//        }
//        Gson gson = new GsonBuilder().create();
//
//        Log.i(TAG, resoult.toString());
//
//        bingWallPeper bing = gson.fromJson(resoult.toString(), bingWallPeper.class);
//
//        return bing;
//    }
//
//    /**
//     * 下载bing壁纸
//     *
//     * @param bingWallPeperInfo
//     * @return
//     */
//    private Bundle getBingWallPaper(bingWallPeper bingWallPeperInfo) {
//        String bingWallPaperurl = bingWallPeperInfo.images.get(0).url;
//        String bingWallPaperTitle = bingWallPeperInfo.images.get(0).copyright;
//        String bingWallPaperName=bingWallPeperInfo.images.get(0).enddate;
//        Bundle bundle = new Bundle();
//        int i = bingWallPaperurl.lastIndexOf("_");
//        bingWallPaperurl = bingWallPaperurl.replace("1920x1080", "1080x1920");
//        Log.i(TAG, "resized" + bingWallPaperurl);
//        try {
//            URL url = new URL(bingWallPaperurl);
//
//            Log.i(TAG, bingWallPaperurl);
//            Log.i(TAG, bingWallPaperTitle);
//
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("GET");
//            conn.setReadTimeout(5 * 1000);
//            conn.setDoOutput(true);
//            conn.setDoInput(true);
//
//            Log.i(TAG,"请求发送中");
//
//            InputStream in = conn.getInputStream();
//
//            Log.i(TAG,"返回");
//
//            byte[] data = new byte[1024];
//            int len = 0;
//            ByteArrayOutputStream bouts = new ByteArrayOutputStream();
//            while ((len = in.read(data)) != -1) {
//                bouts.write(data, 0, len);
//            }
////            Bitmap bitmap= BitmapFactory.decodeByteArray(bundle.getByteArray("image"),0,bundle.getByteArray("image").length);
////            String path="/sdcard/hikeplus";
////            File file=new File(path,bingWallPaperTitle);
////            if(file.exists()){
////                file.delete();
////            }
////            FileOutputStream fos=new FileOutputStream(file);
////            bitmap.compress(Bitmap.CompressFormat.PNG,90,fos);
////            fos.flush();
////            fos.close();
////            bundle.putString("path",path);
////            bundle.putString("name",bingWallPaperTitle);
//            bouts.flush();
//            bundle.putString("name",bingWallPaperName);
//            bundle.putByteArray("image", bouts.toByteArray());
//            bundle.putString("title", bingWallPaperTitle);
//            in.close();
//            bouts.close();
//
//        } catch (MalformedURLException e) {
//            Log.i(TAG, "newurl");
//            e.printStackTrace();
//        } catch (IOException e) {
//            Log.i(TAG, "io2");
//            e.printStackTrace();
//
//        }
//
//        Date date=new Date();
//        Log.i(TAG,"结束"+date.toString());
//
//        return bundle;
//    }
//}

////json 类
//class bingWallPeper extends Binder{
//
//    public List<image> images;
//    public com.nuc.hikeplus.tooltips tooltips;
//}
//
//class image {
//    public String startdate;
//    public String fullstartdate;
//    public String enddate;
//    public String url;
//    public String urlbase;
//    public String copyright;
//    public String copyrightlink;
//    public boolean wp;
//    public String hsh;
//    public int drk;
//    public int top;
//    public int bot;
//    public List<String> hs;
//}
//
//class tooltips {
//    public String loading;
//    public String previous;
//    public String next;
//    public String walle;
//    public String walls;
//}
