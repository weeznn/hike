package com.nuc.hikeplus;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by weeznn on 2016/8/28.
 */
public class Tools {


    public static void showDialog(Context context,
                                  WindowManager manager,
                                  String message,
                                  String title,
                                  View view,
                                  String yes_btn,
                                  DialogInterface.OnClickListener listener_yes,
                                  String no_btn,
                                  DialogInterface.OnClickListener listener_no) {
        if (null == no_btn) {
            AlertDialog alertDialog = new AlertDialog.Builder(context)
                    .setMessage(message)
                    .setTitle(title)
                    .setView(view)
                    .setPositiveButton(yes_btn, listener_yes)
                    .create();
            Window window = alertDialog.getWindow();
            window.setGravity(Gravity.BOTTOM | Gravity.LEFT);
            Display d = manager.getDefaultDisplay();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.height = (int) (d.getHeight() * 0.5);
            lp.width = d.getWidth();
            lp.alpha = 0.7f;
            window.setAttributes(lp);
            alertDialog.show();
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(context)
                    .setMessage(message)
                    .setTitle(title)
                    .setView(view)
                    .setPositiveButton(yes_btn, listener_yes)
                    .setNegativeButton(no_btn, listener_no)
                    .create();
            Window window = alertDialog.getWindow();
            window.setGravity(Gravity.BOTTOM | Gravity.LEFT);
            Display d = manager.getDefaultDisplay();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.height = (int) (d.getHeight() * 0.5);
            lp.width = d.getWidth();
            lp.alpha = 1.0f;

            window.setAttributes(lp);
            alertDialog.show();
        }


    }


}

//class sendMesConfig{
//        /**
//         * url前半部分
//         */
//        final static String BASE_URL = "https://api.miaodiyun.com/20150822";
//
//        /**
//         * 开发者注册后系统自动生成的账号，可在官网登录后查看
//         */
//        final static String ACCOUNT_SID = "84f7f994fe0a49e486062bfb4e6d7f0d";
//
//        /**
//         * 开发者注册后系统自动生成的TOKEN，可在官网登录后查看
//         */
//        final static String AUTH_TOKEN = "3b6e4f508cd54623844c529f03af49ab";
//
//        /**
//         * 响应数据类型, JSON或XML
//         */
//        final static String RESP_DATA_TYPE = "json";
//
//
//}
///**
// * http请求工具
// */
//public class HttpUtil
//{
//    /**
//     * 构造通用参数timestamp、sig和respDataType
//     *
//     * @return
//     */
//    public static String createCommonParam()
//    {
//        // 时间戳
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
//        String timestamp = sdf.format(new Date());
//
//        // 签名
//        String sig = DigestUtils.md5Hex(sendMesConfig.ACCOUNT_SID + sendMesConfig.AUTH_TOKEN + timestamp);
//
//        return "&timestamp=" + timestamp + "&sig=" + sig + "&respDataType=" + sendMesConfig.RESP_DATA_TYPE;
//    }
//
//    /**
//     * post请求
//     *
//     * @param url
//     *            功能和操作
//     * @param body
//     *            要post的数据
//     * @return
//     * @throws IOException
//     */
//    public static String post(String url, String body)
//    {
//        System.out.println("url:" + System.lineSeparator() + url);
//        System.out.println("body:" + System.lineSeparator() + body);
//
//        String result = "";
//        try
//        {
//            OutputStreamWriter out = null;
//            BufferedReader in = null;
//            URL realUrl = new URL(url);
//            URLConnection conn = realUrl.openConnection();
//
//            // 设置连接参数
//            conn.setDoOutput(true);
//            conn.setDoInput(true);
//            conn.setConnectTimeout(5000);
//            conn.setReadTimeout(20000);
//
//            // 提交数据
//            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
//            out.write(body);
//            out.flush();
//
//            // 读取返回数据
//            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
//            String line = "";
//            boolean firstLine = true; // 读第一行不加换行符
//            while ((line = in.readLine()) != null)
//            {
//                if (firstLine)
//                {
//                    firstLine = false;
//                } else
//                {
//                    result += System.lineSeparator();
//                }
//                result += line;
//            }
//        } catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//        return result;
//    }
//
//    /**
//     * 回调测试工具方法
//     *
//     * @param url
//     * @param reqStr
//     * @return
//     */
//    public static String postHuiDiao(String url, String body)
//    {
//        String result = "";
//        try
//        {
//            OutputStreamWriter out = null;
//            BufferedReader in = null;
//            URL realUrl = new URL(url);
//            URLConnection conn = realUrl.openConnection();
//
//            // 设置连接参数
//            conn.setDoOutput(true);
//            conn.setDoInput(true);
//            conn.setConnectTimeout(5000);
//            conn.setReadTimeout(20000);
//
//            // 提交数据
//            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
//            out.write(body);
//            out.flush();
//
//            // 读取返回数据
//            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
//            String line = "";
//            boolean firstLine = true; // 读第一行不加换行符
//            while ((line = in.readLine()) != null)
//            {
//                if (firstLine)
//                {
//                    firstLine = false;
//                } else
//                {
//                    result += System.lineSeparator();
//                }
//                result += line;
//            }
//        } catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//        return result;
//    }
//}