package com.nuc.hikeplus;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import layout.LoadingPerFragment;
import layout.LoadingSFbestFragment;

public class LoadingActivity extends AppCompatActivity {
    private static final String TAG = "LoadingActivity";
    private static boolean isFirst=true;

    private ImageView imageView = null;
    private Button btn_per=null;
    private Button btn_sfbest=null;
    private RelativeLayout relativeLayout=null;

    private static final boolean[] isper={false};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);


        imageView = (ImageView) findViewById(R.id.welcomeImage);
        final AlphaAnimation alphaAnimation = new AlphaAnimation(0.7f,1.0f );
        imageView.startAnimation(alphaAnimation);
        alphaAnimation.setDuration(5 * 1000);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                imageView.setBackgroundResource(R.drawable.ic_launch);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.i(TAG, "end");
                if(isFirst){
                    initLoadingView();
                }else{

                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initLoadingView(){


        relativeLayout= (RelativeLayout) findViewById(R.id.loading_relativeLayout);
        btn_per= (Button) findViewById(R.id.loading_btn_per);
        btn_sfbest= (Button) findViewById(R.id.loading_btn_sfbest);

        relativeLayout.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.GONE);

        FragmentManager manager=getSupportFragmentManager();
        final FragmentTransaction transaction=manager.beginTransaction();
        btn_per.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFirst=false;
                isper[0]=true;
                Log.i(TAG,"per"+isper[0]);
                transaction.replace(R.id.loading_frameLayout,new LoadingPerFragment());
                transaction.commit();
            }
        });

        btn_sfbest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFirst=false;
                isper[0]=false;
                Log.i(TAG,"sfbest"+isFirst+isper[0]);
                transaction.replace(R.id.loading_frameLayout,new LoadingSFbestFragment());
                transaction.commit();
            }
        });
    }


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
//        bingWallPaperurl = bingWallPaperurl.replace("1920x1080", "1080x1920");
//        Log.i(TAG, "resized" + bingWallPaperurl);
//        //从链接加载图片
//        try {
//            URL url = new URL(bingWallPaperurl);
//
//            Log.i(TAG, bingWallPaperurl);
//            Log.i(TAG, bingWallPaperTitle);
//
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("GET");
//            conn.setReadTimeout(3 * 1000);
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
//            //以上

            //下载图片
//            Bitmap bitmap= BitmapFactory.decodeByteArray(bundle.getByteArray("image"),0,bundle.getByteArray("image").length);
//            String path="/sdcard/hikeplus";
//            File file=new File(path,bingWallPaperTitle);
//            if(file.exists()){
//                file.delete();
//            }
//            FileOutputStream fos=new FileOutputStream(file);
//            bitmap.compress(Bitmap.CompressFormat.PNG,90,fos);
//            fos.flush();
//            fos.close();
//            bundle.putString("path",path);
//            bundle.putString("name",bingWallPaperTitle);

            //以上

//            bouts.flush();
//            bundle.putString("name"+".jpg",bingWallPaperName);
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
}






