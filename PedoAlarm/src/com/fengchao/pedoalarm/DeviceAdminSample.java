package com.fengchao.pedoalarm;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
/**
 * The DeviceAdmin Receiver
 * This app just prevent the user from uninstallation, thus not much needs to be done. Leave empty but a toast.
 * @author Fengchao
 *
 */
public class DeviceAdminSample extends DeviceAdminReceiver {
	 @Override
	 public void onEnabled(Context context, Intent intent) {
		 Toast.makeText(context, "Cheers! You cannot uninstall RunningMate until you reach your goal!!", Toast.LENGTH_LONG).show();
	    }

}
