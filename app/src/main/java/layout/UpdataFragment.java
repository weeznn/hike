
package layout;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nuc.hikeplus.DBTool;
import com.nuc.hikeplus.R;


public class UpdataFragment extends Fragment {

    private int dbversion = 1;
    private int appversion = 1;
    private DBTool dbTool = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbTool = new DBTool(getContext());
        dbversion = dbTool.getWritableDatabase().getVersion();

        PackageManager pm=getContext().getPackageManager();
        try {
            PackageInfo info=pm.getPackageInfo(getContext().getPackageName(),0);
            appversion=info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_updata, container, false);

        TextView dbv=(TextView) view.findViewById(R.id.fgu_dbv);
        dbv.setText(String.valueOf(dbversion));
        dbv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUpdata("数据库",dbversion);
            }
        });
        TextView appv = (TextView) view.findViewById(R.id.fgu_appv);
        appv.setText(String.valueOf(appversion));
        appv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUpdata("hikeplus",appversion);
            }
        });
        return view;
    }

    @Override
    public void onDestroy() {
        if (null != dbTool) {
            dbTool.close();
        }
    }

    //弹窗检查更新
    private void checkUpdata(final String title, final int version){
        ProgressDialog progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage(title);
        progressDialog.show();


        final Handler mhander=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                int newVersion=msg.arg1;
                if(newVersion>version){
                    dialog(title,true,newVersion);
                }else{
                    dialog(title,false,version);
                }
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    wait(1000);

                    Message message=new Message();
                    message.arg1=1;
                    mhander.sendMessage(message);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 是否更新弹窗选择
     * @param title
     * @param isupdata
     * @param verson
     */
    private void dialog(String title,boolean isupdata,int verson){
        String str=null;
        if(isupdata){
            str="检测到最新的"+title+"版本为"+verson+",是否更新？";
            new AlertDialog.Builder(getContext())
                    .setTitle(title)
                    .setMessage(str)
                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setNegativeButton("否",null)
                    .show();
        }else{
            str="您当前的版本为最新版本，无需更新！";
            new AlertDialog.Builder(getContext())
                    .setTitle(title)
                    .setMessage(str)
                    .setPositiveButton("确定", null)
                    .show();
        }

    }
}
