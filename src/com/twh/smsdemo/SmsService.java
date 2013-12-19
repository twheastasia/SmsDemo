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
		//������
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
		//�������ȼ�
		intentFilter.setPriority(2147483647);
		//ע�������
		registerReceiver(SMSreceiver, intentFilter);
		super.onStart(intent, startId);
	}

	
	private void camera(){

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
		intent.putExtra("camerasensortype", 2); 
		// ����ǰ������ͷ 
		intent.putExtra("autofocus", true); 
		// �Զ��Խ� 
		intent.putExtra("fullScreen", false); 
		// ȫ�� 
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
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy��MM��dd��HH:mm:ss");
				Date curDate = new Date(System.currentTimeMillis());
				String messagedate = formatter.format(curDate);
				StringBuilder sb = new StringBuilder();
				// ������SMS������������
				Bundle bundle = intent.getExtras();
				// �ж��Ƿ�������
				if (bundle != null)
				{
					// ͨ��pdus���Ի�ý��յ������ж�����Ϣ
					Object[] objArray = (Object[]) bundle.get("pdus");
					/* �������Ŷ���array,�������յ��Ķ��󳤶�������array�Ĵ�С */
					SmsMessage[] messages = new SmsMessage[objArray.length];
					for (int i = 0; i < objArray.length; i++)
					{
						messages[i] = SmsMessage.createFromPdu((byte[]) objArray[i]);
					}
					/* �������Ķ��źϲ��Զ�����Ϣ��StringBuilder���� */
					for (SmsMessage currentMessage : messages)
					{
						sb.append("������Դ:");
						// ��ý��ն��ŵĵ绰����
						sb.append(currentMessage.getDisplayOriginatingAddress());
						sb.append("\n------��������------\n");
						// ��ö��ŵ�����
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
