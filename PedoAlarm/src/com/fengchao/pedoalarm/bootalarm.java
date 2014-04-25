package com.fengchao.pedoalarm;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * start the activity if the alarm is ringing.s
 * @author Fengchao
 *
 */
public class bootalarm extends BroadcastReceiver{
	
	public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
         if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
        	 SharedPreferences pref = context.getSharedPreferences(
        		        "PedoAlarm", Context.MODE_PRIVATE); //Really don't know why the MODE_PRIVATE only cannot work.
        	 
        	 System.out.println("---------3----------");  
        	 
        	 
        	 if (pref.getBoolean("alarmOn", false))
             {
        		 intent.setClass(context,  MainActivity.class);
        		 intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        		 System.out.println("---------4----------");  
        		 context.startActivity(intent);
         		
         		
             }
        	 
            }
    }
	
	

}
