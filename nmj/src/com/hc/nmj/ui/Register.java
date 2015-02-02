package com.hc.nmj.ui;

import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.ConnectException;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

import com.hc.nmj.R;
import com.hc.nmj.utils.DBUtils;
import com.hc.nmj.utils.LocationApplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;

public class Register extends Activity {
    private ProgressDialog progressDialog;
    private RegisterHandler registerHandler;

    private RelativeLayout input_area;
    private Button register_getcode_btn;
    private Button register_btn;
    private TextView login;

    private EditText phoneNum_input;
    private CheckBox secret_checkbox;
    private EditText name_input;
    private EditText inviteCode_input;
    private EditText smsCode_input;
    private EditText pwdSet_input;
    private EditText pwdConfirm_input;

    private static String phoneNum;
    private static String name;
    private static String inviteCode;
    private static String smsCode;
    private static String pwdSet;
    private static String pwdConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath().build());

        input_area = (RelativeLayout) findViewById(R.id.register_input_area);
        input_area.setAlpha(0.25F);

        phoneNum_input = (EditText) findViewById(R.id.register_phoneinput);
        name_input = (EditText) findViewById(R.id.register_nameinput);
        inviteCode_input = (EditText) findViewById(R.id.register_inviteinput);
        smsCode_input = (EditText) findViewById(R.id.register_smsinput);
        pwdSet_input = (EditText) findViewById(R.id.register_pwdinput);
        pwdConfirm_input = (EditText) findViewById(R.id.register_pwdconfirm);
        secret_checkbox = (CheckBox) findViewById(R.id.register_secret_checkbox);

        register_getcode_btn = (Button) findViewById(R.id.register_smsbtn);
        register_getcode_btn.setAlpha(0.9F);
        register_getcode_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                phoneNum = phoneNum_input.getText().toString();
                if (!isPhoneNumValid(phoneNum)) {
                    new AlertDialog.Builder(Register.this).setTitle("提示").setMessage("手机号码有误，请重新输入").setNeutralButton("确定", null).show();
                }
                HttpPost request;
                HttpClient client = new DefaultHttpClient();
                String url = "http://101.227.253.17:8181" + "/service/1/zh-cn/sdfaf-awefhu-9867ke/sms/check/member/" + phoneNum;
                try {
                    request = new HttpPost(new URI(url));
                    HttpParams params = client.getParams();
                    params.setParameter("charset", HTTP.UTF_8);
                    HttpConnectionParams.setConnectionTimeout(params, 3000);
                    HttpConnectionParams.setSoTimeout(params, 5000);
                    HttpResponse httpResponse = client.execute(request);
                    if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                        String tempResult = EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8);
                        JSONObject jsonObject = new JSONObject(tempResult);
                        if (jsonObject.getInt("resultCode") == 0) {
                            new AlertDialog.Builder(Register.this).setTitle("提示").setMessage("验证码发送成功，请注意查收").setNeutralButton("确定", null).show();
                        } else {
                            new AlertDialog.Builder(Register.this).setTitle("提示").setMessage(jsonObject.getString("resultMsg")).setNeutralButton("确定", null)
                                    .show();
                        }
                    }
                } catch (ConnectException e) {
                    new AlertDialog.Builder(Register.this).setTitle("Warning!").setMessage("无法连接到网络，请检查网络!")
                            .setNeutralButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub
                                }
                            }).show();
                } catch (InterruptedIOException e) {
                    new AlertDialog.Builder(Register.this).setTitle("Warning!").setMessage("无法连接到网络，请检查网络!").setNeutralButton("确定", null).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        registerHandler = new RegisterHandler(this);

        register_btn = (Button) findViewById(R.id.register_btn);
        register_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                name = name_input.getText().toString();
                inviteCode = inviteCode_input.getText().toString();
                phoneNum = phoneNum_input.getText().toString();
                smsCode = smsCode_input.getText().toString();
                pwdSet = pwdSet_input.getText().toString();
                pwdConfirm = pwdConfirm_input.getText().toString();

                if (!secret_checkbox.isChecked()) {
                    new AlertDialog.Builder(Register.this).setTitle("提示!").setMessage("需要同意隐私条款").setNeutralButton("确定", null).show();
                } else if (name == null || name.equals("")) {
                    new AlertDialog.Builder(Register.this).setTitle("提示!").setMessage("姓名为空\n请输入姓名").setNeutralButton("确定", null).show();
                }
                // else if(inviteCode == null || inviteCode.equals(""))
                // {
                // new
                // AlertDialog.Builder(Register.this).setTitle("提示!").setMessage("邀请码为空\n请输入邀请码").setNeutralButton("确定",
                // null).show();
                // }
                // else if(phoneNum == null || phoneNum.equals(""))
                // {
                // new
                // AlertDialog.Builder(Register.this).setTitle("提示!").setMessage("手机号码为空\n请输入手机号码").setNeutralButton("确定",
                // null).show();
                // }
                // else if(!isPhoneNumValid(phoneNum))
                // {
                // new
                // AlertDialog.Builder(Register.this).setTitle("提示!").setMessage("手机号码有误\n请重新输入").setNeutralButton("确定",
                // null).show();
                // }
                // else if(smsCode == null || smsCode.equals(""))
                // {
                // new
                // AlertDialog.Builder(Register.this).setTitle("提示!").setMessage("验证码为空\n请输入验证码").setNeutralButton("确定",
                // null).show();
                // }
                else if (pwdSet == null || pwdSet.equals("")) {
                    new AlertDialog.Builder(Register.this).setTitle("提示!").setMessage("密码为空\n请输入密码").setNeutralButton("确定", null).show();
                }
                // else if(pwdConfirm == null || pwdConfirm.equals(""))
                // {
                // new
                // AlertDialog.Builder(Register.this).setTitle("提示!").setMessage("确认密码为空\n请确认密码").setNeutralButton("确定",
                // null).show();
                // }
                // else if(!pwdSet.equals(pwdConfirm))
                // {
                // new
                // AlertDialog.Builder(Register.this).setTitle("提示!").setMessage("两次输入密码不一致\n请重新输入").setNeutralButton("确定",
                // null).show();
                // }
                else {
                    pwdSet = getMD5Str(pwdSet);
                    progressDialog = new ProgressDialog(Register.this);
                    progressDialog.setTitle("注册中...");
                    progressDialog.setMessage("注册中\n请稍候！");
                    progressDialog.show();
                    new Handler(){
                        public void handleMessage(Message msg) {
                            progressDialog.dismiss();
                            if (DBUtils.checkUserExist(name)) {
                                Toast.makeText(Register.this, "该用户已存在", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            DBUtils.createUsrData(name, pwdSet);
                            Toast.makeText(Register.this, "注册成功", Toast.LENGTH_SHORT).show();
                            finish();
                        };
                    }.sendEmptyMessageDelayed(0, 2000);
                }
            }
        });

        login = (TextView) findViewById(R.id.register_login);
        login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
    }

    private boolean isPhoneNumValid(String num) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(num);
        return m.matches();
    }

    private static class RegisterHandler extends Handler {
        WeakReference<Register> mActivity;
        JSONObject jsonObject;

        RegisterHandler(Register activity) {
            mActivity = new WeakReference<Register>(activity);
        }

        public void handleMessage(Message msg) {
            HttpPost request;
            HttpClient client = new DefaultHttpClient();
            String url = "http://101.227.253.17:8181" + "/service/1/zh-cn/sdfaf-awefhu-9867ke/personal/reg?phone=" + phoneNum + "&name=encodeURI('" + name
                    + "')&pwd=" + pwdSet + "&code=" + smsCode + "&recommend=" + inviteCode;
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
                        mActivity.get().progressDialog.dismiss();
                        new AlertDialog.Builder(mActivity.get()).setTitle("提示!").setMessage("注册成功\n请返回重新登录").setNeutralButton("确定", null).show();
                    } else {
                        mActivity.get().progressDialog.dismiss();
                        new AlertDialog.Builder(mActivity.get()).setTitle("提示!").setMessage(jsonObject.getString("resultMsg")).setNeutralButton("确定", null)
                                .show();
                    }
                } else {
                    mActivity.get().progressDialog.dismiss();
                    new AlertDialog.Builder(mActivity.get()).setTitle("提示!").setMessage("服务器存在问题\n请与服务提供商联系").setNeutralButton("确定", null).show();
                }
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
        // MobclickAgent.onPageStart("Register"); //统计页面
        // MobclickAgent.onResume(this); //统计时长
    }

    public void onPause() {
        super.onPause();
        // MobclickAgent.onPageEnd("Register"); // 保证 onPageEnd 在onPause 之前调用,因为
        // onPause 中会保存信息
        // MobclickAgent.onPause(this);
    }
}
