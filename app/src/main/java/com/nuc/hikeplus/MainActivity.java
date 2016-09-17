package com.nuc.hikeplus;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;

import layout.AdviceFragment;
import layout.ClockFragment;
import layout.HelpFragment;
import layout.MainFragment;
import layout.NoticeFragment;
import layout.StorInfoFragment;
import layout.UpdataFragment;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";

    //建议与反馈
    private AdviceFragment adviceFragment = null;
    //今日打卡
    private ClockFragment clockFragment = null;
    //帮助
    private HelpFragment helpFragment = null;
    //商店
    private MainFragment mainFragment = null;
    //通知
    private NoticeFragment noticeFragment = null;
    //商店信息
    private StorInfoFragment storInfoFragment = null;
    //检查更新
    private UpdataFragment updataFragment = null;
    //为打卡传bing美图
    private Bundle clockImageInfo = null;
    private String imagePath = null;
    private String imageTitle = null;

    //是否是个人用户
    private boolean isPer = false;
    //侧边栏判断
    private Bundle storeOrPerInfoBundle=null;
    private Bundle mainBundle=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        Intent intent = getIntent();
        isPer=intent.getBooleanExtra("isPer",false);
        storeOrPerInfoBundle=getInfoFromLast(isPer,intent);


        //侧滑部分
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        if (savedInstanceState == null) {
            //设置默认fragment
            mainFragment = new MainFragment();
            mainFragment.setArguments(storeOrPerInfoBundle);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.main_activity_fragment, mainFragment, "main");
            transaction.commit();
        }
        //设置阴影
        drawer.setDrawerShadow(R.drawable.side_nav_bar, GravityCompat.START);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
//            @Override
//            public void onDrawerClosed(View drawerView) {
//                super.onDrawerClosed(drawerView);
//            }
//
//            @Override
//            public void onDrawerOpened(View drawerView) {
//                super.onDrawerOpened(drawerView);
//            }
//        };
//        drawer.setDrawerListener(toggle);
//
//        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if(isPer){
            navigationView.inflateMenu(R.menu.activity_setting_per);

        }else{
            navigationView.inflateMenu(R.menu.activity_setting_drawer);

            //获取壁纸信息
            clockImageInfo = new Bundle();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.i(TAG, "start");
                    try {
                        bingWallPeper bing = getBingWallPaperInfo();

                        imagePath = bing.images.get(0).url;
                        imagePath = imagePath.replace("1920x1080", "1080x1920");
                        imageTitle = bing.images.get(0).copyright;

                        Log.i(TAG, "imagePath     " + imagePath);
                        Log.i(TAG, "imageTitle   " + imageTitle);


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        navigationView.setNavigationItemSelectedListener(this);

//        Handler handler=new Handler(){
//            @Override
//            public void handleMessage(Message msg) {
//
//                clockImageInfo=msg.getData();
//            }
//        };

//        Intent intent=new Intent(MainActivity.this,MyService.class);

//        Log.i(TAG,"bind");
//        bindService(intent,serviceConnection,BIND_AUTO_CREATE);

        new Thread(new Runnable() {
            @Override
            public void run() {
                //检测数据库
                initDB();
            }
        }).start();


    }

    /**
     * 从上一个activity中获取信息
     * @param bool
     * @param intent
     * @return
     */
    private Bundle getInfoFromLast(boolean bool,Intent intent){
        Bundle bundle=new Bundle();
        if(bool){
            //per
            bundle.putBoolean("isPer",true);
            bundle.putString("name",intent.getStringExtra("name"));
            bundle.putString("tel",intent.getStringExtra("tel"));
            bundle.putString("address",intent.getStringExtra("address"));
        }else{
            //sfBest
            bundle.putBoolean("isPer",false);
            bundle.putString("storeNO",intent.getStringExtra("storeNo"));
        }

        Log.i(TAG,bundle+"   传值");
        return bundle;
    }

    /**
     * 检测数据库
     */
    private void initDB() {
        DBTool dbTool = new DBTool(this);
        try {
            dbTool.createDB();
        } catch (IOException e) {
            e.printStackTrace();
        }
        dbTool.close();
    }

    @Override
    protected void onPause() {

        super.onPause();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        //unbind Myservice
//        Intent intent=new Intent(MainActivity.this,MyService.class);
//        Log.i(TAG,"unbind");
//        unbindService(serviceConnection);
//        Log.i(TAG,"stop");
//        stopService(intent);

    }


    //侧滑菜单相应fragment的变换
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        switch (item.getItemId()) {
            case R.id.nav_storeInfo:
                if (null == storInfoFragment) {
                    storInfoFragment = new StorInfoFragment();
                }
                Log.i(TAG,storeOrPerInfoBundle+"");
                storInfoFragment.setArguments(storeOrPerInfoBundle);
                transaction.replace(R.id.main_activity_fragment, storInfoFragment, "storeInfo");
                transaction.addToBackStack(null);
                break;
            case R.id.nav_clock:
                if (null == clockFragment) {
                    clockFragment = new ClockFragment();
                }

                clockImageInfo.putString("imageTitle", imageTitle);
                clockImageInfo.putString("imagePath", imagePath);

                Log.i(TAG, clockImageInfo.toString());

                clockFragment.setArguments(clockImageInfo);
                transaction.replace(R.id.main_activity_fragment, clockFragment, "clock");
                transaction.addToBackStack(null);
                break;
            case R.id.nav_notice:
                if (null == noticeFragment) {
                    noticeFragment = new NoticeFragment();
                }
                transaction.replace(R.id.main_activity_fragment, noticeFragment, "notice");
                transaction.addToBackStack(null);
                break;
            case R.id.nav_help:
                if (null == helpFragment) {
                    helpFragment = new HelpFragment();
                }
                transaction.replace(R.id.main_activity_fragment, helpFragment, "help");
                transaction.addToBackStack(null);
                break;
            case R.id.nav_updata:
                if (null == updataFragment) {
                    updataFragment = new UpdataFragment();
                }
                transaction.replace(R.id.main_activity_fragment, updataFragment, "updata");
                transaction.addToBackStack(null);
                break;
            case R.id.nav_advice:
                if (null == adviceFragment) {
                    adviceFragment = new AdviceFragment();
                }
                transaction.replace(R.id.main_activity_fragment, adviceFragment, "advice");
                transaction.addToBackStack(null);
                break;
            default:
                if (null == mainFragment) {
                    mainFragment = new MainFragment();
                    mainFragment.setArguments(storeOrPerInfoBundle);
                    transaction.replace(R.id.main_activity_fragment, mainFragment, "main");
                }
                break;
        }
        transaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * 获取bing壁纸的相关信息
     *
     * @return
     */
    private bingWallPeper getBingWallPaperInfo() throws IOException {
        Date date = new Date();
        Log.i(TAG, "开始" + date.toString());
        BufferedReader in = null;
        StringBuilder resoult = new StringBuilder();
        try {
            String bingJsonRequst = "http://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1";

            Log.i(TAG, bingJsonRequst);

            URL url = new URL(bingJsonRequst);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");//请求方式
            conn.setReadTimeout(5 * 1000);//超时时间
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while (null != (line = in.readLine())) {
                resoult.append(line);
            }

            in.close();
        } catch (MalformedURLException e) {

            Log.e(TAG, "连接出错");
            e.printStackTrace();
        } catch (IOException e) {
            if (null != in) {
                in.close();
            }
            Log.i(TAG, "readcucuo ");
            e.printStackTrace();
        }
        Gson gson = new GsonBuilder().create();

        Log.i(TAG, resoult.toString());

        bingWallPeper bing = gson.fromJson(resoult.toString(), bingWallPeper.class);

        return bing;
    }

}


//json 类
class bingWallPeper {

    public List<image> images;
    public com.nuc.hikeplus.tooltips tooltips;
}

class image {
    public String startdate;
    public String fullstartdate;
    public String enddate;
    public String url;
    public String urlbase;
    public String copyright;
    public String copyrightlink;
    public boolean wp;
    public String hsh;
    public int drk;
    public int top;
    public int bot;
    public List<String> hs;
}

class tooltips {
    public String loading;
    public String previous;
    public String next;
    public String walle;
    public String walls;
}
