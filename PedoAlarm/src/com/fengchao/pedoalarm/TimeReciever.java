package com.fengchao.pedoalarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Vibrator;
import android.widget.Toast;
  
/**
 * Receive alarm broadcast and start the main activity  
 * @author Fengchao
 *
 */
public class TimeReciever extends BroadcastReceiver {  
	@Override  
	public void onReceive(Context context, Intent intent) {  
	// TODO Auto-generated method stub  
		Toast.makeText(context, "Time to RUN!!!!!", Toast.LENGTH_SHORT).show();  

		//Set alarm flag to reset alarm if user reboot.
		SharedPreferences pref = context.getSharedPreferences(
		        "PedoAlarm", Context.MODE_PRIVATE); //Really don't know why the MODE_PRIVATE only cannot work.
		Editor editor = pref.edit();
		editor.putBoolean("alarmOn", true);	
		editor.commit();

		//start vibration
		Vibrator v;
		v=(Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
		v.vibrate(3000);


		intent.setClass(context,  MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	
	}

}  