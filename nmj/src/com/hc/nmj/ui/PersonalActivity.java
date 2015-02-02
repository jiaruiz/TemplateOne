package com.hc.nmj.ui;

import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.ConnectException;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.kymjs.kjframe.utils.PreferenceHelper;

import com.hc.nmj.R;
import com.hc.nmj.common.Constant;
import com.hc.nmj.utils.DBUtils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Paint;

public class PersonalActivity extends Activity {
    private ProgressDialog progressDialog;
    private RefreshHandler refreshHandler;
    private static String username;
    private static String password;
    
    public RelativeLayout input_area;
    private TextView pwd_forget;
    private TextView register;
    private EditText user_input;
    private EditText pwd_input;
    private Button login_btn;

    ImageView login_or;
    RelativeLayout login_after;
    TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath().build());

        input_area = (RelativeLayout) findViewById(R.id.login_input_area);
        input_area.setAlpha(0.25F);

        pwd_forget = (TextView) findViewById(R.id.login_forgetpwd);
        pwd_forget.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        register = (TextView) findViewById(R.id.login_register);
        register.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        register.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(PersonalActivity.this, Register.class);
                PersonalActivity.this.startActivity(intent);
            }
        });

        user_input = (EditText) findViewById(R.id.login_userinput);
        pwd_input = (EditText) findViewById(R.id.login_pwdinput);
        login_or = (ImageView) findViewById(R.id.login_or);
        login_after = (RelativeLayout) findViewById(R.id.login_after);
        name = (TextView) findViewById(R.id.login_user);

        refreshHandler = new RefreshHandler(this);

        login_btn = (Button) findViewById(R.id.login_btn);
        login_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if ("退出".equalsIgnoreCase(login_btn.getText().toString())) {
                    input_area.setVisibility(View.VISIBLE);
                    pwd_forget.setVisibility(View.VISIBLE);
                    login_or.setVisibility(View.VISIBLE);
                    register.setVisibility(View.VISIBLE);
                    login_after.setVisibility(View.GONE);
                    name.setText(username);
                    
                    login_btn.setText("登陆");
                    user_input.setText("");
                    pwd_input.setText("");
                    return;
                }
                username = user_input.getText().toString();
                password = pwd_input.getText().toString();
                
                PreferenceHelper.write(PersonalActivity.this, "user", "username", username);
                PreferenceHelper.write(PersonalActivity.this, "user", "password", password);
                
                if (username == null || username.equals("")) {
                    new AlertDialog.Builder(PersonalActivity.this).setTitle("提示").setMessage("用户名为空\n请输入用户名").setNeutralButton("确定", null).show();
                } else if (password == null || password.equals("")) {
                    new AlertDialog.Builder(PersonalActivity.this).setTitle("提示").setMessage("密码为空\n请输入密码").setNeutralButton("确定", null).show();
                } else {
                    password = getMD5Str(password);
                    progressDialog = new ProgressDialog(PersonalActivity.this);
                    progressDialog.setTitle("登录中...");
                    progressDialog.setMessage("登录中\n请稍候！");
                    progressDialog.show();
                    if (DBUtils.checkLogin(username, password)) {
                        progressDialog.dismiss();
                        loginSuccess();
                    } else {
                        Toast.makeText(PersonalActivity.this, "登陆失败，输入有误", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        return;
                    }
                    
                    progressDialog.setOnCancelListener(new OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface arg0) {
                            // TODO Auto-generated method stub
                            progressDialog.dismiss();
                            PersonalActivity.this.finish();
                        }
                    });
                    // refreshHandler.sleep(100);
                    
                }
            }
        });
        checkLogin();
    }

    private void checkLogin() {
        if (Constant.login) {
            loginSuccess();
        }
    }


    private class RefreshHandler extends Handler {
        WeakReference<PersonalActivity> mActivity;
        JSONObject jsonObject;

        RefreshHandler(PersonalActivity activity) {
            mActivity = new WeakReference<PersonalActivity>(activity);
        }

        public void handleMessage(Message msg) {
            HttpPost request;
            HttpClient client = new DefaultHttpClient();
            String url = "http://101.227.253.17:8181" + "/service/1/zh-cn/sdfaf-awefhu-9867ke/personal/login?name=" + username + "&pwd=" + password;
            try {
                request = new HttpPost(new URI(url));
                HttpParams params = client.getParams();
                params.setParameter("charset", HTTP.UTF_8);
                HttpConnectionParams.setConnectionTimeout(params, 3000);
                HttpConnectionParams.setSoTimeout(params, 5000);
                HttpResponse httpResponse = client.execute(request);
                if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    String tempResult = EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8);
                    jsonObject = new JSONObject(tempResult);
                    if (jsonObject.getInt("resultCode") == 0) {
                        loginSuccess();
                    } else {
                        mActivity.get().progressDialog.dismiss();
                        new AlertDialog.Builder(mActivity.get()).setTitle("提示").setMessage(jsonObject.getString("resultMsg")).setNeutralButton("确定", null)
                                .show();
                    }
                }
                loginSuccess();
            } catch (ConnectException e) {
                mActivity.get().progressDialog.dismiss();
                new AlertDialog.Builder(mActivity.get()).setTitle("Warning!").setMessage("无法连接到网络，请检查网络!").setNeutralButton("确定", null).show();
            } catch (InterruptedIOException e) {
                mActivity.get().progressDialog.dismiss();
                new AlertDialog.Builder(mActivity.get()).setTitle("Warning!").setMessage("无法连接到网络，请检查网络!").setNeutralButton("确定", null).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void sleep(long delayMillis) {
            this.removeMessages(0);
            sendMessageDelayed(obtainMessage(0), delayMillis);
        }
    }

    private void loginSuccess() {
        // Intent intent=new Intent(mActivity.get(), SearchActivity.class);
        // mActivity.get().startActivity(intent);
        // mActivity.get().finish();
        login_btn.setText("退出");
        input_area.setVisibility(View.GONE);
        pwd_forget.setVisibility(View.GONE);
        login_or.setVisibility(View.GONE);
        register.setVisibility(View.GONE);
        login_after.setVisibility(View.VISIBLE);
        name.setText(PreferenceHelper.readString(PersonalActivity.this, "user", "username"));
        hideInputMethodManager();
        Constant.login = true;
//        DBUtils.createTestData();
    }

    private String getMD5Str(String str) {
        MessageDigest messageDigest = null;

        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException caught!");
            System.exit(-1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        byte[] byteArray = messageDigest.digest();

        StringBuffer md5StrBuff = new StringBuffer();

        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            } else {
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
            }
        }
        return md5StrBuff.toString().toUpperCase();
    }

    public void onResume() {
        super.onResume();
        // MobclickAgent.onPageStart("Login"); //统计页面
        // MobclickAgent.onResume(this); //统计时长
    }

    public void onPause() {
        super.onPause();
        // MobclickAgent.onPageEnd("Login"); // 保证 onPageEnd 在onPause 之前调用,因为
        // onPause 中会保存信息
        // MobclickAgent.onPause(this);
    }
    
    private void hideInputMethodManager() {
        InputMethodManager mIm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (mIm.isActive()) {
            mIm.hideSoftInputFromWindow(user_input.getWindowToken(),
                    InputMethodManager.RESULT_UNCHANGED_SHOWN);
        }
    }
}
