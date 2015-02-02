package com.hc.nmj.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hc.nmj.R;
import com.hc.nmj.base.NetWorkStateUtil;

public class TaobaoActivity extends Activity implements View.OnClickListener{
	
	
	private TextView mtb,mBack;
	private ImageButton mPre,mNext,mFresh;
	
	private WebView mWebView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_tabao);
		
		findAllViewById();
		
		Intent intent = getIntent();
		String url = intent.getStringExtra("url");


		WebSettings wSet = mWebView.getSettings();
		wSet.setJavaScriptEnabled(true);
		// 如果页面中链接，如果希望点击链接继续在当前browser中响应，
		// 而不是新开Android的系统browser中响应该链接，必须覆盖webview的WebViewClient对象
		mWebView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
				view.loadUrl(url);
				return true;
			}
		});
		mWebView.loadUrl(url);


	}

	private void findAllViewById() {
		mtb = (TextView) findViewById(R.id.tb_mytb);
		mtb.setOnClickListener((OnClickListener) this);
		mBack = (TextView) findViewById(R.id.tb_back);
		mBack.setOnClickListener(this);
		mWebView = (WebView) findViewById(R.id.tb_webview);
	}
	
	public void onClick(View paramView) {
		switch (paramView.getId()) {
		default:
		case R.id.tb_mytb:
			mWebView.loadUrl("http://my.m.taobao.com/myTaobao.htm");
			return;
		case R.id.tb_back:			
			TaobaoActivity.this.finish();
			return;
		}
	}

	//检查网络情况
	private AlertDialog isNotNet() {
		boolean bool = NetWorkStateUtil.isConnected(this);
		AlertDialog localAlertDialog = null;
		if (!bool)
			localAlertDialog = new AlertDialog.Builder(this)
					.setTitle("提示")
					.setMessage("你的网络不给力,请稍后再试")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface paramDialogInterface,
										int paramInt) {
									TaobaoActivity.this.onBackPressed();
									TaobaoActivity.this.finish();
								}
							}).create();
		return localAlertDialog;
	}

	
	private AlertDialog isNullUri(String paramString) {
		AlertDialog localAlertDialog;
		if (paramString != null) {
			boolean bool = paramString.equals("");
			localAlertDialog = null;
			if (!bool);
		} else {
			localAlertDialog = new AlertDialog.Builder(this)
					.setTitle("提示")
					.setMessage("你访问的页面不存在,请返回上一层")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface paramDialogInterface,
										int paramInt) {
									TaobaoActivity.this.finish();
								}
							}).create();
		}
		return localAlertDialog;
	}

}
