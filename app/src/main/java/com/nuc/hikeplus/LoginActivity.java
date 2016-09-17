package com.nuc.hikeplus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;

/**
 * 得到使用者的信息
 * 由mainactivity调用 以String类型返回使用者的name,tel,address,height
 */
public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";


    // UI references.
    private EditText mNameView;
    private EditText mTelView;
    private EditText mAddressView;
    private EditText mHeightView;
    private View mProgressView;
    private View mLoginFormView;
    private String[] strings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mNameView = (EditText) findViewById(R.id.name);
        mTelView = (EditText) findViewById(R.id.tel);
        mAddressView = (EditText) findViewById(R.id.address);
        mHeightView = (EditText) findViewById(R.id.height);

        ImageButton mSignInButton = (ImageButton) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

//        mLoginFormView = findViewById(R.id.login_form);
//        mProgressView = findViewById(R.id.login_progress);
    }


    /**
     * 获取信息 并进行相应的检查
     */
    private void attemptLogin() {

        // Reset errors.
        mNameView.setError(null);
        mTelView.setError(null);
        mAddressView.setError(null);
        mHeightView.setError(null);

        String name = mNameView.getText().toString();
        String tel = mTelView.getText().toString();
        String address = mAddressView.getText().toString();
        String height = mHeightView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // 检查address
        if (TextUtils.isEmpty(address)) {
            mAddressView.setError("地址为空");
            cancel = true;
            focusView = mAddressView;
        } else {
            strings = slideAddress(address);
            Log.i(TAG, strings[0] + "   " + strings[1] + "   " + strings[2]);
            if (strings[0]==null) {
                mAddressView.setError("地址填写不规范");
                cancel = true;
                focusView = mAddressView;
            }
            if (strings[1]==null) {
                mAddressView.setError("地址填写不规范");
                cancel = true;
                focusView = mAddressView;
            }
        }

        // 检查tel
        Log.i("LoginActivity", tel);
        if (11 != tel.length()) {
            mTelView.setError("手机号码位数错误");
            cancel = true;
            focusView = mTelView;
        }

        // 检查name
        if (TextUtils.isEmpty(name)) {
            mNameView.setError("姓名为空");
            cancel = true;
            focusView = mNameView;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            // 填写信息准确返回值并结束本activity
            Intent intent = getIntent();
            intent.putExtra("receiverName", name);
            intent.putExtra("receiverTel", tel);
            intent.putExtra("receiverProvince", strings[0]);
            intent.putExtra("receiverCity", strings[1]);
            intent.putExtra("receiverAddress", strings[2]);
            intent.putExtra("receiverHeight", height);
            LoginActivity.this.setResult(1, intent);
            LoginActivity.this.finish();
        }
    }

    /**
     * 地址分离  省   市   具体地址
     *
     * @param address
     * @return
     */
    public static String[] slideAddress(String address) {
        //Log.i(TAG, address);
        String pro = new String();
        String cit = new String();
        String adr = new String();
        int i = address.indexOf("省");
        int j = address.indexOf("市", i + 1);
        //Log.i(TAG,i+"   "+j);
        if(i!=-1&&j!=-1){
            pro = address.substring(0, i);
            cit = address.substring(i + 1, j);
            adr = address.substring(j + 1, address.length());
            Log.i(TAG, i + "  " + pro + "  " + j + "   " + cit + "  " + adr);
            return new String[]{pro, cit, adr};
        }else{
            return new String[]{null,null,null};
        }


    }
}

