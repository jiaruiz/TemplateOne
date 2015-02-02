package com.hc.nmj.ui;


import java.io.File;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hc.nmj.R;
import com.hc.nmj.base.BaseActivity;
import com.hc.nmj.base.LoadJson;
import com.hc.nmj.security.DownloadTask;
import com.hc.nmj.security.UpdateInfo;
import com.hc.nmj.security.UpdateInfoService;

/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
public class LoadActivity extends BaseActivity {
	
	
	LoadJson loadjson = new LoadJson();
	String adUrl = "http://www.ankobeauty.com/anko/index.php/Index/Index/aad";
	private ImageView mLoadItem_iv = null;
	private UpdateInfo info;
	private String version;
	private TextView tv_version;
	
	//进度条对话框 下载新apk
	private ProgressDialog progressDialog;
	
	private static final String TAG = "Security";
	
	/**
	 *取得信息队列里面msg 如果msg.what=1 就说明需要更新 
	 */
    
//	private Handler handler = new Handler() {
//		public void handleMessage(Message msg) {
//			if (isNeedUpdate(version)) {
//				showUpdateDialog();
//			}
//		};
//	};
    
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_load);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//mHandler = new Handler(getMainLooper());
		
//		tv_version = (TextView) findViewById(R.id.tv_splash_version);
//		version = getVersion();
//		tv_version.setText("版本号  " + version);
		
		findViewById();
		initView();
		
		//设计一个进度条
//        progressDialog = new ProgressDialog(this);
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//        progressDialog.setMessage("正在下载...");
        //延迟3秒钟 
//		new Thread() {
//			public void run() {
//				try {
//					sleep(3000);
//					loadjson.getJson(adUrl);
//					handler.sendEmptyMessage(0);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			};
//		}.start();
	}
	

	protected void findViewById() {
		// TODO Auto-generated method stub
		mLoadItem_iv = (ImageView) findViewById(R.id.splash_loading_item);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		Animation translate = AnimationUtils.loadAnimation(this,
				R.anim.splash_loading);

		translate.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				Toast.makeText(LoadActivity.this, "skip activity", Toast.LENGTH_LONG).show();
				openActivity(HomeActivity.class);
//				overridePendingTransition(R.anim.push_left_in,
//						R.anim.push_left_out);
				LoadActivity.this.finish();
			}

		});
		mLoadItem_iv.setAnimation(translate);

	}
	

    /**
     * 升级提醒对话框
     */
	private void showUpdateDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setTitle("升级提醒");
		builder.setMessage(info.getDescription()+" "+info.getUrl());
		builder.setCancelable(false);
		//确定之后下载远程apk文件
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				//在手机内存中建立下载文件
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					File dir = new File(Environment
							.getExternalStorageDirectory(), "/security/update");
					if (!dir.exists()) {
						dir.mkdirs();
					}
					String apkPath = Environment.getExternalStorageDirectory()
							+ "/security/update/anko.apk";
					//更新任务 远程路径 
					UpdateTask task = new UpdateTask(info.getUrl(), apkPath);
					progressDialog.show();
					new Thread(task).start();
				} else {
					Toast.makeText(LoadActivity.this, "SD卡不可用，请插入SD卡",
							Toast.LENGTH_SHORT).show();
					loadMainUI();
				}
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				loadMainUI();
			}

		});
		builder.create().show();
	}
	
	
    /**
     * 检查是否要更新
     * @param version 当前的版本
     * @return
     */
    private boolean isNeedUpdate(String version)
    {
		UpdateInfoService updateInfoService = new UpdateInfoService(this);
		try {
			//远程地址http://www.ankobeauty.com/my800/update.xml 解析 并取出实例化成info对象
			info = updateInfoService.getUpdateInfo(R.string.serverUrl);
			//获取版本
			String v = info.getVersion();
			//假如版本与当前版本一致不需要更新
			if (v.equals(version)) {
				Log.i(TAG, "当前版本：" + version);
				Log.i(TAG, "最新版本：" + v);
				loadMainUI();
				return false;
			} else {
				Log.i(TAG, "需要更新");
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "获取更新信息异常，请稍后再试", Toast.LENGTH_SHORT).show();
			loadMainUI();
		}
		return false;
    }
    
    
    /**
     * 获取当前版本号
     * @return
     */
    private String getVersion()
    {
		try {
			PackageManager packageManager = getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					getPackageName(), 0);

			return packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "版本号未知";
		}
    }
	
    
    //跳转
	private void loadMainUI() {
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
		finish();
	}
    

    
    //===========================================================================================
    
    /**
     * 下载的线程
     * 
     */
	class UpdateTask implements Runnable {
		private String path;
		private String filePath;

		public UpdateTask(String path, String filePath) {
			this.path = path;
			this.filePath = filePath;
		}

		@Override
		public void run() {
			try {
				//下载远程apk到本地磁盘中 path 为远程apk地址
				//filePath为本地存储apk的文件路径
				File file = DownloadTask.getFile(path, filePath, progressDialog);
				progressDialog.dismiss();
				install(file);
			} catch (Exception e) {
				e.printStackTrace();
				progressDialog.dismiss();
				Toast.makeText(LoadActivity.this, "更新失败", Toast.LENGTH_SHORT)
						.show();
				loadMainUI();
			}
		}
           
    }
	
    /**
     * 安装apk
     * @param file 要安装的apk的目录
     */
	private void install(File file) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		finish();
		startActivity(intent);
	}


}
