package com.nuc.hikeplus;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by weeznn on 2016/7/20.
 */
public class JSONutils {
    //快递接口
    private static final String TAG = "JSONutils";
    private final String BUSINESSID = "1261258";
    private final String APPKEY = "722e2445-533a-4b4a-abe4-d671048598ec";
    private final String OLDERREQURL = "http://testapi.kdniao.cc:8081/api/OOrderService";
    private final String SEARCHREQURL = "http://api.kdniao.cc/Ebusiness/EbusinessOrderHandle.aspx";

    //订单信息
    private String orderNo = null;
    private String orderDate = null;
    private String receiverName = null;
    private String receiverTel = null;
    private String receiverProvince = null;
    private String receiverCity = null;
    private String receiverAddress = null;
    private String goodsName = null;
    private String goodWeight = null;
    private String sendName = null;
    private String sendTel = null;
    private String sendProvince = null;
    private String sendCity = null;
    private String sendAddress = null;

    //物流编号
    private String logisticCode = null;


    public JSONutils(String orderNo, String orderDate, String receiverName, String receiverTel, String receiverProvince
            , String receiverCity, String receiverAddress, String goodsName, String goodsWeight,
                     String sendName, String sendTel, String sendProvince, String sendCity, String sendAddress) {

        this.orderNo = orderNo;
        this.orderDate = orderDate;
        this.receiverName = receiverName;
        this.receiverTel = receiverTel;
        this.receiverProvince = receiverProvince;
        this.receiverCity = receiverCity;
        this.receiverAddress = receiverAddress;
        this.goodsName = goodsName;
        this.goodWeight = goodsWeight;
        this.sendName = sendName;
        this.sendTel = sendTel;
        this.sendProvince = sendProvince;
        this.sendCity = sendCity;
        this.sendAddress = sendAddress;
        Log.i(TAG, "JSON初始化完成");

    }

    public JSONutils(String order, String logisticCode) {
        this.orderNo = order;
        this.logisticCode = logisticCode;
    }

    /**
     * 发送在线下单请求
     *
     * @return
     * @throws Exception
     */
    public Bundle JsonOnlineOlderSend() throws Exception {
        Log.i(TAG, "在线下单请求准备");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ShipperCode", "SF");
        jsonObject.put("OrderCode", orderNo);
        jsonObject.put("PayType", 4);
        jsonObject.put("ExpType", 1);
        JSONObject Sender = new JSONObject();
        Sender.put("Name", sendName);
        Sender.put("Mobile", sendTel);
        Sender.put("ProvinceName", sendProvince);
        Sender.put("CityName", sendCity);
        Sender.put("Address", sendAddress);
        jsonObject.put("Sender", Sender);
        JSONObject Receiver = new JSONObject();
        Receiver.put("Name", receiverName);
        Receiver.put("Mobile", receiverTel);
        Receiver.put("ProvinceName", receiverProvince);
        Receiver.put("CityName", receiverCity);
        Receiver.put("Address", receiverAddress);
        jsonObject.put("Receiver", Receiver);
        JSONObject Commodity = new JSONObject();
        Commodity.put("GoodsName", goodsName);
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(Commodity);
        jsonObject.put("Commodity", jsonArray);
        jsonObject.put("Weight", Double.valueOf(goodWeight));
        jsonObject.put("Remark", "小心轻放");

        Log.i(TAG, jsonObject.toString());

        Map<String, String> params = new HashMap<String, String>();
        params.put("RequestData", urlEncoder(jsonObject.toString(), "UTF-8"));
        params.put("EBusinessID", BUSINESSID);
        params.put("RequestType", "1001");
        String dataSign = encrypt(jsonObject.toString(), APPKEY, "UTF-8");
        params.put("DataSign", urlEncoder(dataSign, "UTF-8"));
        params.put("DataType", "2");

        Log.i(TAG, "发送ing");
        String resoult = sendPost(OLDERREQURL, params);
        Log.i(TAG, "发送成功 返回结果");
        Log.i(TAG, "结果是：" + resoult);
        //处理返回的json数据
        Gson gson = new GsonBuilder().create();
        OnlineOlderSuccess olderSuccess = gson.fromJson(resoult, OnlineOlderSuccess.class);
        if (olderSuccess.Order == null) {
            Log.i(TAG, "gson返回null");
        }
        Bundle bundle = new Bundle();
        bundle.putString("logistic", olderSuccess.Order.LogisticCode);
        bundle.putString("reason", olderSuccess.Reason);
        bundle.putBoolean("success", olderSuccess.Success);
        Log.i(TAG, "  " + bundle.toString());
        return bundle;
    }

    //在线下单结果返回类
    public class OnlineOlderSuccess {
        public String EBusinessID;
        public boolean Success;
        public OrderClass Order;
        public String ResultCode;
        public String Reason;
        public String UniquerRequestNumber;

        public class OrderClass {
            public String OrderCode;
            public String ShipperCode;
            public String LogisticCode;
        }
    }


    /**
     * 查寻路线
     *
     * @return
     * @throws Exception
     */
    public Bundle jsonSearch() throws Exception {

        Log.i(TAG,"search json ready");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("OrderCode", orderNo);
        jsonObject.put("ShipperCode", "SF");
        jsonObject.put("LogisticCode", logisticCode);

        Log.i(TAG, "requst" + jsonObject.toString());

        Map<String, String> params = new HashMap<String, String>();
        params.put("RequestData", urlEncoder(jsonObject.toString(), "UTF-8"));
        params.put("EBusinessID", BUSINESSID);
        params.put("RequestType", "1002");
        String dataSign = encrypt(jsonObject.toString(), APPKEY, "UTF-8");
        params.put("DataSign", urlEncoder(dataSign, "UTF-8"));
        params.put("DataType", "2");

        String result = sendPost(SEARCHREQURL, params);

        Log.i(TAG, "result" + result);

        Gson gson = new GsonBuilder().create();
        SearchResoult searchResoult = gson.fromJson(result, SearchResoult.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("isSuccess", searchResoult.Success);
        bundle.putInt("traceSize",searchResoult.Traces.size());
        for (int i = 0; i < searchResoult.Traces.size(); i++) {
            String[] strings=new String[]{searchResoult.Traces.get(i).AcceptTime,
                    searchResoult.Traces.get(i).AcceptStation,
                    searchResoult.Traces.get(i).Remark};
            bundle.putStringArray("str"+i,strings);
        }
        return bundle;
    }

    //物流追踪返回结果类
    public class SearchResoult {
        public String EBusinessID;
        public String OrderCode;
        public String ShipperCode;
        public String LogisticCode;
        public boolean Success;
        public String Reason;
        public int State;
        //public Trace[] Traces;
        public List<Trace> Traces;

        class Trace {
            public String AcceptTime;
            public String AcceptStation;
            public String Remark;
        }

    }




    /**
     * MD5加密
     *
     * @param str
     * @param charset
     * @return
     * @throws Exception
     */
    private String MD5(String str, String charset) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(str.getBytes(charset));
        byte[] result = md.digest();
        StringBuffer sb = new StringBuffer(32);
        for (int i = 0; i < result.length; i++) {
            int val = result[i] & 0xff;
            if (val <= 0xf) {
                sb.append("0");
            }
            sb.append(Integer.toHexString(val));
        }
        return sb.toString().toLowerCase();
    }

    /**
     * base64编码
     *
     * @param str
     * @param charset
     * @return
     * @throws UnsupportedEncodingException
     */
    private String base64(String str, String charset) throws UnsupportedEncodingException {
        String encoded = base64Encode(str.getBytes(charset));
        return encoded;
    }


    /**
     * 电商签名生成
     *
     * @param content
     * @param keyValue
     * @param charset
     * @return
     * @throws UnsupportedEncodingException
     * @throws Exception
     */
    private String encrypt(String content, String keyValue, String charset) throws UnsupportedEncodingException, Exception {
        if (keyValue != null) {
            return base64(MD5(content + keyValue, charset), charset);
        }
        return base64(MD5(content, charset), charset);
    }


    /**
     * 向指定URL发送post请求
     *
     * @param url    指定url
     * @param params 请求参数集合
     * @return 资源响应结果
     */
    @SuppressWarnings("unused")
    private String sendPost(String url, Map<String, String> params) {
        OutputStreamWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try {
            URL realUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();

            conn.setDoOutput(true);
            conn.setDoInput(true);

            conn.setRequestMethod("POST");

            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.connect();

            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");

            if (params != null) {
                StringBuilder param = new StringBuilder();
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    if (param.length() > 0) {
                        param.append("&");
                    }
                    param.append(entry.getKey());
                    param.append("=");
                    param.append(entry.getValue());
                    System.out.println(entry.getKey() + ":" + entry.getValue());
                }
                System.out.println("param:" + param.toString());
                out.write(param.toString());
            }

            out.flush();

            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result.toString();
    }


    private String urlEncoder(String str, String charset) throws UnsupportedEncodingException {
        String result = URLEncoder.encode(str, charset);
        return result;
    }

    private static char[] base64EncodeChars = new char[]{
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
            'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
            'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
            'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
            'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
            'w', 'x', 'y', 'z', '0', '1', '2', '3',
            '4', '5', '6', '7', '8', '9', '+', '/'};

    public static String base64Encode(byte[] data) {
        StringBuffer sb = new StringBuffer();
        int len = data.length;
        int i = 0;
        int b1, b2, b3;
        while (i < len) {
            b1 = data[i++] & 0xff;
            if (i == len) {
                sb.append(base64EncodeChars[b1 >>> 2]);
                sb.append(base64EncodeChars[(b1 & 0x3) << 4]);
                sb.append("==");
                break;
            }
            b2 = data[i++] & 0xff;
            if (i == len) {
                sb.append(base64EncodeChars[b1 >>> 2]);
                sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);
                sb.append(base64EncodeChars[(b2 & 0x0f) << 2]);
                sb.append("=");
                break;
            }
            b3 = data[i++] & 0xff;
            sb.append(base64EncodeChars[b1 >>> 2]);
            sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);
            sb.append(base64EncodeChars[((b2 & 0x0f) << 2) | ((b3 & 0xc0) >>> 6)]);
            sb.append(base64EncodeChars[b3 & 0x3f]);
        }
        return sb.toString();
    }


}

