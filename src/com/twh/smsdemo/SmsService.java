package com.twh.smsdemo;

import java.sql.Date;
import java.text.SimpleDateFormat;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.telephony.gsm.SmsMessage;
import android.view.View;
import android.widget.Toast;
import com.twh.smsdemo.*;

public class SmsService extends Service {

	public static boolean abortSMS = false;
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		return super.onUnbind(intent);
	}  
	
	@Override  
    public void onCreate() {  
        super.onCreate();
     }

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Toast.makeText(this, "destory service", Toast.LENGTH_SHORT).show();  
		unregisterReceiver(SMSreceiver);
		super.onDestroy();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "start service", Toast.LENGTH_SHORT).show();  
		//过滤器
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
		//设置优先级
		intentFilter.setPriority(2147483647);
		//注册监听器
		registerReceiver(SMSreceiver, intentFilter);
		super.onStart(intent, startId);
	}

	
	private void camera(){

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
		intent.putExtra("camerasensortype", 2); 
		// 调用前置摄像头 
		intent.putExtra("autofocus", true); 
		// 自动对焦 
		intent.putExtra("fullScreen", false); 
		// 全屏 
		intent.putExtra("showActionIcons", false); 
//		startActivityForResult(intent, PICK_FROM_CAMERA); 
		startActivity(intent);
	}

	private BroadcastReceiver SMSreceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if ("android.provider.Telephony.SMS_RECEIVED".equals(intent.getAction()))
			{
				if(abortSMS) SMSreceiver.abortBroadcast();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");
				Date curDate = new Date(System.currentTimeMillis());
				String messagedate = formatter.format(curDate);
				StringBuilder sb = new StringBuilder();
				// 接收由SMS传过来的数据
				Bundle bundle = intent.getExtras();
				// 判断是否有数据
				if (bundle != null)
				{
					// 通过pdus可以获得接收到的所有短信消息
					Object[] objArray = (Object[]) bundle.get("pdus");
					/* 构建短信对象array,并依据收到的对象长度来创建array的大小 */
					SmsMessage[] messages = new SmsMessage[objArray.length];
					for (int i = 0; i < objArray.length; i++)
					{
						messages[i] = SmsMessage.createFromPdu((byte[]) objArray[i]);
					}
					/* 将送来的短信合并自定义信息于StringBuilder当中 */
					for (SmsMessage currentMessage : messages)
					{
						sb.append("短信来源:");
						// 获得接收短信的电话号码
						sb.append(currentMessage.getDisplayOriginatingAddress());
						sb.append("\n------短信内容------\n");
						// 获得短信的内容
						sb.append(currentMessage.getDisplayMessageBody());
					}
				}
				Toast.makeText(context, sb.toString(), Toast.LENGTH_LONG).show();
//				camera();
				Intent i = new Intent(SmsService.this,MainActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				i.putExtra("setImage", 1);
				startActivity(i);
			}
		}
	};
	

}
