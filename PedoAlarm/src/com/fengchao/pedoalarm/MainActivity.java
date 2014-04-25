package com.fengchao.pedoalarm;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements SensorEventListener
{
    
    private TextView textView2;
    private TextView tvSensors;
    private Button btnStart;
    private Button btnStop;
    private Button btnPStart;
    private Button btnPStop;
    boolean flag = true; //sensor control
    float lastPoint =0; //sensor
    int count =0; //steps
    int stepGoal = 60; //stepGoal, default 60 before user set
    Calendar calendar; //for start alarm, stop alarm instantly, a helper calendar
    Calendar endDate; //exercise end date
    Calendar nextAlarm;//a helper calendar for set the next alarm.
    SharedPreferences pref;//store user setting
     
    

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //calendar for start the alarm after restart, set to now
		calendar =Calendar.getInstance();  
    	
    	//retrieve alarm status, setting status and other info.
        pref = getApplicationContext().getSharedPreferences(
                "PedoAlarm", MODE_PRIVATE);
       
        //start the setting activity for first time use
        if (pref.getBoolean("first_run", true))
        {
        	Intent intent = new Intent(MainActivity.this, Init.class);
    		startActivity(intent);
        }
        
        //check if the alarm is on after system reboot.
        if (pref.getBoolean("alarmOn", false))
        	
        {
        	setDailyAlarm (calendar);
        }

        //retrieve steps goal and end date 
        stepGoal = pref.getInt("stepGoal", 50);
        endDate = Calendar.getInstance();
        endDate.setTimeInMillis(pref.getLong("End_Date", System.currentTimeMillis()));
        
        

        //  get SensorManager object
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        //  register orientation sensor
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_FASTEST);

        
        //some views
        textView2 = (TextView) findViewById(R.id.textView2);
        tvSensors = (TextView)findViewById(R.id.tvSensors);
        
        
        //just for demo purpose, start the alarm now instead of wait for the actual trigger time.
        //will be removed in release version
        Button btnStart=(Button)findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {  
        	
        	public void onClick(View v) {  
        		
        		calendar.setTimeInMillis(System.currentTimeMillis());  
        		calendar.add(Calendar.SECOND,1); //start alarm after 1s
        		setDailyAlarm(calendar);
        		
        	}
        });
        
        //just for demo purpose, stop the alarm now instead of do the actual purchase and exercise.
        //will fulfill the purchase function in release version
        Button btnStop=(Button)findViewById(R.id.btnStop);
        btnStop.setOnClickListener(new View.OnClickListener() {  
      	  
        	public void onClick(View v) { 
        		
        		stopDailyAlarm(calendar);
        		
        	}
        });
        
        //just for demo purpose, get the settings activity, otherwise it only shows on the first time.
        //will be removed in release version
        Button btnPStart=(Button)findViewById(R.id.btnPStart);
        btnPStart.setOnClickListener(new View.OnClickListener() {  
      	  
        	public void onClick(View v) { 
        		
        		Intent intent = new Intent(MainActivity.this, Init.class);
        		startActivity(intent);
        		
        	}
        });        
    }
    
    /**
     * pedometer core function
     */
    @Override
    public void onSensorChanged(SensorEvent event){

                /*String orientation = "direction\n" + "X：" + event.values[0] + "\n"
                        + "Y:" + event.values[1] + "\n" + "Z:" + event.values[2] + "\n";
                tvOrientation.setText(orientation);*/
                
                //the pedometer code
                if (flag)
                {
                    lastPoint = event.values[1];
                    flag = false;
                }
                //  if the difference of two value[1] larger than 23, consider as 1 step. 
                if (Math.abs(event.values[1] - lastPoint) > 23)
                {
                    //  
                    lastPoint = event.values[1];
                    //  show steps
                    tvSensors.setText(String.valueOf(++count));
                }
                
                //count steps
                if (count==stepGoal) 
                	{
                		
                		stopDailyAlarm(endDate);                		
                		textView2.setText("Congratulations!!!");
                		
                	}
                
        }
    
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
    }
    
/**
 * set alarm
 * @param nextAlarm the time next alarm to be started
 */
    public void setDailyAlarm(Calendar nextAlarm )
	{
    	count = 0;
		Intent intent =new Intent(MainActivity.this,TimeReciever.class);  
		System.out.println("------------q1-------");  
		//create PendingIntent  
		PendingIntent sender=PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);  
		// set PendingIntent run for once
		AlarmManager alarmManager;  
		alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
		
		//some alternatives
		//alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);  
		//alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,SystemClock.elapsedRealtime(), 5*1000, sender);
		alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,nextAlarm.getTimeInMillis(), 3000, sender);
		// toast notification-------------------------------unused block  SystemClock.elapsedRealtime()
		Toast.makeText(getBaseContext(), "Alarm On!",Toast.LENGTH_LONG).show();  
		
	}
    
    /**
     * stop alarm, and set new alarm for next training time
     * @param ExpireCalc the day that user set at first to stop training
     */
    public void stopDailyAlarm(Calendar expireDate){
    	Intent intent =new Intent(MainActivity.this,TimeReciever.class);  
    	PendingIntent pendingIntent=PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0); 
    	
      
    	AlarmManager alarmManager;  
    	alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);  
    	alarmManager.cancel(pendingIntent);  
    	Toast.makeText(MainActivity.this, "Alarm Off!", 3).show();  
    	
    	Editor editor = pref.edit();
    	editor.putBoolean("alarmOn", false);
    	editor.commit();
    	
    	calendar.setTimeInMillis(System.currentTimeMillis());
    	
    	
    	//set the next alarm on tomorrow
    	if ( calendar.before(expireDate)){
    		
    		nextAlarm  =Calendar.getInstance(); 
        	nextAlarm.setTimeInMillis(System.currentTimeMillis());  
        	nextAlarm.set(Calendar.HOUR_OF_DAY,endDate.get(Calendar.HOUR_OF_DAY));
        	nextAlarm.set(Calendar.HOUR_OF_DAY,endDate.get(Calendar.MINUTE));
        	nextAlarm.add(Calendar.DAY_OF_MONTH,1);
        	setDailyAlarm(nextAlarm);
    		
    	}
    	
    	

    	
    	
    	
	
	
		
	}
    
    }
    

