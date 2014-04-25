package com.fengchao.pedoalarm;

import java.util.Calendar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

/**
 * Set the user preference 
 * @author Fengchao
 *
 */
public class Init extends Activity {
	
	Calendar cEnd;
	EditText stepGoal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_init);
		// Show the Up button in the action bar.
		setupActionBar();
		Button setTime=(Button)findViewById(R.id.setTime);	
		Button setDate =(Button)findViewById(R.id.setDate); 
		Button confirm =(Button)findViewById(R.id.confirm);
		stepGoal = (EditText)findViewById(R.id.stepGoal);
		cEnd = Calendar.getInstance();
		cEnd.setTimeInMillis(System.currentTimeMillis());
		
		
		//get DeviceAdmin enabled
		ComponentName devadmin
        = new ComponentName(this, DeviceAdminSample.class);
		Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, devadmin);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                "Enable to use RunningMate");
        startActivity(intent);
		
		/**
		 * set alarm time
		 */
		setTime.setOnClickListener(new OnClickListener() {
	        
	         @Override
	         public void onClick(View v) {
	        	//get the button press time as the default value for TimePickerDialog
	        	 
	             int mHour =cEnd.get(Calendar.HOUR_OF_DAY);
	             int mMinute =cEnd.get(Calendar.MINUTE);
	             new TimePickerDialog(Init.this,new TimePickerDialog.OnTimeSetListener() {
	            	 @Override
                     public void onTimeSet(TimePicker view,int hourOfDay,int minute) {
	            		 cEnd.set(Calendar.HOUR_OF_DAY, hourOfDay);
	            		 cEnd.set(Calendar.MINUTE, minute);
	            		 cEnd.set(Calendar.SECOND, 0);
	            		 cEnd.set(Calendar.MILLISECOND, 0);
	            		 
	            		 //Debug only
	            		  String tmpS = format(hourOfDay)+":"+ format(minute);
	            		  Toast.makeText(Init.this,"Time :"+tmpS, Toast.LENGTH_LONG).show();
	            	 }
	             },mHour, mMinute,true).show();
	         }			
			});
		
		/**
		 * set end date
		 */
		setDate.setOnClickListener(new OnClickListener() {
	        
	         @Override
	         public void onClick(View v) {
	        	//get the button press time as the default value for TimePickerDialog
	        	 
	             int mYear =cEnd.get(Calendar.YEAR);
	             int mMonth =cEnd.get(Calendar.MONTH);
	             int mDay =cEnd.get(Calendar.DAY_OF_MONTH);
	             
	             new DatePickerDialog (Init.this,new DatePickerDialog.OnDateSetListener() {
	            	 @Override
	            	 public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
	            		 cEnd.set(Calendar.YEAR, year);
	            		 cEnd.set(Calendar.MONTH, monthOfYear);
	            		 cEnd.set(Calendar.DAY_OF_MONTH, dayOfMonth);
	            		 cEnd.set(Calendar.SECOND, 0);
	            		 cEnd.set(Calendar.MILLISECOND, 0);
	            		 //Debug only
	            		  String tmpS = ""+year+"/"+monthOfYear+"/"+dayOfMonth;
	            		  Toast.makeText(Init.this,"Date:"+tmpS, Toast.LENGTH_LONG).show();
	            	 }
					}
	             ,mYear, mMonth,mDay).show();
	         }			
			});

		/**
		 * confirm button saves calendar, 1st use info to sharedPrefere
		 */
		confirm.setOnClickListener(new OnClickListener() {
	        
	         @Override
	         public void onClick(View v) {
	        	 //get step goal
	        	 String stepgoal = stepGoal.getText().toString();
	        	 int stepGoal = Integer.parseInt(stepgoal);
	        	 //save to SharedPreferences for future use
	        	 SharedPreferences pref = getApplicationContext().getSharedPreferences(
                         "PedoAlarm", MODE_PRIVATE);
	        	 Editor editor = pref.edit();
	        	 editor.putLong("End_Date", cEnd.getTimeInMillis());
	        	 editor.putBoolean("first_run", false);	
	        	 editor.putInt("stepGoal", stepGoal);
	        	 editor.commit();
	        	 Intent intent = new Intent(Init.this, MainActivity.class);
	        	 startActivity(intent);
	        	 Toast.makeText(Init.this,"Exercise Plan Set!", Toast.LENGTH_LONG).show();

	         }			
			});
		}
	
	/**
	 * a function to conver the time format
	 * @param x
	 * @return
	 */
	   private String format(int x)
	    {
	      String s = "" + x;
	      if (s.length() == 1)
	        s = "0" + s;
	      return s;


	}
	
	


	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.init, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
