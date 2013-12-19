package com.twh.smsdemo;

import java.sql.Date;
import java.text.SimpleDateFormat;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.telephony.gsm.SmsMessage;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Button btn_start;
	private Button btn_stop;
	private Button btn_abortSms;
	public static ImageView image;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btn_start = (Button)findViewById(R.id.button_start);
		btn_stop = (Button)findViewById(R.id.button_stop);
		btn_abortSms = (Button)findViewById(R.id.button_abortsms);
		image = (ImageView)findViewById(R.id.imageView);
		

		btn_start.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent("com.twh.smsdemo.SmsService"); 
				startService(intent);
			}
		});
		
		btn_stop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent("com.twh.smsdemo.SmsService"); 
				stopService(intent);
			}
		});
		
		btn_abortSms.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				SmsService.abortSMS = !SmsService.abortSMS;
				showToast("abort sms boardcast? " + SmsService.abortSMS);
				setImage();
			}
		});
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		Intent i = getIntent();
		if(i != null){
	        int imageOrNot = i.getIntExtra("setImage",0);
	        if(imageOrNot == 1){
	        	setImage();
	        }
		}

		super.onStart();
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		// TODO Auto-generated method stub
		super.startActivityForResult(intent, requestCode);
	}

	private void showToast(String str){
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void setImage(){
		image.setImageBitmap(takeScreenShot(this));
	}
	
	// 获取指定Activity的截屏，保存到png文件
    private static Bitmap takeScreenShot(Activity activity) {
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay().getHeight();
        
        // View是你需要截图的View
        View view = activity.getWindow().getDecorView();
    	view.layout(0, 0, width,height);  
        view.setDrawingCacheEnabled(true); 
        view.setFocusable(true);  
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();
        
        // 获取状态栏高度
        //Rect frame = new Rect();
        //activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        //int statusBarHeight = frame.top;
        //Log.i("TAG", "" + statusBarHeight);

       // 去掉标题栏
        // Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);
        Bitmap b = Bitmap.createBitmap(b1, 0, 0, width, height);
        view.destroyDrawingCache();
        return b;
      
    }

}


