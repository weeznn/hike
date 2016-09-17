package layout;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.nuc.hikeplus.HikeUnityActivity;
import com.nuc.hikeplus.LoginActivity;
import com.nuc.hikeplus.OlderActivity;
import com.nuc.hikeplus.R;
import com.nuc.hikeplus.SearchActivity;

public class MainFragment extends Fragment {
    private static final String TAG = "mainFragment";

    private Context context;
    private Toolbar toolbar=null;
    private FloatingActionButton fab1=null,fab2=null;
    private WebView webView=null;
    private boolean blockNetImage;
    private Button button=null;//测试
    private FrameLayout frameLayout=null;
    private ImageView frameLy_inageView=null;

    private boolean isPer=false;
    private String receiverName=null;
    private String receiverTel=null;
    private String receiverAddress=null;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getContext();

        Bundle bundle=getArguments();
        isPer=bundle.getBoolean("isPer");
        receiverName=bundle.getString("name");
        receiverTel=bundle.getString("tel");
        receiverAddress=bundle.getString("address");
        Log.i(TAG,isPer+"  "+receiverName+receiverTel+receiverAddress);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);


        fab1 = (FloatingActionButton) view.findViewById(R.id.fgm_fab_1);
        fab2 = (FloatingActionButton) view.findViewById(R.id.fgm_fab_2);
        webView = (WebView) view.findViewById(R.id.webview);
        button = (Button) view.findViewById(R.id.btn_main);
        frameLayout= (FrameLayout) view.findViewById(R.id.content_main_frameLy);
        frameLy_inageView= (ImageView) view.findViewById(R.id.content_main_frameLy_image);


        //测试下单按钮
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OlderActivity.class);
                intent.putExtra("goodsName", "花瓶");
                intent.putExtra("receiverName", "郝文章");
                intent.putExtra("receiverTel", "15513020014");
                intent.putExtra("receiverProvince", "山西省");
                intent.putExtra("receiverCity", "太原市");
                intent.putExtra("receiverAddress", "中北大学");
                startActivity(intent);
            }
        });
        //VR商城按钮
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isPer){
                    Log.i(TAG, "mainactivity2unity");
                    Intent intent = new Intent(getActivity(), HikeUnityActivity.class);
                    intent.putExtra("receiverName",receiverName);
                    intent.putExtra("receiverTel",receiverTel);
                    String[] strings= LoginActivity.slideAddress(receiverAddress);
                    intent.putExtra("receiverProvince", strings[0]);
                    intent.putExtra("receiverCity", strings[1]);
                    intent.putExtra("receiverAddress", strings[2]);
                    intent.putExtra("receiverHeight", "170");

                    startActivity(intent);
                }else{
                    Log.i(TAG, "mainactivity2loginactivity");
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(intent, 1);
                }

            }
        });

        //查询按钮
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "mainactivity2searchactivity");
                Intent intent=new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("isPer",isPer);

                Log.i(TAG,"  传给search  "+isPer);
                startActivity(intent);
            }
        });

        //webview

        webView.loadUrl("http://www.sfbest.com/");
        final WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        //提高渲染优先级
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        //最后加载图片
        webSettings.setBlockNetworkImage(true);
        blockNetImage=true;
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if(newProgress>=100){
                    webView.setVisibility(View.VISIBLE);
                    frameLy_inageView.setVisibility(View.GONE);
                    AnimationDrawable animationDrawable= (AnimationDrawable) frameLy_inageView.getDrawable();
                    animationDrawable.stop();
                }else{
                    frameLy_inageView.setImageDrawable(getResources().getDrawable(R.drawable.running_man));
                    AnimationDrawable animationDrawable= (AnimationDrawable) frameLy_inageView.getDrawable();
                    animationDrawable.start();
                }
                if(blockNetImage){
                    webSettings.setBlockNetworkImage(false);
                    blockNetImage=false;
                }
            }
        });
        //使用缓存
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setDomStorageEnabled(true);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (1 == requestCode && 1 == resultCode) {
            Bundle info = data.getExtras();
//            String receiverName = info.getString("receiverName");
//            String receiverTel = info.getString("receiverTel");
//            String receiverProvince = info.getString("receiverProvince");
//            String receiverCity = info.getString("receiverCity");
//            String receiverAddress = info.getString("receiverAddress");
//            String receiverHeight = info.getString("receiverHeight");

//            Log.i(TAG, receiverName + "  " + receiverTel + "  " + receiverHeight + "  " + receiverAddress);

            Intent intent = new Intent(getActivity(), HikeUnityActivity.class);
            intent.putExtra("receiverName", info.getString("receiverName"));
            intent.putExtra("receiverTel", info.getString("receiverTel"));
            intent.putExtra("receiverProvince", info.getString("receiverProvince"));
            intent.putExtra("receiverCity", info.getString("receiverCity"));
            intent.putExtra("receiverAddress", info.getString("receiverAddress"));
            intent.putExtra("receiverHeight", info.getString("receiverHeight"));

            Log.i(TAG, "mainactivity2unityactivity");

            startActivity(intent);
        }
    }
}
